package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Standalone unit tests for {@link VoltageCompensator}.  These run without the
 * full FTC SDK by relying on the minimal interface stubs under
 * {@code test/com/qualcomm/robotcore/}.
 *
 * <p>Run via {@code ./runtests.sh} or directly:
 * <pre>
 *   javac -d build \
 *     VoltageCompensator.java \
 *     test/com/qualcomm/robotcore/hardware/VoltageSensor.java \
 *     test/com/qualcomm/robotcore/util/Range.java \
 *     test/com/qualcomm/robotcore/external/Telemetry.java \
 *     test/org/firstinspires/ftc/teamcode/util/VoltageCompensatorTest.java
 *   java -cp build org.firstinspires.ftc.teamcode.util.VoltageCompensatorTest
 * </pre>
 */
public class VoltageCompensatorTest {

    private static int passed = 0;
    private static int failed = 0;
    private static final double TOL = 1e-6;

    // ── Test harness ──────────────────────────────────────────────────────

    private static void assertClose(String name, double got, double want, double tol) {
        if (Double.isNaN(got) || Math.abs(got - want) > tol) {
            failed++;
            System.out.printf(Locale.ROOT,
                              "FAIL %s: got %.6f, want %.6f (tol %.6f)%n",
                              name, got, want, tol);
        } else {
            passed++;
        }
    }

    private static void assertClose(String name, double got, double want) {
        assertClose(name, got, want, TOL);
    }

    private static void assertEqual(String name, boolean got, boolean want) {
        if (got != want) {
            failed++;
            System.out.printf(Locale.ROOT, "FAIL %s: got %s, want %s%n", name, got, want);
        } else {
            passed++;
        }
    }

    private static void assertHas(String name, boolean got, String label) {
        if (got) {
            passed++;
        } else {
            failed++;
            System.out.printf(Locale.ROOT, "FAIL %s: expected label \"%s\" to be present%n",
                              name, label);
        }
    }

    // ── Test doubles ──────────────────────────────────────────────────────

    /** {@link VoltageSensor} that returns whatever voltage we tell it to. */
    private static final class FakeSensor implements VoltageSensor {
        double v;
        FakeSensor(double v) { this.v = v; }
        @Override public double getVoltage() { return v; }
    }

    /** {@link Telemetry} that just captures every {@code addData} call. */
    private static final class FakeTelemetry implements Telemetry {
        final Map<String, String> data = new LinkedHashMap<>();
        @Override public Telemetry addData(String key, String value) {
            data.put(key, value); return this;
        }
        @Override public Telemetry addData(String key, Object value) {
            data.put(key, String.valueOf(value)); return this;
        }
        @Override public Telemetry addData(String key, String fmt, Object... args) {
            data.put(key, String.format(Locale.ROOT, fmt, args));
            return this;
        }
        @Override public void update() { }
        boolean has(String key) { return data.containsKey(key); }
    }

    // ── Tests ─────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        testInitialCompensationFactorIsOne();
        testInitialSmoothedVoltageIsNominal();
        testInitialNoBrownout();
        testInitialTrendIsZero();

        testFirstUpdateFillsRollingBufferExactly();
        testUpdateAtNominalKeepsCompensationAtOne();
        testCompensationAtLowerVoltage();
        testCompensationCapAtMax();
        testCompensationFloorAtOne();

        testBrownoutBelowThreshold();
        testNoBrownoutAtOrAboveThreshold();
        testBrownoutHalvesDrivePower();
        testBrownoutHalvesMotorPower();
        testBrownoutHalvesServoPower();

        testDriveCurveCubicShapingPositive();
        testDriveCurveCubicShapingNegative();
        testMotorSkipsDriveCurve();

        testServoUsesNarrowerCap();
        testServoFloorAtOneWhenBatteryHigh();

        testNullSensorFallsBackToNominal();
        testTelemetryReceivesBrownoutMessage();
        testTelemetryReceivesNullSensorWarning();

        testRepeatedUpdatesAtNominalStayStable();
        testRepeatedUpdatesAtLowBatteryStayStable();

