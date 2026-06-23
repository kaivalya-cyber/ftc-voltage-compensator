package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Minimal stub of the FTC SDK's {@code OpMode} base class, used only by the
 * local test harness in {@code test/}.  Only the public members touched by
 * {@code VoltageCompensatedTeleOp} are declared.
 */
public abstract class OpMode {

    // Compile-only stub: the real FTC SDK wires these before runOpMode()
    // executes.  The test harness MUST NOT instantiate this class for
    // runtime use \u2014 these declarations exist solely so the production
    // .java files compile against the test/ classpath.

    public HardwareMap hardwareMap = new HardwareMap();
    public Gamepad     gamepad1     = new Gamepad();
    public Gamepad     gamepad2     = new Gamepad();
    public Telemetry   telemetry;   // deliberately null in the stub

    public boolean opModeIsActive() { return false; }
}
