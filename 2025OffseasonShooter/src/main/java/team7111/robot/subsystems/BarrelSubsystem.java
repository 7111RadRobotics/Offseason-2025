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

    //Dutycycle is on a scale of -1 (full reverse) to 1 (full forward)
    private final double intakeDutycycle = 0.3;
    private final double adjustDutycycle = -0.1;
    private final double readjustDutycycle = -0.4;
    private final double shootDutycycle = 0.5;
    private final double reverseDutycycle = -0.3;
    private final double stopped = 0;

    private DigitalInput beamBreak = new DigitalInput(8);

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

    public void setManualSpeed(double dutycycle){
        barrel.set(dutycycle);
    }

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

    public boolean getBeamBreak() {
        return beamBreak.get();
    }

    private void intake() {
        // Placeholder values. Wheels active
        barrel.set(intakeDutycycle).execute();
    }

    private void adjust() {
        //  Placeholder values. Moves wheels backward slowly
        barrel.set(adjustDutycycle).execute();
    }

    private void readjust() {
        //  Placeholder values. Moves wheels forward
        barrel.set(readjustDutycycle).execute();
    }

    private void shoot() {
        // Placeholder values. Wheels will be active at full speed
        barrel.set(shootDutycycle).execute();
    }

    private void reverse() {
        // Placeholder values. Sets wheels reversing slowly
        barrel.set(reverseDutycycle).execute();
    }

    private void unloaded() {
        barrel.set(stopped).execute();
    }

    private void loaded() {
        // Game piece is stored, wheels inactive
        barrel.set(stopped).execute();
    }

    private void defaultState() {
        //  Wheels inactive
        barrel.set(stopped).execute();
    }
}
