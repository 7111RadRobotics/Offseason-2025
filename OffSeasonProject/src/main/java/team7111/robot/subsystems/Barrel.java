package team7111.robot.subsystems;

public class Barrel {

    private BarrelStates state = BarrelStates.defaultState;

    public enum BarrelStates {
        intake,
        adjust,
        readjust,
        shoot,
        reverse,
        unload,
        loaded,
        defaultState
    };

    public void setState(BarrelStates state) {
        this.state = state;
    }

    private void manageState() {
        switch(state) {
            case intake:
                intakeMethod();
            break;

            case adjust:
                adjustMethod();
            break;

            case readjust:
                readjustMethod();
            break;

            case shoot:
                shootMethod();
            break;

            case reverse:
                reverseMethod();
            break;

            case unload:
                unloadMethod();
            break;

            case loaded:
                loadedMethod();
            break;

            case defaultState:
                defaultStateMethod();
            break;
        }
    }

    public void periodic() {
    }

    private void intakeMethod() {
    }

    private void adjustMethod() {
    }

    private void readjustMethod() {
    }

    private void shootMethod() {
    }

    private void reverseMethod() {
    }

    private void unloadMethod() {
    }

    private void loadedMethod() {
    }

    private void defaultStateMethod() {
    }
}
