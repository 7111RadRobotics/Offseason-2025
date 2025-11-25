//Fix
package team7111.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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

public class BarrelSubsystem extends SubsystemBase {

    public enum BarrelState {
        intake,
        adjust,
        readjust,
        shoot,
        reverse,
        unload,
        loaded,
        defaultState
    };

    private DigitalInput beamBreak = new DigitalInput(1);

    private SparkMax barrelMotor = new SparkMax(12, MotorType.kBrushless);
    private SmartMotorControllerConfig barrelMotorConfig = new SmartMotorControllerConfig(this)
        .withClosedLoopController(new PIDController(0.1, 0, 0))
        .withGearing(new MechanismGearing(GearBox.fromReductionStages(2.5, 1)))
        .withIdleMode(MotorMode.BRAKE)
        .withTelemetry("BarrelMotor", TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withMotorInverted(false)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25))
        .withFeedforward(new SimpleMotorFeedforward(0, 0))
        .withControlMode(ControlMode.OPEN_LOOP);
    
    private SmartMotorController barrelController = new SparkWrapper(barrelMotor, DCMotor.getNEO(1), barrelMotorConfig);
    private FlyWheelConfig barrelConfig = new FlyWheelConfig(barrelController)
        .withDiameter(Inches.of(1))
        .withMass(Pounds.of(0.5))
        .withUpperSoftLimit(RPM.of(1000))
        .withTelemetry("Barrel", TelemetryVerbosity.HIGH);

    private FlyWheel barrel = new FlyWheel(barrelConfig);

    private BarrelState state = BarrelState.defaultState;

    //Constructor for class
    public BarrelSubsystem() {}

    @Override
    public void periodic() {
        manageState();
        barrel.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
        barrel.simIterate();
    }

    private void manageState() {
        switch(state) {
            case intake:
                intake();
                break;
            case adjust:
                adjust();
                break;
            case readjust:
                readjust();
                break;
            case shoot:
                shoot();
                break;
            case reverse:
                reverse();
                break;
            case unload:
                unloaded();
                break;
            case loaded:
                loaded();
                break;
            case defaultState:
                defaultState();
                break;
        }
    }

    public BarrelState getState() {
        return state;
    }

    public void setState(BarrelState state) {
        this.state = state;
    }

    public boolean getBeamBrake() {
        return beamBreak.get();
    }

    private void intake() {
        // Placeholder values. Wheels active
        barrel.setSpeed(DegreesPerSecond.of(60)).execute();;
    }

    private void adjust() {
        //  Placeholder values. Moves wheels backward slowly
        barrel.setSpeed(DegreesPerSecond.of(-40)).execute();
    }

    private void readjust() {
        //  Placeholder values. Moves wheels forward
        barrel.setSpeed(DegreesPerSecond.of(-80)).execute();
    }

    private void shoot() {
        // Placeholder values. Wheels will be active at full speed
        barrel.setSpeed(DegreesPerSecond.of(180)).execute();
    }

    private void reverse() {
        // Placeholder values. Sets wheels reversing slowly
        barrel.setSpeed(DegreesPerSecond.of(-60)).execute();
    }

    private void unloaded() {

    }

    private void loaded() {
        // Game piece is stored, wheels inactive

    }

    private void defaultState() {
        //  Wheels inactive
        barrel.setSpeed(DegreesPerSecond.of(0));
    }
}
