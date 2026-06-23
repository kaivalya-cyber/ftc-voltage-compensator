package com.qualcomm.robotcore.eventloop.opmode;

/**
 * Minimal stub of the FTC SDK's {@code LinearOpMode} abstract class, used
 * only by the local test harness in {@code test/}.
 */
public abstract class LinearOpMode extends OpMode {

    public abstract void runOpMode();

    public void waitForStart() { /* real SDK blocks here until "Play" */ }
}
