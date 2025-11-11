package team7111.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;
import yams.mechanisms.config.PivotConfig;
import yams.mechanisms.positional.Pivot;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;

public class IntakeSubsystem extends SubsystemBase{
    // enums defined above variables
    // naming should be name of subsystem (without "Subsystem") followed by "State"
    public enum IntakeState {  
        store,
        defualtState,
        deploy,
        transition,
        eject,
        manual,
    };

    // variables defined above methods

    // variables relating to intake flywheels
    private SparkMax flywheelMotor = new SparkMax(1, MotorType.kBrushless);
    private MechanismGearing flywheelGearing = new MechanismGearing(GearBox.fromReductionStages(1.2, 1));
    private SmartMotorControllerConfig flywheelMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.OPEN_LOOP)
        .withGearing(flywheelGearing)
        .withIdleMode(MotorMode.COAST)
        .withMotorInverted(false)
        .withTelemetry(TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40))
        .withOpenLoopRampRate(Seconds.of(0.25));

    private SmartMotorController flywheelController = new SparkWrapper(flywheelMotor, DCMotor.getNEO(1), flywheelMotorConfig);
    private FlyWheelConfig intakeConfig = new FlyWheelConfig(flywheelController)
        .withDiameter(Inches.of(2.25))
        .withMass(Pounds.of(0.5))
        .withUpperSoftLimit(RPM.of(4000))
        .withTelemetry("IntakeFlywheels", TelemetryVerbosity.HIGH);

    private FlyWheel flywheels = new FlyWheel(intakeConfig);

    // variables relating to intake pivot
    private SparkMax pivotMotor = new SparkMax(2, MotorType.kBrushless);
    private MechanismGearing pivotGearing = new MechanismGearing(GearBox.fromReductionStages(18, 1));
    private SmartMotorControllerConfig pivotMotorConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(4, 0, 0)
        .withGearing(pivotGearing)
        .withIdleMode(MotorMode.BRAKE)
        .withMotorInverted(false)
        .withStatorCurrentLimit(Amps.of(40))
        .withClosedLoopRampRate(Seconds.of(0.25))
        .withOpenLoopRampRate(Seconds.of(0.25));
    
    private SmartMotorController pivotController = new SparkWrapper(pivotMotor, DCMotor.getNEO(1), pivotMotorConfig);
    private PivotConfig pivotConfig = new PivotConfig(pivotController)
        .withStartingPosition(Degrees.of(0))
        .withHardLimit(Degrees.of(0), Degrees.of(48))
        .withTelemetry("IntakePivot", TelemetryVerbosity.HIGH)
        .withMOI(Inches.of(8.876), Pounds.of(7.015));

    private Pivot pivot = new Pivot(pivotConfig);

    // other variables
    private IntakeState state = IntakeState.defualtState;

    private double currentPivotSetpoint = 0;
    private double currentFlywheelSpeed = 0;

    // methods defined at bottom
    // class constructor above all other methods
    public IntakeSubsystem() {}

    // periodic and simulationPeriodic just below constructor
    @Override
    public void periodic() {
        manageState();
        if(pivot.getMechanismSetpoint().isPresent())
            currentPivotSetpoint = pivot.getMechanismSetpoint().get().in(Degrees);
        currentFlywheelSpeed = flywheels.getSpeed().in(RPM);
        flywheels.updateTelemetry();
        pivot.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
        flywheels.simIterate();
        pivot.simIterate();
    }

    // all subsytems that contain a state enum must contain a getState, setState, manageState, and methods for each state
    public void setState(IntakeState state){
        this.state = state;
    }

    public IntakeState getState(){
        return state;
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
            case defualtState:
            default:
                break;
        }
    }

    private void store() {
        // pivot will be inside and wheels stopped
        flywheels.setSpeed(RPM.of(0)).execute(); // execute must be called if the method is a command
        pivot.setAngle(Degrees.of(0)).execute();
    }

    private void deploy() {
        // placeholder values. pivot will be extended and wheels intaking
        flywheels.setSpeed(RPM.of(1000)).execute();
        pivot.setAngle(Degrees.of(48)).execute();
    }

    private void transition() {
        // placeholder values. pivot will be inside and wheels intaking
        flywheels.setSpeed(RPM.of(1000)).execute();
        pivot.setAngle(Degrees.of(0)).execute();
    }

    private void eject() {
        // placeholder values. pivot will be extended and wheels reversing
        flywheels.setSpeed(RPM.of(-1000)).execute();
        pivot.setAngle(Degrees.of(48)).execute();
    }

    private void manual() {
        
    }

    // other methods at the bottom

    public void setManual(double pivotIncrement, double flywheelSpeed){
        state = IntakeState.manual;
        currentFlywheelSpeed = flywheelSpeed;
        currentPivotSetpoint += pivotIncrement;
        flywheels.set(flywheelSpeed).execute();
        pivot.setAngle(Degrees.of(currentPivotSetpoint)).execute();
    }
}
