package team7111.robot.subsystems;

import com.revrobotics.spark.SparkMax;

public class Intake {

    public Intake() {}

    public enum IntakeStates {  
        store,
        defualtState,
        deploy,
        transition,
        eject,
        manual,
    };

    private IntakeStates state = IntakeStates.defualtState;

    public void setState(IntakeStates state)
    {
        this.state = state;
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
        }
    }

    /**
     * Manages motors controlling the intake mechanism on the robot.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
        manageState();
    }

    private void store() {}

    private void deploy() {}

    private void transition() {}

    private void eject() {}

    private void manual() {}
}
