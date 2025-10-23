package team7111.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;

public class Shooter {
    
    public Shooter() {}

    //Potentially use array for shooter wheels
    //private SparkMax[] shooterWheels;

    ShooterStates state = ShooterStates.defaultState;

    public enum ShooterStates {
        prepareShot,
        shoot,
        reverse,
        prepareShotVision,
        defaultState,
        manual,
        idle,
    };

    public void setState(ShooterStates state) {
        this.state = state;
    }

    /**
     * Sets the shooter angle from minimum to maximum shooter angle.
     * Minimum angle is 
     */
    public void setAngle(double angle)
    {
        
    }

    private void manageState() {
        switch (state) {
            case prepareShot:
            prepareShot();
                break;
            case shoot:
            shoot();
                break;
            case reverse:
            reverse();
                break;
            case prepareShotVision:
            prepareShotVision();
                break;
            case manual:
            manual();
                break;
            case idle:
            idle();
                break;
        }
    }

    /**
     * Sets motor positions based off of state.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
        manageState();
    }

    private void prepareShot() {}

    private void shoot() {}

    private void reverse() {}

    private void prepareShotVision() {}

    private void manual() {}

    private void idle() {}
}
