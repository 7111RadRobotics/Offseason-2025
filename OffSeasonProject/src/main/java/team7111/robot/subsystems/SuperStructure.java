
package team7111.robot.subsystems;

import team7111.robot.subsystems.Intake.IntakeStates;
import team7111.robot.subsystems.ShooterSubsystem.ShooterStates;

class SuperStructure {


    Vision vision;
    Intake intake;
    ShooterSubsystem shooter;



    enum superStates {
        intake,
        shoot,
        prepShot,
        prepshotVision,
        unloaded,
        
    };

    controlState control = controlState.defaultState;
    actualState actual;

    private enum controlState {
        aButton,
        bButton,
        xButton,
        yButton,
        rightBumper,
        rightTrigger,
        leftBumper,
        leftTrigger,
        defaultState,
    }

    private enum actualState {
        intake,
        shootClose,
        shootVision,
        prepShot,
        manual,
        defaultState,
    }


    private void manageControl() {
        switch (control) {
            case aButton:
                
                break;
            case bButton:
                
                break;
            case xButton:
                
                break;
            case yButton:
                
                break;
            case rightBumper:
                
                break;
            case rightTrigger:
                
                break;
            case leftBumper:
                
                break;
            case leftTrigger:
                
                break;  
            case defaultState:
            
                break;
            default:
                break;
        }

        manageActual();
    
    }

    private void manageActual() {
        switch (actual) {
            case intake:
                intakeState();
                break;
            case shootClose:
                shootClose();
                break;
            case shootVision:
                shootVision();
                break;
            case prepShot:
                prepShot();
                break;
            case manual:
                manual();
                break;
            case defaultState:
                defaultState();
                break;      
            default:
                break;
        }
    }
        



    private void intakeState() {
        
    }

    private void shootVision() {
        shooter.setState(ShooterStates.shoot);
    }
    
    private void shootClose() {
        shooter.setState(ShooterStates.shoot);
    }

    private void prepShot() {
        shooter.setState(ShooterStates.prepareShot);
        intake.setState(IntakeStates.transition);
    }

    private void manual() {
        shooter.setState(ShooterStates.manual);
        intake.setState(IntakeStates.manual);
    }

    private void defaultState() {
        shooter.setState(ShooterStates.defaultState);
        intake.setState(IntakeStates.defualtState);
    }

    public void periodic() {
    }
}