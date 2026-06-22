package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Adaptive voltage compensation system for FTC TeleOp.
 *
 * Reads the Control Hub's voltage sensor every loop cycle, maintains a rolling
 * average and sag trend, and scales motor/servo outputs so the robot behaves
 * consistently regardless of battery state.
 */
public class VoltageCompensator {

    // ── Named constants (tune these during practice) ──────────────────────

    /** Battery voltage we design around (fresh 12V lead-acid). */
    private static final double NOMINAL_VOLTAGE = 12.0;

    /** Below this voltage we enter brownout protection (cap powers at 0.5). */
    private static final double BROWNOUT_THRESHOLD = 10.5;

    /** Absolute max multiplier for any motor power (prevents burning motors). */
    private static final double MAX_SAG_COMPENSATION = 1.4;

    /** Number of raw samples in the smoothing circular buffer. */
    private static final int ROLLING_WINDOW_SIZE = 20;

    /**
     * Number of smoothed samples for trend linear regression.
     * At ~30 Hz loop rate this covers ~3.3 seconds.
     */
    private static final int TREND_WINDOW_SIZE = 100;

    /** Gain for the predictive trend correction (applied to V/sec slope). */
    private static final double TREND_CORRECTION_GAIN = 0.08;

    /** Servo compensation is clamped tighter to avoid jitter on position servos. */
    private static final double MAX_SERVO_COMPENSATION = 1.15;

    // ── Circular buffer state ─────────────────────────────────────────────

    private final double[] rollingBuffer;
    private int rollingHead;
    private int rollingCount;

    private final double[] trendBuffer;
    private int trendHead;
    private int trendCount;

    // ── Internal state ────────────────────────────────────────────────────

    private double smoothedVoltage;
    private double trendSlope;
    private double compensationFactor;
    private boolean brownoutActive;
    private boolean applyDriveCurve;

    /**
     * @param applyDriveCurve whether to apply a cubic shaping curve to drive
     *                        inputs before compensating (improves low-speed
     *                        driver feel). Set true for driver-controlled teleop.
     */
    public VoltageCompensator(boolean applyDriveCurve) {
        this.applyDriveCurve = applyDriveCurve;

        rollingBuffer = new double[ROLLING_WINDOW_SIZE];
        rollingHead = 0;
        rollingCount = 0;

        trendBuffer = new double[TREND_WINDOW_SIZE];
        trendHead = 0;
        trendCount = 0;

        smoothedVoltage = NOMINAL_VOLTAGE;
        trendSlope = 0.0;
        compensationFactor = 1.0;
        brownoutActive = false;
    }

    /**
     * Call once per loop cycle FIRST, before any compensate*() calls.
     * Reads the hardware voltage sensor and updates all internal state.
     * Telemetry warnings for brownout are sent via the (optional) overload.
     */
    public void update(VoltageSensor sensor) {
        update(sensor, null);
    }

    /**
     * Like {@link #update(VoltageSensor)} but also logs brownout warnings
     * and sensor-missing warnings to the provided Telemetry instance.
     */
    public void update(VoltageSensor sensor, Telemetry telemetry) {
        double rawVoltage = NOMINAL_VOLTAGE;

        if (sensor == null) {
            if (telemetry != null) {
                telemetry.addData("WARNING", "No voltage sensor found — compensation disabled");
            }
        } else {
            rawVoltage = sensor.getVoltage();
        }

        // ── Rolling average ───────────────────────────────────────────────
        // On the very first reading fill every buffer slot so there is no
        // cold-start ramp-up artifact where the average slowly climbs.
        if (rollingCount == 0) {
            for (int i = 0; i < ROLLING_WINDOW_SIZE; i++) {
                rollingBuffer[i] = rawVoltage;
            }
            rollingHead = 0;
            rollingCount = ROLLING_WINDOW_SIZE;
        }

        rollingBuffer[rollingHead] = rawVoltage;
        rollingHead = (rollingHead + 1) % ROLLING_WINDOW_SIZE;

        double sum = 0.0;
        for (int i = 0; i < ROLLING_WINDOW_SIZE; i++) {
            sum += rollingBuffer[i];
        }
        smoothedVoltage = sum / ROLLING_WINDOW_SIZE;

        // ── Trend buffer (stores smoothed values, not raw) ────────────────
        trendBuffer[trendHead] = smoothedVoltage;
        trendHead = (trendHead + 1) % TREND_WINDOW_SIZE;
        if (trendCount < TREND_WINDOW_SIZE) {
            trendCount++;
        }

        // ── Linear regression slope (V/sec) ──────────────────────────────
        trendSlope = computeLinearRegressionSlope(trendBuffer, trendHead, trendCount);

        // ── Compensation factor ───────────────────────────────────────────
        // Base: scale power inversely with voltage so torque stays constant.
        // Trend correction: if voltage is dropping, add a small extra boost
        // pre-emptively so the driver never feels the sag.
        compensationFactor = NOMINAL_VOLTAGE / smoothedVoltage + (-trendSlope * TREND_CORRECTION_GAIN);
        compensationFactor = Range.clip(compensationFactor, 1.0, MAX_SAG_COMPENSATION);

        // ── Brownout detection ────────────────────────────────────────────
        brownoutActive = smoothedVoltage < BROWNOUT_THRESHOLD;
        if (brownoutActive && telemetry != null) {
            telemetry.addData("BROWNOUT", "Voltage %.2fV below threshold! Motors capped at 50%%",
                              smoothedVoltage);
        }
    }

