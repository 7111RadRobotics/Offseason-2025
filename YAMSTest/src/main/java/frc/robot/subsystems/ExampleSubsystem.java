// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Seconds;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import yams.mechanisms.config.PivotConfig;
import yams.mechanisms.positional.Pivot;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class ExampleSubsystem extends SubsystemBase {

    private TalonFX talon = new TalonFX(15);
    private SmartMotorControllerConfig talonConfig = new SmartMotorControllerConfig(this)
        .withControlMode(ControlMode.CLOSED_LOOP)
        .withClosedLoopController(1000, 0, 0)
        .withGearing(1)
        .withIdleMode(MotorMode.BRAKE)
        .withMotorInverted(false)
        .withTelemetry(TelemetryVerbosity.HIGH)
        .withStatorCurrentLimit(Amps.of(40));
    private SmartMotorController motorController = new TalonFXWrapper(talon, DCMotor.getKrakenX60(1), talonConfig);
    private PivotConfig pivotConfig = new PivotConfig(motorController)
        .withHardLimit(Degrees.of(0), Degrees.of(37))
        .withMOI(0.0001)
        .withStartingPosition(Degrees.of(0))
        .withTelemetry("pivot", TelemetryVerbosity.HIGH);
    private Pivot pivot = new Pivot(pivotConfig);
    
    /** Creates a new ExampleSubsystem. */
    public ExampleSubsystem() {}

    /**
     * Example command factory method.
     *
     * @return a command
     */
    public Command setAngle(double degrees) {
        // Inline construction of command goes here.
        // Subsystem::RunOnce implicitly requires `this` subsystem.
        return pivot.setAngle(Degrees.of(degrees));
    }

    /**
     * An example method querying a boolean state of the subsystem (for example, a digital sensor).
     *
     * @return value of some boolean subsystem state, such as a digital sensor.
     */
    public boolean exampleCondition() {
        // Query some boolean state, such as a digital sensor.
        return true;
    }

    @Override
    public void periodic() {
        pivot.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
        pivot.simIterate();
        // This method will be called once per scheduler run during simulation
    }
}
