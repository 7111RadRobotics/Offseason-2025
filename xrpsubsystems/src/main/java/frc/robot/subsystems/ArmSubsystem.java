package frc.robot.subsystems;

import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase{

    private XRPServo servo;
    private ArmState currentState = ArmState.up;

    public enum ArmState {
        down,
        up,
    }

    public ArmSubsystem(){
        servo = new XRPServo(5);
    }

    public void periodic(){
        switch (currentState) {
            case down:
                down();
                break;
            case up:
                up();
                break;
            default:
                break;
        }
    }

    private void down(){
        servo.setAngle(-90);
    }

    private void up(){
        servo.setAngle(0);
    }

    private void setState(ArmState state){
        currentState = state;
    }

    public Command setStateCommand(ArmState state){
        return runOnce(() -> {
            setState(state);
        });
    }
}