    /**
     * Shaped drive power with voltage compensation.
     * Applies an optional cubic curve for better low-speed driver feel before
     * compensating.
     *
     * @param rawPower gamepad stick value in [-1.0, 1.0]
     * @return compensated power in [-1.0, 1.0]
     */
    public double compensateDrive(double rawPower) {
        double power = rawPower;

        // Cubic shaping: blends raw^3 (fine near zero) with raw (full range)
        // so the driver gets finer control at low speeds without losing top
        // end authority.
        if (applyDriveCurve) {
            power = Math.pow(rawPower, 3) * 0.3 + rawPower * 0.7;
        }

        double compensated = power * compensationFactor;

        if (brownoutActive) {
            compensated *= 0.5;
        }

        return Range.clip(compensated, -1.0, 1.0);
    }

    /**
     * Voltage-compensated power for non-drive DC motors (arm, lift, intake…).
     * Same full compensation as drive but without the cubic shaping curve.
     *
     * @param rawPower desired motor power in [-1.0, 1.0]
     * @return compensated power in [-1.0, 1.0]
     */
    public double compensateMotor(double rawPower) {
        double compensated = rawPower * compensationFactor;

        if (brownoutActive) {
            compensated *= 0.5;
        }

        return Range.clip(compensated, -1.0, 1.0);
    }

    /**
     * Compensation for continuous-rotation servos.
     * Uses a narrower compensation cap (1.15 instead of MAX_SAG_COMPENSATION)
     * because position servos jitter when over-driven.  Regular (non-CR)
     * servos should NOT be compensated at all — their internal PID handles
     * voltage variation internally.
     *
     * @param rawPower desired servo power in [-1.0, 1.0]
     * @return compensated power in [-1.0, 1.0]
     */
    public double compensateServo(double rawPower) {
        double servoFactor = Range.clip(compensationFactor, 1.0, MAX_SERVO_COMPENSATION);
        double compensated = rawPower * servoFactor;

        if (brownoutActive) {
            compensated *= 0.5;
        }

        return Range.clip(compensated, -1.0, 1.0);
    }

    // ── Telemetry helpers ─────────────────────────────────────────────────

    /** Returns the current smoothed voltage (V). */
    public double getVoltage() { return smoothedVoltage; }

    /** Returns the voltage trend (V/sec); negative means sagging. */
    public double getTrend() { return trendSlope; }

    /** Returns the current compensation multiplier [1.0 … MAX_SAG_COMPENSATION]. */
    public double getCompensationFactor() { return compensationFactor; }

    /** Returns true when brownout protection is active. */
    public boolean isBrownoutProtected() { return brownoutActive; }

    // ── Private helpers ───────────────────────────────────────────────────

    /**
     * Ordinary least-squares linear regression over a circular buffer of
     * equally-spaced time samples.  Returns slope in V/sec assuming ~30 Hz
     * sample rate.
     */
    private double computeLinearRegressionSlope(double[] buffer, int head, int count) {
        if (count < 2) {
            return 0.0;
        }

        int n = count;
        double sumX = 0.0, sumY = 0.0, sumXY = 0.0, sumXX = 0.0;

        for (int i = 0; i < n; i++) {
            double x = i; // sample index = time
            int idx = (head - n + 1 + i) % TREND_WINDOW_SIZE;
            if (idx < 0) {
                idx += TREND_WINDOW_SIZE;
            }
            double y = buffer[idx];

            sumX  += x;
            sumY  += y;
            sumXY += x * y;
            sumXX += x * x;
        }

        double denom = n * sumXX - sumX * sumX;
        if (Math.abs(denom) < 1e-10) {
            return 0.0;
        }

        double slopePerSample = (n * sumXY - sumX * sumY) / denom;

        // Convert from V/sample to V/sec assuming ~30 Hz loop rate.
        // Teams with faster loops should adjust this constant.
        double SAMPLES_PER_SECOND = 30.0;
        return slopePerSample * SAMPLES_PER_SECOND;
    }
}
