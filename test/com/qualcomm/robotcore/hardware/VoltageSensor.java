package com.qualcomm.robotcore.hardware;

/**
 * Minimal stub of the FTC SDK's {@code VoltageSensor} interface, used only by
 * the local test harness in {@code test/}.  The real FTC SDK provides a richer
 * implementation; this stub exists so the test suite can compile and run
 * without dragging in the whole SDK / Gradle build.
 */
public interface VoltageSensor {
    /** Returns the current battery voltage in volts. */
    double getVoltage();
}
