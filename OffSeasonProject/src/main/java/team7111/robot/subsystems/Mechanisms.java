package team7111.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

public class Mechanisms {
    
    private SparkMax intakePivot;

    private SparkMax intakeWheels;
    private SparkMax indexWheels;

    private TalonFX shooterPivot;
    
    //private SparkMax[] shooterWheels;

    private SparkMax shooterWheel1;
    private SparkMax shooterWheel2;

    mechanismStates state = mechanismStates.manual;

    private enum mechanismStates {
        intake,
        shootClose,
        shootVision,
        prepShot,
        manual,
    };


    public Mechanisms() {}

    public void setMechState(mechanismStates state)
    {
        this.state = state;
    }
}
