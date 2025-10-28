package team7111.robot.subsystems;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import yams.mechanisms.config.ShooterConfig;
import yams.mechanisms.velocity.Shooter;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;

public class Barrel extends SubsystemBase {

    SparkMax barrelMotor = new SparkMax(1, MotorType.kBrushless);

    private BarrelStates state = BarrelStates.defaultState;

    private DigitalInput beamBreak = new DigitalInput(1);

    public boolean beamBrakeState = false;

    private SmartMotorControllerConfig barrelMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withSoftLimit(Degrees.of(0), Degrees.of(180));

    private SmartMotorController barrelSparkController = new SparkWrapper(barrelMotor, DCMotor.getNEO(1), barrelMotorConfig);

    private final ShooterConfig barrelConfig = new ShooterConfig(barrelSparkController)
        .withDiameter(Inches.of(4))
        .withMass(Pounds.of(.5))
        .withUpperSoftLimit(RPM.of(1000))
        .withTelemetry("IntakeConfig", TelemetryVerbosity.HIGH);

    private Shooter barrel = new Shooter(barrelConfig);

    public enum BarrelStates {
        intake,
        adjust,
        readjust,
        shoot,
        reverse,
        unload,
        loaded,
        defaultState
    };

    public void setState(BarrelStates state) {
        this.state = state;
    }

    private void manageState() {
        switch(state) {
            case intake:
                intakeMethod();
                break;
            case adjust:
                adjustMethod();
                break;
            case readjust:
                readjustMethod();
                break;
            case shoot:
                shootMethod();
                break;
            case reverse:
                reverseMethod();
                break;
            case unload:
                unloadMethod();
                break;
            case loaded:
                loadedMethod();
                break;
            case defaultState:
                defaultStateMethod();
                break;
        }
    }

    public void periodic() {
        this.beamBrakeState = beamBreak.get();
    }

    private void intakeMethod() {
        barrel.setSpeed(DegreesPerSecond.of(60));
    }

    private void adjustMethod() {

    }

    private void readjustMethod() {

    }

    private void shootMethod() {
        barrel.setSpeed(DegreesPerSecond.of(180));
    }

    private void reverseMethod() {
        barrel.setSpeed(DegreesPerSecond.of(-60));
    }

    private void unloadMethod() {

    }

    private void loadedMethod() {

    }

    private void defaultStateMethod() {
        barrel.setSpeed(DegreesPerSecond.of(0));
    }
}
