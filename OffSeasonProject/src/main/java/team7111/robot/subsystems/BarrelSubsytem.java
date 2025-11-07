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
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;

public class BarrelSubsytem extends SubsystemBase {

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

    private DigitalInput beamBreak = new DigitalInput(1);
    public boolean beamBrakeState = false;
    public boolean getBeamBrake() {
        return beamBreak.get();
    }
    
    private SparkMax barrelMotor = new SparkMax(1, MotorType.kBrushless);
    private String[] barrelGear = {"1","1"};
    private GearBox barrelGearBox = new GearBox(barrelGear);
    private MechanismGearing barrelGearing = new MechanismGearing(barrelGearBox);
    private SmartMotorControllerConfig barrelMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withSoftLimit(Degrees.of(0), Degrees.of(180))
        .withGearing(barrelGearing);
    
    private SmartMotorController barrelSparkController = new SparkWrapper(barrelMotor, DCMotor.getNEO(1), barrelMotorConfig);
    private final FlyWheelConfig barrelConfig = new FlyWheelConfig(barrelSparkController)
        .withDiameter(Inches.of(4))
        .withMass(Pounds.of(.5))
        .withUpperSoftLimit(RPM.of(1000))
        .withTelemetry("IntakeConfig", TelemetryVerbosity.HIGH);

    private FlyWheel barrel = new FlyWheel(barrelConfig);

    
    private BarrelStates state = BarrelStates.defaultState;

    public void setState(BarrelStates state) {
        this.state = state;
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
                unload();
                break;
            case loaded:
                loaded();
                break;
            case defaultState:
                defaultState();
                break;
        }
    }

    @Override
    public void periodic() {
        manageState();
        barrel.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
        barrel.simIterate();
    }

    private void intake() {
        // Placeholder values. Wheels active
        barrel.setSpeed(DegreesPerSecond.of(60));
    }

    private void adjust() {
        //  Placeholder values. Moves wheels backward slowly
        barrel.setSpeed(DegreesPerSecond.of(-40));
    }

    private void readjust() {
        //  Placeholder values. Moves wheels forward
        barrel.setSpeed(DegreesPerSecond.of(-80));
    }

    private void shoot() {
        // Placeholder values. Wheels will be active at full speed
        barrel.setSpeed(DegreesPerSecond.of(180));
    }

    private void reverse() {
        // Placeholder values. Sets wheels reversing slowly
        barrel.setSpeed(DegreesPerSecond.of(-60));
    }

    private void unload() {
        //  unloads the game piece, wheels active at low speeds

    }

    private void loaded() {
        // Game piece is stored, wheels inactive

    }

    private void defaultState() {
        //  Wheels inactive
        barrel.setSpeed(DegreesPerSecond.of(0));
    }
}
