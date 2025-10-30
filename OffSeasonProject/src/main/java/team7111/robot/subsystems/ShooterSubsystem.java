package team7111.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degree;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Seconds;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import yams.mechanisms.config.PivotConfig;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class ShooterSubsystem implements Subsystem {
    
    public ShooterSubsystem() {}

    //Potentially use array for shooter wheels
    //private SparkMax[] shooterWheels;

    ShooterStates state = ShooterStates.defaultState;

    private double visionAngle = 0;

    private double visionSpeed = 0;

    private TalonFX shooterPivotMotor = new TalonFX(0);

    private SparkMax shooterWheelsMotor = new SparkMax(0, MotorType.kBrushless);

    private SmartMotorControllerConfig talonConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(4, 0, 0, DegreesPerSecond.of(180), DegreesPerSecondPerSecond.of(90))
        .withIdleMode(MotorMode.BRAKE)
        .withSoftLimit(Degree.of(0), Degree.of(90))
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25))
        .withTelemetry("shooterPivotMotors", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40));

    private SmartMotorControllerConfig sparkConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(4, 0, 0, DegreesPerSecond.of(180), DegreesPerSecondPerSecond.of(90))
        .withIdleMode(MotorMode.BRAKE)
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25))
        .withFeedforward(new SimpleMotorFeedforward(0, 0, 0))
        .withSimFeedforward(new SimpleMotorFeedforward(0, 0, 0))
        .withTelemetry("shooterWheelsMotor", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40));

    private SmartMotorController shooterPivot = new TalonFXWrapper(shooterPivotMotor, DCMotor.getNEO(1), talonConfig);

    private SmartMotorController shooterWheels = new SparkWrapper(shooterWheelsMotor, DCMotor.getNEO(1), sparkConfig);

    private PivotConfig shooterPivotConfig = new PivotConfig(shooterPivot)
        .withStartingPosition(Degrees.of(0))
        .withWrapping(Degree.of(0), Degree.of(360))
        .withHardLimit(Degrees.of(0), Degrees.of(720))
        .withMOI(Meters.of(0), Pounds.of(0));

    private FlyWheelConfig flywheelConfig = new FlyWheelConfig(shooterWheels)
        .withDiameter(Inches.of(0))
        .withMass(Pounds.of(0))
        .withTelemetry("Shooter", TelemetryVerbosity.HIGH)
        .withUpperSoftLimit(RPM.of(1000));

    private FlyWheel shooter = new FlyWheel(flywheelConfig);

    public enum ShooterStates {
        prepareShot,
        shoot,
        reverse,
        prepareShotVision,
        defaultState,
        manual,
        idle,
    };

    public void setState(ShooterStates state) {
        this.state = state;
    }

    /**
     * Sets the shooter angle from minimum to maximum shooter angle.
     * Minimum angle is 
     */
    public void setAngle(double angle)
    {
        
    }

    private void manageState() {
        switch (state) {
            case prepareShot:
            prepareShot();
                break;
            case shoot:
            shoot();
                break;
            case reverse:
            reverse();
                break;
            case prepareShotVision:
            prepareShotVision();
                break;
            case manual:
            manual();
                break;
            case idle:
            idle();
                break;
        }
    }

    /**
     * Sets motor positions based off of state.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
        manageState();
    }

    private void prepareShot() {
        shooter.setSpeed(RPM.of(160)).execute();
        shooterPivot.setPosition(Degrees.of(90));
    }

    private void shoot() {
        shooter.setSpeed(RPM.of(160)).execute();
    }

    private void reverse() {
        shooter.setSpeed(RPM.of(-80)).execute();
    }

    private void prepareShotVision() {
        shooterPivot.setPosition(Degrees.of(visionAngle));
        shooter.setSpeed(RPM.of(visionSpeed));
    }

    private void manual() {

    }

    private void idle() {

    }
}
