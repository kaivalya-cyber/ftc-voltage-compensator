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

    public HardwareMap hardwareMap = new HardwareMap();
    public Gamepad     gamepad1     = new Gamepad();
    public Gamepad     gamepad2     = new Gamepad();
    public Telemetry   telemetry;

    public boolean opModeIsActive() { return false; }
}
