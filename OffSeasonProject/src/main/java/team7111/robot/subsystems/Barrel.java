package team7111.robot.subsystems;

public class Barrel {
    private Barrelstates state = Barrelstates.intake;

    public enum Barrelstates {
        intake,
        adjust,
        readjust,
        shoot,
        reverse,
        unload,
        loaded
    };
}
