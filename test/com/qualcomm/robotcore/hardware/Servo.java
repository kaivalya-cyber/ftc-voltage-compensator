package com.qualcomm.robotcore.hardware;

/**
 * Minimal stub of the FTC SDK's {@code Servo} interface, used only by
 * the local test harness in {@code test/}.
 */
public interface Servo {
    void setPosition(double position);
    void setPower(double power);
}
