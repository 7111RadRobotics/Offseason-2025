package team7111.robot.subsystems;

import com.revrobotics.spark.SparkMax;

public class Intake {
 
    private SparkMax intakePivot;

    private SparkMax intakeWheels;

    private intakeStates state = intakeStates.defualtState;

    public enum intakeStates {  
        store,
        intake,
        transition,
        defualtState,
    };

    public void setState(intakeStates state)
    {
        this.state = state;
    }
}
