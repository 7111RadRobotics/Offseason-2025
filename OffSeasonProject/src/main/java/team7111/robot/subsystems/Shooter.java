package team7111.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

public class Shooter {
    

    private SparkMax indexWheels;

    private TalonFX shooterPivot;

    private SparkMax shooterWheel1;
    private SparkMax shooterWheel2;

    //private SparkMax[] shooterWheels;

    shooterStates state = shooterStates.defaultState;

    public enum shooterStates {
        intake,
        loaded,
        prepareShot,
        shoot,
        overshot,
        unloaded,
        defaultState,
    };


    public void setState(shooterStates state) {
        this.state = state;
    }
}
