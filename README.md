# FTC Voltage Compensator

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build & Test](https://github.com/kaivalya-cyber/ftc-voltage-compensator/actions/workflows/build.yml/badge.svg)](../../actions/workflows/build.yml)
[![Java 11+](https://img.shields.io/badge/Java-11%2B-blue.svg)]()

## Documentation

- **[ARCHITECTURE.md](ARCHITECTURE.md)** — state-machine diagrams, per-loop sequence, constants rationale, tuning recipe
- **[CHANGELOG.md](CHANGELOG.md)** — release history in Keep-a-Changelog format
- **[SECURITY.md](SECURITY.md)** — supported versions, vulnerability reporting, hardening guarantees
- **[CONTRIBUTING.md](CONTRIBUTING.md)** — how to add stubs, coding style, PR process

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

## Tests

A standalone unit-test suite lives under [`test/`](test/). It uses tiny stub interfaces that mirror the FTC SDK `VoltageSensor`, `Range`, and `Telemetry` types, so the whole suite compiles and runs with nothing more than a JDK — no Gradle, no FTC SDK download.

Run locally:

```sh
./runtests.sh
```

Or step-by-step (any JDK 11+):

```sh
mkdir -p build && javac -d build \
  VoltageCompensator.java \
  test/com/qualcomm/robotcore/hardware/VoltageSensor.java \
  test/com/qualcomm/robotcore/util/Range.java \
  test/com/qualcomm/robotcore/external/Telemetry.java \
  test/org/firstinspires/ftc/teamcode/util/VoltageCompensatorTest.java

java -ea -cp build org.firstinspires.ftc.teamcode.util.VoltageCompensatorTest
```

CI runs the same flow on every push and pull request — see [`.github/workflows/build.yml`](.github/workflows/build.yml).

## Build

The lightweight Gradle-free path is the primary way to build and test:

```sh
./runtests.sh
```

Two small Gradle files (`build.gradle`, `settings.gradle`) are also included for teams that prefer Gradle.  They are purely additive — `./runtests.sh` and CI keep working without them.

```sh
gradle classes       # compile only
gradle runUnitTests  # compile + run unit tests
```

(Requires a system-installed Gradle 7.0+; no `gradlew` wrapper is bundled.)

### Wiring into a real FTC Android Studio project

To use these files in your team's robot project, copy:

- `VoltageCompensator.java`     → `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/util/`
- `VoltageCompensatedTeleOp.java` → `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`

Both files already declare the correct FTC SDK packages, so they drop straight in.  Just remove (or ignore) `build.gradle`, `settings.gradle`, and `runtests.sh` from the copy.  Deploy from Android Studio to the Robot Controller phone (or use the FTC SDK's gradle tasks) to actually run the OpMode on a Control Hub.

## License

[MIT](LICENSE).
