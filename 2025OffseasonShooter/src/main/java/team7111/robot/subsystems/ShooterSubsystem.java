//Fix
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
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

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

public class ShooterSubsystem implements Subsystem {
    
    public enum ShooterStates {
        prepareShot,
        shoot,
        reverse,
        prepareShotVision,
        defaultState,
        manual,
        idle,
    };

    public ShooterStates state = ShooterStates.defaultState;

    private double visionAngle = 0;
    private double visionSpeed = 0;

    private int mainMotorID;
    private int followerMotorID;

    private SparkMax shooterWheelsMotor = new SparkMax(mainMotorID, MotorType.kBrushless);
    private SparkMax shooterFollowerMotor = new SparkMax(followerMotorID, MotorType.kBrushless);

    private boolean pairBoolean = true;
    private Pair<Object, Boolean> followerMotorPair = new Pair<>(shooterFollowerMotor, pairBoolean);
    private SparkMaxConfig followerMotorConfig = new SparkMaxConfig();
    private static PersistMode followerMotorConfigPersistMode = PersistMode.kPersistParameters;
    private static ResetMode followerMotorConfigResetMode = ResetMode.kResetSafeParameters;

    private GearBox pivotGearBox = GearBox.fromReductionStages(112, 1);
    private MechanismGearing pivotGearing = new MechanismGearing(pivotGearBox);
    private SmartMotorControllerConfig talonConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(4, 0, 0, DegreesPerSecond.of(180), DegreesPerSecondPerSecond.of(90))
        .withIdleMode(MotorMode.BRAKE)
        .withSoftLimit(Degree.of(0), Degree.of(90))
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25))
        .withTelemetry("shooterPivotMotors", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withGearing(pivotGearing);

    private TalonFX shooterPivotMotor = new TalonFX(0);
    private SmartMotorController shooterPivot = new TalonFXWrapper(shooterPivotMotor, DCMotor.getNEO(1), talonConfig);
    
    private PivotConfig shooterPivotConfig = new PivotConfig(shooterPivot)
        .withStartingPosition(Degrees.of(0))
        .withWrapping(Degree.of(0), Degree.of(360))
        .withHardLimit(Degrees.of(0), Degrees.of(720))
        .withMOI(Meters.of(0), Pounds.of(3.953))
        .withHardLimit(Degrees.of(0), Degrees.of(30));
    private Pivot pivot = new Pivot(shooterPivotConfig);

    private GearBox wheelGearBox = GearBox.fromReductionStages(1,1);
    private MechanismGearing wheelGearing = new MechanismGearing(wheelGearBox);
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
        .withStatorCurrentLimit(Amps.of(40))
        .withGearing(wheelGearing)
        .withFollowers(followerMotorPair);

    private SmartMotorController shooterWheels = new SparkWrapper(shooterWheelsMotor, DCMotor.getNEO(1), sparkConfig);
    private FlyWheelConfig shooterConfig = new FlyWheelConfig(shooterWheels)
        .withDiameter(Inches.of(0))
        .withMass(Pounds.of(0))
        .withTelemetry("Shooter", TelemetryVerbosity.HIGH)
        .withUpperSoftLimit(RPM.of(1000))
        .withDiameter(Inches.of(4));

    private FlyWheel shooter = new FlyWheel(shooterConfig);

    public ShooterSubsystem(int mainMotorID, int followerMotorID) {
        this.mainMotorID = mainMotorID;
        this.followerMotorID = followerMotorID;

        configureFollowerMotor();
    }

    /**
     * Sets motor positions based off of state.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
        manageState();
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

    public void setState(ShooterStates state) {
        this.state = state;
    }

    public ShooterStates getState() {
        return state;
    }

    /**
     * Sets the shooter angle from minimum to maximum shooter angle.
     * Minimum angle is 
     */
    public void setAngle(double angle){
        
    }

    private void prepareShot() {
        // placeholder values. pivot will be extended and wheels will rev-up
        shooter.setSpeed(RPM.of(160)).execute();
        pivot.setAngle(Degrees.of(90)).execute();;
    }

    private void shoot() {
        // placeholder values. wheels will be shooting
        shooter.setSpeed(RPM.of(160)).execute();
    }

    private void reverse() {
        // placeholder values. wheels will be reversed
        shooter.setSpeed(RPM.of(-80)).execute();
    }

    private void prepareShotVision() {
        // set shooterPivot to visionAngle and shooter to visionSpeed
        pivot.setAngle(Degrees.of(visionAngle)).execute();
        shooter.setSpeed(RPM.of(visionSpeed)).execute();
    }

    private void defaultState() {}

    private void configureFollowerMotor() {
        // configure followerMotorConfig to be a follower and inverted
        followerMotorConfig.inverted(true);
        shooterFollowerMotor.configure(followerMotorConfig, followerMotorConfigResetMode, followerMotorConfigPersistMode);
    } 

    private void manual() {

    }

    private void idle() {

    }
}
