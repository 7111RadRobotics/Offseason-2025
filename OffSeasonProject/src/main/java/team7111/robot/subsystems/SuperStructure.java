
package team7111.robot.subsystems;

import team7111.robot.subsystems.Intake.intakeStates;
import team7111.robot.subsystems.Shooter.shooterStates;

class SuperStructure {


    Vision vision;
    Intake intake;
    Shooter shooter;



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
        shooter.setState(shooterStates.intake);
        intake.setState(intakeStates.intake);
    }

    private void shootVision() {
        shooter.setState(shooterStates.shoot);
    }
    
    private void shootClose() {
        shooter.setState(shooterStates.shoot);
    }

    private void prepShot() {
        shooter.setState(shooterStates.prepareShot);
        intake.setState(intakeStates.transition);
    }

    private void manual() {
        shooter.setState(shooterStates.manual);
        intake.setState(intakeStates.manual);
    }

    private void defaultState() {
        shooter.setState(shooterStates.defaultState);
        intake.setState(intakeStates.defualtState);
    }

    public void periodic() {
    }
}