        report();
    }

    private static void testInitialCompensationFactorIsOne() {
        VoltageCompensator c = new VoltageCompensator(false);
        assertClose("initial compFactor", c.getCompensationFactor(), 1.0);
    }

    private static void testInitialSmoothedVoltageIsNominal() {
        VoltageCompensator c = new VoltageCompensator(false);
        assertClose("initial smoothed voltage", c.getVoltage(), 12.0);
    }

    private static void testInitialNoBrownout() {
        VoltageCompensator c = new VoltageCompensator(false);
        assertEqual("initial brownout flag", c.isBrownoutProtected(), false);
    }

    private static void testInitialTrendIsZero() {
        VoltageCompensator c = new VoltageCompensator(false);
        assertClose("initial trend slope", c.getTrend(), 0.0);
    }

    private static void testFirstUpdateFillsRollingBufferExactly() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(11.0));
        // Buffer-fill logic ⇒ smoothed voltage matches raw on the first cycle
        // (no slow ramp-up from zero-initialised slots).
        assertClose("first update at 11V: smoothed", c.getVoltage(), 11.0, 1e-9);
    }

    private static void testUpdateAtNominalKeepsCompensationAtOne() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(12.0));
        assertClose("at 12V: smoothed",    c.getVoltage(),            12.0,   1e-9);
        assertClose("at 12V: compFactor",  c.getCompensationFactor(),  1.0,   1e-9);
    }

    private static void testCompensationAtLowerVoltage() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(11.0));
        // 12 / 11 ≈ 1.09090…  (single sample ⇒ regression slope = 0)
        assertClose("at 11V: compFactor", c.getCompensationFactor(), 12.0 / 11.0, 1e-6);
    }

    private static void testCompensationCapAtMax() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(8.0));   // 12/8 = 1.5 → clipped to MAX_SAG_COMPENSATION (1.4)
        assertClose("at 8V: compFactor capped", c.getCompensationFactor(), 1.4, 1e-9);
    }

    private static void testCompensationFloorAtOne() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(14.0));  // above nominal ⇒ floored to 1.0
        assertClose("at 14V: compFactor floored", c.getCompensationFactor(), 1.0, 1e-9);
    }

    private static void testBrownoutBelowThreshold() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(10.4));
        assertEqual("at 10.4V: brownout active", c.isBrownoutProtected(), true);
    }

    private static void testNoBrownoutAtOrAboveThreshold() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(10.5));
        assertEqual("at 10.5V: brownout inactive", c.isBrownoutProtected(), false);
        c.update(new FakeSensor(12.0));
        assertEqual("at 12.0V: brownout inactive", c.isBrownoutProtected(), false);
    }

    private static void testBrownoutHalvesDrivePower() {
        VoltageCompensator c = new VoltageCompensator(true);
        c.update(new FakeSensor(10.0));  // compFactor = 12/10 = 1.2
        double out = c.compensateDrive(1.0);
        // curve(1.0) = 1.0;  compensated = 1.0 * 1.2 * 0.5 (brownout)
        assertClose("brownout halves drive at 10V", out, 0.6, 1e-6);
    }

    private static void testBrownoutHalvesMotorPower() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(10.0));
        double out = c.compensateMotor(1.0);
        // 1.0 * 1.2 * 0.5 (brownout) = 0.6
        assertClose("brownout halves motor at 10V", out, 0.6, 1e-6);
    }

    private static void testBrownoutHalvesServoPower() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(10.0));  // servoFactor = min(1.2, 1.15) = 1.15
        double out = c.compensateServo(1.0);
        // 1.0 * 1.15 * 0.5 (brownout) = 0.575
        assertClose("brownout halves servo at 10V", out, 0.575, 1e-6);
    }

    private static void testDriveCurveCubicShapingPositive() {
        VoltageCompensator c = new VoltageCompensator(true);
        c.update(new FakeSensor(12.0));  // compFactor = 1.0
        // curve(0.5) = 0.125*0.3 + 0.5*0.7 = 0.3875
        double expected = 0.125 * 0.3 + 0.5 * 0.7;
        assertClose("cubic curve at +0.5", c.compensateDrive(0.5), expected, 1e-9);
        assertClose("cubic curve at 0",   c.compensateDrive(0.0), 0.0, 1e-9);
        assertClose("cubic curve at +1",  c.compensateDrive(1.0), 1.0, 1e-9);
    }

    private static void testDriveCurveCubicShapingNegative() {
        VoltageCompensator c = new VoltageCompensator(true);
        c.update(new FakeSensor(12.0));
        // curve(-0.5) = (-0.125)*0.3 + (-0.5)*0.7 = -0.3875
        double expected = -0.125 * 0.3 + (-0.5) * 0.7;
        assertClose("cubic curve at -0.5", c.compensateDrive(-0.5), expected, 1e-9);
        assertClose("cubic curve at -1",   c.compensateDrive(-1.0), -1.0, 1e-9);
    }

    private static void testMotorSkipsDriveCurve() {
        VoltageCompensator noCurve   = new VoltageCompensator(false);
        VoltageCompensator withCurve = new VoltageCompensator(true);
        noCurve.update(new FakeSensor(12.0));
        withCurve.update(new FakeSensor(12.0));

        double m = noCurve.compensateMotor(0.5);    // 0.5 * 1.0
        double d = withCurve.compensateDrive(0.5);   // 0.3875 * 1.0
        assertClose("motor raw multiplier", m, 0.5, 1e-9);
        assertClose("drive uses cubic",    d, 0.3875, 1e-9);
    }

    private static void testServoUsesNarrowerCap() {
        // At 8V every cap is engaged: compFactor → 1.4 (MAX_SAG), servoFactor →
        // 1.15 (MAX_SERVO), and the brownout halving (8 < 10.5) applies too.
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(8.0));

        double driveOut = c.compensateDrive(0.5);  // curve off → 0.5 * 1.4 * 0.5 = 0.35
        double servoOut = c.compensateServo(0.5);  //          → 0.5 * 1.15 * 0.5 = 0.2875

        assertClose("drive cap-and-brownout at 8V", driveOut, 0.35,   1e-9);
        assertClose("servo narrower cap at 8V",    servoOut, 0.2875, 1e-9);

        // The point of this test: servo cap is narrower than drive cap, so even
        // with identical inputs and both caps engaged, servo outputs strictly
        // less than drive.
        assertEqual("servo narrower than drive under low battery", servoOut < driveOut, true);
    }

    private static void testServoFloorAtOneWhenBatteryHigh() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(new FakeSensor(13.5));  // compFactor = clip(12/13.5, 1, 1.4) = 1.0
        double out = c.compensateServo(0.5);
        assertClose("servo floor at 1.0 with fresh battery", out, 0.5, 1e-9);
    }

    private static void testNullSensorFallsBackToNominal() {
        VoltageCompensator c = new VoltageCompensator(false);
        c.update(null);
        assertClose("null sensor: smoothed voltage", c.getVoltage(), 12.0, 1e-9);
        assertClose("null sensor: compFactor",       c.getCompensationFactor(), 1.0, 1e-9);
    }

    private static void testTelemetryReceivesBrownoutMessage() {
        VoltageCompensator c = new VoltageCompensator(false);
        FakeTelemetry t = new FakeTelemetry();
        c.update(new FakeSensor(10.0), t);
        assertHas("brownout telemetry label", t.has("BROWNOUT"), "BROWNOUT");
    }

    private static void testTelemetryReceivesNullSensorWarning() {
        VoltageCompensator c = new VoltageCompensator(false);
        FakeTelemetry t = new FakeTelemetry();
        c.update(null, t);
        assertHas("null sensor telemetry label", t.has("WARNING"), "WARNING");
    }

    private static void testRepeatedUpdatesAtNominalStayStable() {
        VoltageCompensator c = new VoltageCompensator(false);
        FakeSensor s = new FakeSensor(12.0);
        for (int i = 0; i < 200; i++) c.update(s);
        assertClose("stable at 12V: voltage",     c.getVoltage(),            12.0, 1e-9);
        assertClose("stable at 12V: compFactor",  c.getCompensationFactor(),  1.0, 1e-9);
        assertEqual("stable at 12V: brownout",   c.isBrownoutProtected(), false);
    }

    private static void testRepeatedUpdatesAtLowBatteryStayStable() {
        VoltageCompensator c = new VoltageCompensator(false);
        FakeSensor s = new FakeSensor(11.0);
        for (int i = 0; i < 200; i++) c.update(s);
        assertClose("flat at 11V: voltage",     c.getVoltage(),            11.0, 1e-9);
        assertClose("flat at 11V: compFactor",  c.getCompensationFactor(), 12.0 / 11.0, 1e-6);
    }

    private static void report() {
        System.out.println();
        System.out.println("============================================");
        System.out.printf(Locale.ROOT, "Tests: %d   Passed: %d   Failed: %d%n",
                          passed + failed, passed, failed);
        System.out.println("============================================");
        if (failed > 0) System.exit(1);
    }
}
