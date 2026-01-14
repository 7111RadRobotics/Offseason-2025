
package team7111.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SuperStructure extends SubsystemBase{

    public enum ControlState {
        autoTrigger,
    }

    private enum SuperState {
        manual,
        autonomous,
        defaultState,
    }

    private SwerveSubsystem swerve;


    private SuperState superState = SuperState.defaultState;

    public SuperStructure(SwerveSubsystem swerve, int operatorPort) {
        this.swerve = swerve;
    }

    public void periodic() {
        manageSuperState();
        SmartDashboard.putString("SuperState", superState.name());
    }

    private void manageSuperState() {
        switch (superState) {
            case manual:
                manual();
                break;
            case autonomous:
                autonomous();
                break;
            default:
                break;
        }
    }
    private void setControlState(ControlState button, boolean state) {
        switch (button) {
            default:
                break;
        }
    }

    public Command setControlStateCommand(ControlState button, boolean state){
        return runOnce(() -> setControlState(button, state));
    }

    public void setSuperState(SuperState state) {
        superState = state;
    }

    private void autonomous(){
        
    }

    private void manual() {
  
    }
}