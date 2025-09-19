package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DrivetrainSubsystem extends SubsystemBase {
    

    public enum DrivetrainState {
        manual,
        rotate90,
        forward10,
    }

    public DrivetrainSubsystem(){

    }
}
