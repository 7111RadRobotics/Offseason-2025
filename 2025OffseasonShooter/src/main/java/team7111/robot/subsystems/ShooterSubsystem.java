//Fix
package team7111.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degree;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Seconds;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team7111.robot.utils.encoder.GenericEncoder;
import team7111.robot.utils.encoder.ThroughBore;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import yams.mechanisms.config.PivotConfig;
import yams.mechanisms.positional.Pivot;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class ShooterSubsystem extends SubsystemBase {
    
    public enum ShooterState {
        prepareShot,
        shoot,
        reverse,
        prepareShotVision,
        defaultState,
        manual,
        idle,
    };

    private ShooterState state = ShooterState.defaultState;

    private double visionAngle = 0;
    private double visionSpeed = 0;
    private double previousAngle = 0;
    private double manualPivotSetpoint = 0;

    private SparkMax flywheelMotor = new SparkMax(10, MotorType.kBrushless);
    private SparkMax flywheelFollowerMotor = new SparkMax(12, MotorType.kBrushless);

    private GenericEncoder pivotEncoder = new ThroughBore(0, 1, 1/(8192*17.5) * 4);

    private SmartMotorControllerConfig pivotControllerConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(30, 0.0, 0)
        .withIdleMode(MotorMode.COAST)
        //.withSoftLimit(Degree.of(0), Degree.of(90))
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withTelemetry("shooterPivotMotor", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withGearing(new MechanismGearing(GearBox.fromReductionStages(48/12, 24/15, 210/12)))
        //.withExternalEncoder(new Encoder(4, 5))
        //.withExternalEncoderGearing(210/12)
        //.withExternalEncoderInverted(false)
        //.withExternalEncoderZeroOffset(Degrees.of(0))
        ;

    private TalonFX pivotMotor = new TalonFX(15);
    private SmartMotorController pivotController = new TalonFXWrapper(pivotMotor, DCMotor.getKrakenX60(1), pivotControllerConfig);
    
    private PivotConfig pivotConfig = new PivotConfig(pivotController)
        .withStartingPosition(Degrees.of(35))
        .withMOI(Inches.of(15.5), Pounds.of(3.953))
        .withHardLimit(Degrees.of(35), Degrees.of(65))
        .withTelemetry("ShooterPivot", TelemetryVerbosity.HIGH);

    private Pivot pivot = new Pivot(pivotConfig);

    private SmartMotorControllerConfig flywheelControllerConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(1, 0, 0)
        .withIdleMode(MotorMode.BRAKE)
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25))
        .withFeedforward(new SimpleMotorFeedforward(0, 0, 0))
        .withSimFeedforward(new SimpleMotorFeedforward(0, 0, 0))
        .withTelemetry("shooterWheelsMotor", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withGearing(new MechanismGearing(GearBox.fromReductionStages(1,1)))
        .withFollowers(new Pair<>(flywheelFollowerMotor, true));

    private SmartMotorController flywheelController = new SparkWrapper(flywheelMotor, DCMotor.getNEO(2), flywheelControllerConfig);

    private FlyWheelConfig flywheelConfig = new FlyWheelConfig(flywheelController)
        .withDiameter(Inches.of(2))
        .withMass(Pounds.of(1))
        .withTelemetry("ShooterFlywheels", TelemetryVerbosity.HIGH)
        .withUpperSoftLimit(RPM.of(1000))
        .withDiameter(Inches.of(4));

    private FlyWheel shooter = new FlyWheel(flywheelConfig);

    public ShooterSubsystem() {
        
    }

    /**
     * Sets motor positions based off of state.
     * <p>
     * 
     */
    public void periodic() {
        if(RobotBase.isReal()) {
            pivotController.setEncoderPosition(Degrees.of(pivotEncoder.getPosition().getDegrees() + 35));
        }
        pivot.updateTelemetry();
        shooter.updateTelemetry();
        manageState();

        SmartDashboard.putNumber("throughbore position", pivotEncoder.getPosition().getDegrees());
        SmartDashboard.putNumber("pivot position", pivot.getAngle().in(Degrees));
    }

    public void simulationPeriodic(){
        pivot.simIterate();
        shooter.simIterate();
    }

    public void setManualSpeed(double speed){
        shooter.setSpeed(RPM.of(speed)).execute();
    }

    public void addManualAngle(double angleIncrement){
        manualPivotSetpoint += angleIncrement;
        if(manualPivotSetpoint > 65)
            manualPivotSetpoint = 65;
        if(manualPivotSetpoint < 35)
            manualPivotSetpoint = 35;
        pivot.setAngle(Degrees.of(manualPivotSetpoint)).execute();
    }

    /**
     * Sets the shooter angle from minimum to maximum shooter angle.
     * Minimum angle is 
     */
    public void setAngle(double angle){
        visionAngle = angle;
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
            case defaultState:
                defaultState();
                break;
        }
    }

    public void setState(ShooterState state) {
        this.state = state;
    }

    public ShooterState getState() {
        return state;
    }

    private void idle() {
        shooter.setSpeed(RPM.of(100)).execute();
        pivot.setAngle(Degrees.of(35)).execute();
        manualPivotSetpoint = 0;
    }

    private void prepareShot() {
        // placeholder values. pivot will be extended and wheels will rev-up
        shooter.setSpeed(RPM.of(300)).execute();
        pivot.setAngle(Degrees.of(65)).execute();
        previousAngle = 65;
        manualPivotSetpoint = 30;
    }

    private void shoot() {
        // placeholder values. wheels will be shooting
        shooter.setSpeed(RPM.of(300)).execute();
        pivot.setAngle(Degrees.of(previousAngle)).execute();
        manualPivotSetpoint = previousAngle;
    }

    private void reverse() {
        // placeholder values. wheels will be reversed
        shooter.setSpeed(RPM.of(-80)).execute();
        pivot.setAngle(Degrees.of(65)).execute();
        manualPivotSetpoint = 65;
    }

    private void prepareShotVision() {
        // set shooterPivot to visionAngle and shooter to visionSpeed
        pivot.setAngle(Degrees.of(visionAngle)).execute();
        shooter.setSpeed(RPM.of(visionSpeed)).execute();
        previousAngle = visionAngle;
        manualPivotSetpoint = previousAngle;
    }

    private void defaultState() {} 

    private void manual() {}
}
