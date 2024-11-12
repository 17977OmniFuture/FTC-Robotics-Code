package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**Configuration File
 * controlHub:
 * frontRightMotor port: 00
 * frontLeftMotor port: 01
 * backRightMotor port: 02
 * backLeftMotor port: 03
 * expansionHub:
 * armMotor port:00
 */

@TeleOp
public class driverControl extends LinearOpMode {
    // this section is for drivetrain variables
    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private double motorZeroPower = 0.0;
    private double drivetrainSensitivity;

    // this section is for arm motor variables
    private DcMotor armMotor;
    private int armMaxPosition = 1000;
    private int armArmMinPosition = 0;
    private double armMotorPower = 1.0;
    private double armMotorSensitivity = 1; // dont really need this since its not controled
    private int armMotorPositionOne = 0; //a
    private int armMotorPositionTwo = 1250;  //x
    private int armMotorPositionThree = 3200;  //b



    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();
        driveTrainTelemetry();
        armMotorTelemetry();


        while (!isStarted()) {
            armMotorTelemetry();
        }
        waitForStart();

        while (opModeIsActive()) {
            armMotorTelemetry();
            armMotorTelemetry();
            driveTrainControls();
            armMotorControls();

        }
    }

    public void initHardware() {
        initDrivetrain();
        initArmMotor();

     }

    public void initArmMotor() {                                                          // this is init for run to a position may need to comment
        armMotor = hardwareMap.dcMotor.get("armMotor");
        armMotor.setDirection(DcMotor.Direction.REVERSE);                         // out and add one for run with limits
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setPower(motorZeroPower);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    public void initDrivetrain() {
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRightMotor.setPower(motorZeroPower);
        frontLeftMotor.setPower(motorZeroPower);
        backRightMotor.setPower(motorZeroPower);
        backLeftMotor.setPower(motorZeroPower);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

    }

    public void driveTrainControls() {

        if (gamepad1.right_bumper) {
            drivetrainSensitivity = .75;
        } else {
            drivetrainSensitivity = .4;
        }

        double y = -gamepad1.right_stick_y;
        double x = gamepad1.right_stick_x;
        double rx = gamepad1.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower * drivetrainSensitivity);
        backLeftMotor.setPower(backLeftPower * drivetrainSensitivity);
        frontRightMotor.setPower(frontRightPower * drivetrainSensitivity);
        backRightMotor.setPower(backRightPower * drivetrainSensitivity);
    }
    public void runArmMotorToPosition(int position){

        armMotor.setTargetPosition(position);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(armMotorPower * armMotorSensitivity);
        }

    public void armMotorControls() {

        if (gamepad1.a) {
            runArmMotorToPosition(armMotorPositionOne);
        }
        if (gamepad1.x) {
            runArmMotorToPosition(armMotorPositionTwo);
        }
        if (gamepad1.b) {
            runArmMotorToPosition(armMotorPositionThree);
        }
    }
    public void armMotorTelemetry() {
        telemetry.addData("armMotor",armMotor.getCurrentPosition());
        //telemetry.addData("Right stick y",gamepad2.left_stick_y);
        telemetry.update();
    }
    public void driveTrainTelemetry() {
        telemetry.addData("frontRightMotor", frontRightMotor.getCurrentPosition());
        telemetry.addData("frontLeftMotor", frontLeftMotor.getCurrentPosition());
        telemetry.addData("backRightMotor", backRightMotor.getCurrentPosition());
        telemetry.addData("backRightMotor", backLeftMotor.getCurrentPosition());
        telemetry.update();

        if (gamepad1.right_bumper) {
            telemetry.addData("Turbo Mode Active", gamepad1.right_bumper);
            telemetry.update();
        } else {
            telemetry.addData("Turbo Mode Off", !gamepad1.right_bumper);
            telemetry.update();
        }


    }
}

