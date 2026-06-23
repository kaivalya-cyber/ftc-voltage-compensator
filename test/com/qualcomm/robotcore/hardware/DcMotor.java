package com.qualcomm.robotcore.hardware;

/**
 * Minimal stub of the FTC SDK's {@code DcMotor} interface, used only by the
 * local test harness in {@code test/}.  Only the methods exercised by
 * {@code VoltageCompensatedTeleOp} are declared here.  Nested enums
 * {@link RunMode} and {@link Direction} are kept as compact subsets.
 */
public interface DcMotor {

    // RUN_WITHOUT_ENCODER is the only mode exercised by the reference
    // OpMode; the other values are kept purely for API parity with the
    // real FTC SDK and are unused by the test harness.
    enum RunMode {
        RUN_WITHOUT_ENCODER,
        RUN_USING_ENCODER,
        RUN_TO_POSITION,
        STOP_AND_RESET_ENCODER
    }

    enum Direction {
        FORWARD,
        REVERSE
    }

    void setPower(double power);
    void setMode(RunMode mode);
    void setDirection(Direction direction);
}
