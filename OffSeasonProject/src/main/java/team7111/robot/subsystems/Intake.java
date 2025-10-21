package team7111.robot.subsystems;

import com.revrobotics.spark.SparkMax;

public class Intake {

    /**
     * Constructor for intake class. Assigns intake motors.
     */
    public Intake(SparkMax intakePivot, SparkMax intakeWheels) {
        this.intakePivot = intakePivot;
        this.intakeWheels = intakeWheels;
    }

    //Motors
    private SparkMax intakePivot;
    private SparkMax intakeWheels;

    private intakeStates state = intakeStates.defualtState;

    public enum intakeStates {  
        store,
        intake,
        transition,
        defualtState,
        manual,
    };

    public void setState(intakeStates state)
    {
        this.state = state;
    }

    /**
     * Manages motors controlling the intake mechanism on the robot.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
    }
}
