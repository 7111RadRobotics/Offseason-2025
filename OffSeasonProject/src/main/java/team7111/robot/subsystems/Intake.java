package team7111.robot.subsystems;

import com.revrobotics.spark.SparkMax;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Feet;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SmartMotionConfig;
import com.revrobotics.spark.config.SparkBaseConfig;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.TimeUnit;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.SmartMechanism;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;
import yams.mechanisms.config.PivotConfig;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;

public class Intake extends SubsystemBase{

    private SparkMax intakeMotor = new SparkMax(1, MotorType.kBrushless);

    private String[] gear = {"1","1"};

    private GearBox gearBox = new GearBox(gear);

    private MechanismGearing gearing = new MechanismGearing(gearBox);

    private SmartMotorControllerConfig IntakeMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withSoftLimit(Degrees.of(0), Degrees.of(180))
        .withGearing(gearing);

    private SmartMotorController intakeSparkController = new SparkWrapper(intakeMotor, DCMotor.getNEO(1), IntakeMotorConfig);

    private final FlyWheelConfig intakeConfig = new FlyWheelConfig(intakeSparkController)
        .withDiameter(Inches.of(4))
        .withMass(Pounds.of(.5))
        .withUpperSoftLimit(RPM.of(1000))
        .withTelemetry("IntakeConfig", TelemetryVerbosity.HIGH);

    private FlyWheel intake = new FlyWheel(intakeConfig);

    private SparkMax pivotMotor = new SparkMax(2, MotorType.kBrushless);

    private SmartMotorControllerConfig pivotMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(4, 0, 0, DegreesPerSecond.of(180), DegreesPerSecondPerSecond.of(90))
        .withGearing(gearing(gearbox(3, 4)))
        .withIdleMode(MotorMode.BRAKE)
        .withMotorInverted(false)
        .withTelemetry("IntakePivot", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25));
    
    private SmartMotorController pivot = new SparkWrapper(pivotMotor, DCMotor.getNEO(1), pivotMotorConfig);

    private PivotConfig pivotConfig = new PivotConfig(pivot)
        .withStartingPosition(Degrees.of(0))
        .withWrapping(Degrees.of(0), Degrees.of(360))
        .withHardLimit(Degrees.of(0), Degrees.of(720))
        .withTelemetry("PivotMotor", TelemetryVerbosity.HIGH)
        .withMOI(Meters.of(0.25), Pounds.of(4));

    public Intake() {}

    public enum IntakeStates {  
        store,
        defualtState,
        deploy,
        transition,
        eject,
        manual,
    };

    private IntakeStates state = IntakeStates.defualtState;

    public void setState(IntakeStates state)
    {
        this.state = state;
    }

    private void manageState() {
        switch (state) {
            case store:
            store();
                break;
            case deploy:
            deploy();
                break;
            case transition:
            transition();
                break;
            case eject:
            eject();
                break;
            case manual:
            manual();
                break;
        }
    }

    @Override
    public void periodic() {
        manageState();
        intake.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
      intake.simIterate();
    }

    private void store() {
        intake.setSpeed(DegreesPerSecond.of(0));
    }

    private void deploy() {
        intake.setSpeed(DegreesPerSecond.of(50));
    }

    private void transition() {}

    private void eject() {
        intake.setSpeed(DegreesPerSecond.of(-50));
    }

    private void manual() {

    }
}
