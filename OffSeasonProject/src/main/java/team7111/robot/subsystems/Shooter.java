package team7111.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

public class Shooter {
    
    //Potentially use array for shooter wheels
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
        manual,
    };

    public void setState(shooterStates state) {
        this.state = state;
    }

    /**
     * Sets the shooter angle from minimum to maximum shooter angle.
     * Minimum angle is 
     */
    public void setAngle(double angle)
    {
        
    }

    /**
     * Sets motor positions based off of state.
     * <p>
     * Returns false if an error has occurred.
     */
    public boolean periodic() {
        return true;
    }
}
