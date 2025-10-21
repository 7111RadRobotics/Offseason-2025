package team7111.robot.subsystems;

public class Barrel {

    private barrelStates state = barrelStates.defaultState;

    public enum barrelStates {
        intake,
        adjust,
        readjust,
        shoot,
        reverse,
        unload,
        loaded,
        defaultState
    };

    public void setState(barrelStates state) {
        this.state = state;
    };

    public boolean periodic() {
        return true;
    };
}
