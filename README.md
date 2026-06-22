# FTC Voltage Compensator

An adaptive battery-voltage compensation system for FTC TeleOp periodic loops. It scales motor and servo outputs in real time so the robot behaves consistently as the 12V lead-acid battery sags during a match, and gracefully limits power when a brownout is imminent.

## What it does

- Reads the Control Hub's voltage sensor every loop cycle.
- Maintains a rolling average (20 samples) for smoothing and a longer buffered trend (100 samples, ~3.3 s at 30 Hz) fitted with linear regression to predict sag direction.
- Computes a compensation factor `NOMINAL_VOLTAGE / smoothedVoltage` plus a small predictive trend correction, clipped to a safe maximum.
- Detects brownout and caps all motor power at 50% below the threshold.

## Files

| File | Purpose |
|------|---------|
| `VoltageCompensator.java`     | Reusable compensation helper. Drop into `org.firstinspires.ftc.teamcode.util`. |
| `VoltageCompensatedTeleOp.java` | Reference `LinearOpMode` showing how to wire it into a typical 4-motor mecanum drivetrain, arm motor, and a mix of position + continuous-rotation servos. |

## Usage

```java
// In your OpMode:
batterySensor = hardwareMap.voltageSensor.iterator().next();
VoltageCompensator compensator = new VoltageCompensator(true); // true = cubic drive curve

while (opModeIsActive()) {
    compensator.update(batterySensor, telemetry);

    double lf = drive + strafe + rotate;
    leftFront.setPower(compensator.compensateDrive(lf));

    armMotor.setPower(compensator.compensateMotor(gamepad2.left_stick_y));

    // Position servos — DO NOT compensate; their internal PID handles voltage variation.
    // Continuous-rotation servos — call compensateServo(...) with the narrower cap.
    intakeServo.setPower(compensator.compensateServo(intakePower));
}
```

## Tunable constants

All tuning constants live at the top of `VoltageCompensator.java`:

| Constant | Default | Meaning |
|----------|---------|---------|
| `NOMINAL_VOLTAGE`         | 12.0 V | Battery voltage we design around |
| `BROWNOUT_THRESHOLD`       | 10.5 V | Below this, motor power is capped at 50% |
| `MAX_SAG_COMPENSATION`    | 1.4    | Hard ceiling on the boost factor |
| `MAX_SERVO_COMPENSATION`  | 1.15   | Tighter ceiling for servos (avoids jitter) |
| `ROLLING_WINDOW_SIZE`     | 20     | Voltage-smoothing circular buffer |
| `TREND_WINDOW_SIZE`       | 100    | Samples fed into the linear-regression slope |
| `TREND_CORRECTION_GAIN`   | 0.08   | How aggressively the trend boosts preemptively |
| `SAMPLES_PER_SECOND`      | 30     | Update this if your loop runs at a different rate |

## Notes

- This library only handles **voltage** compensation. Temperature, encoder drift, and PID tuning are out of scope.
- Position servos (e.g. a claw) should not be passed through `compensateServo` — they regulate position internally and external scaling causes jitter.
- Designed for the FTC SDK's `LinearOpMode` and the standard Control Hub voltage sensor (`hardwareMap.voltageSensor`).
