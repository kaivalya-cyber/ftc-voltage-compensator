package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.util.VoltageCompensator;

@TeleOp(name = "VoltageCompensated TeleOp")
public class VoltageCompensatedTeleOp extends LinearOpMode {

    // ── Hardware declarations ─────────────────────────────────────────────
    private DcMotor leftFront, rightFront, leftBack, rightBack;
    private DcMotor armMotor;
    private Servo clawServo;      // NOT compensated (position servo)
    private Servo intakeServo;    // continuous rotation — IS compensated

    private VoltageCompensator compensator;
    private VoltageSensor batterySensor;

    @Override
    public void runOpMode() {
        // ── Hardware map ──────────────────────────────────────────────────
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");
        armMotor   = hardwareMap.get(DcMotor.class, "armMotor");
        clawServo  = hardwareMap.get(Servo.class, "clawServo");
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");

        // Configure drive motors (RUN_WITHOUT_ENCODER for direct teleop)
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Reverse one side for tank-drive convention
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Arm motor — also direct power control, not PID position mode
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // ── Voltage sensor ────────────────────────────────────────────────
        // The Control Hub exposes one voltage sensor; grab the first one.
        batterySensor = hardwareMap.voltageSensor.iterator().next();

        // ── Compensator ───────────────────────────────────────────────────
        // applyDriveCurve = true — enables cubic shaping for better feel
        compensator = new VoltageCompensator(true);

        telemetry.addData("Status", "Initialized — waiting for start");
        telemetry.update();

        // ── Wait for driver to press play ─────────────────────────────────
        waitForStart();

        while (opModeIsActive()) {
            // 1. Update compensator BEFORE reading any gamepad inputs
            //    so compensation factor is ready for this loop iteration.
            compensator.update(batterySensor, telemetry);

            // ──────────────────────────────────────────────────────────────
            // 2. Drivetrain — use compensateDrive for the cubic + voltage
            // ──────────────────────────────────────────────────────────────
            double drive   = -gamepad1.left_stick_y;   // forward/back
            double strafe  =  gamepad1.left_stick_x;   // left/right
            double rotate  =  gamepad1.right_stick_x;  // rotation

            // Mecanum drive kinematics (standard FTC convention)
            double lf = drive + strafe + rotate;
            double rf = drive - strafe - rotate;
            double lb = drive - strafe + rotate;
            double rb = drive + strafe - rotate;

            leftFront.setPower(compensator.compensateDrive(lf));
            rightFront.setPower(compensator.compensateDrive(rf));
            leftBack.setPower(compensator.compensateDrive(lb));
            rightBack.setPower(compensator.compensateDrive(rb));

            // ──────────────────────────────────────────────────────────────
            // 3. Arm motor — use compensateMotor (no cubic shaping)
            // ──────────────────────────────────────────────────────────────
            double armPower = -gamepad2.left_stick_y;
            armMotor.setPower(compensator.compensateMotor(armPower));

            // ──────────────────────────────────────────────────────────────
            // 4. Servos
            // ──────────────────────────────────────────────────────────────
            // Position servo — do NOT compensate (internal PID handles it)
            if (gamepad2.a) {
                clawServo.setPosition(0.0); // open
            } else if (gamepad2.b) {
                clawServo.setPosition(0.5); // closed
            }

            // Continuous-rotation servo — compensate with narrower cap
            double intakePower = gamepad2.right_trigger - gamepad2.left_trigger;
            intakeServo.setPower(compensator.compensateServo(intakePower));

            // ──────────────────────────────────────────────────────────────
            // 5. Telemetry
            // ──────────────────────────────────────────────────────────────
            telemetry.addData("Voltage (raw)",    "%.2f V", batterySensor.getVoltage());
            telemetry.addData("Voltage (smoothed)","%.2f V", compensator.getVoltage());
            telemetry.addData("Trend",            "%.2f V/sec", compensator.getTrend());
            telemetry.addData("Comp factor",      "%.2fx", compensator.getCompensationFactor());
            telemetry.addData("Brownout",         compensator.isBrownoutProtected() ? "YES" : "NO");
            telemetry.update();
        }
    }
}
