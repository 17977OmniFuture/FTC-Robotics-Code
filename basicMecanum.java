
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**Configuration File
 * controlHub:
 * frontRightMotor port: 00
 * frontLeftMotor port: 01
 * backRightMotor port: 02
 * backLeftMotor port: 03
 * expansionHub
 */

@TeleOp
public class basicMecanum extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private double motorZeroPower =0.0;         // defines zero power for all motors initialization so we don't have to set the power to zero on each motor
    private double drivetrainSensitivity;    // use this to multiply by the motor power to adjust the speed on the drivetrain


    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();
        driveTrainTelemetry();

        while(!isStarted()){
        }

        waitForStart();

        while(opModeIsActive()){
            driveTrainControls();
            driveTrainTelemetry();

        }

    }
    public void initHardware(){
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");              // hardware maps
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");                // make sure these match what is in the driver hub exactly
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        backLeftMotor= hardwareMap.dcMotor.get("backLeftMotor");

        frontRightMotor .setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);             // set run mode
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);             // this stops the motors and resets the encoders to zero if you skip this the encoder data will carry over from last time you ran the program
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);              // if you use STOP AND RESET ENCODERS you have to change the RunMode to RUN right after or the motors will stay stopped

        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);             // set run mode
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);             // mecanum drive did not like run with encoder, switched to run without encoder which I think still allows for telemetry
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRightMotor.setPower(motorZeroPower);                                // set the motors to zero power so they do not move on initialization
        frontLeftMotor.setPower(motorZeroPower);
        backRightMotor.setPower(motorZeroPower);
        backLeftMotor.setPower(motorZeroPower);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // brake makes the motor stop when there is not power to it, FLOAT would coast to a stop which is not good for drivetrains
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);                // set direction of the motors
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

    }
    public void driveTrainControls(){

        if (gamepad1.right_bumper){
            drivetrainSensitivity=1;                             // TURBO MODE!!!!! lets goooooo!
        }
        else {drivetrainSensitivity=.75;                         // the driveTrainSensitivity is used to make the motors faster or slower
        }

        double y = -gamepad1.right_stick_y;
        double x = gamepad1.right_stick_x;
        double rx = gamepad1.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1 );     // these lines set the drive train motor powers to the output of the joysticks
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower*drivetrainSensitivity);              // these lines take the motor powers that are taken from the joy sticks and multiplies them to slow them down or speed them up
        backLeftMotor.setPower(backLeftPower*drivetrainSensitivity);
        frontRightMotor.setPower(frontRightPower*drivetrainSensitivity);
        backRightMotor.setPower(backRightPower*drivetrainSensitivity);

    }
    public void driveTrainTelemetry(){
        telemetry.addData("frontRightMotor",frontRightMotor.getCurrentPosition());         // this is telemetry for the drive train motors, can use this information for writing autonomous
        telemetry.addData("frontLeftMotor",frontLeftMotor.getCurrentPosition());
        telemetry.addData("backRightMotor",backRightMotor.getCurrentPosition());
        telemetry.addData("backRightMotor",backLeftMotor.getCurrentPosition());


        if (gamepad1.right_bumper){                                                  // these lines are just for the turbo mode telemetry
            telemetry.addData("Turbo Mode Active", gamepad1.right_bumper);
            telemetry.update();
        }
        else {
            telemetry.addData("Turbo Mode Off", !gamepad1.right_bumper);
            telemetry.update();
        }

    }
}