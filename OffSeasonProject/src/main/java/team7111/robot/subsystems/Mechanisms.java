package team7111.robot.subsystems;

public class Mechanisms {
    
    mechanismStates state = mechanismStates.defaultState;

    private enum mechanismStates {
        intake,
        shootClose,
        shootVision,
        prepShot,
        manual,
        defaultState
    };


    public Mechanisms() {}

    public void setMechState(mechanismStates state)
    {
        this.state = state;
    }

    /**
     * Acts on current state of mechanisms and sets motor positions and other subsystem states accordingly.
     * <p>
     * Returns false if an error has occurred.
     */
    public boolean periodic() {
        return true;
    }
}
