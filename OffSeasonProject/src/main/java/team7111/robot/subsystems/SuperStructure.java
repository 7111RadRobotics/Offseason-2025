
package team7111.robot.subsystems;

import team7111.robot.subsystems.Intake.IntakeStates;
import team7111.robot.subsystems.ShooterSubsystem.ShooterStates;
import team7111.robot.subsystems.Barrel.BarrelStates;
import team7111.robot.subsystems.Barrel;

class SuperStructure {


    Vision vision;
    Intake intake;
    ShooterSubsystem shooter;
    Barrel barrel;

    private double shotTimer = 0;

    enum superStates {
        intake,
        shoot,
        prepShot,
        prepshotVision,
        unloaded,
        loaded
    };

    superStates superState = superStates.unloaded;
    controlState control = controlState.defaultState;
    SuperState actual;

    private void setSuperState(SuperState state) {
        actual = state;
    }

    private void setSuperStates(superStates state) {
        superState = state;
    }

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

    private enum SuperState {
        intake,
        secure,
        shoot,
        prepareShot,
        prepareShotVision,
        eject,
        shootVision,
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
            case secure:
                secure();
                break;
            case shootVision:
                shootVision();
                break;
            case eject:
                eject();
                break;
            case manual:
                manual();
                break;
            case defaultState:
                defaultState();
                break;   
            case shoot:
                shoot();
                break;
            default:
                break;
        }
    }

    private void intakeState() {
        intake.setState(IntakeStates.deploy);
        barrel.setState(BarrelStates.intake);
        if (barrel.getBeamBrake() == true) {
            setSuperState(SuperState.secure);
        }
    }

    private void shootVision() {
        shooter.setState(ShooterStates.shoot);
    }
    
    private void eject() {
        shooter.setState(ShooterStates.shoot);
    }

    private void shoot() {
        shooter.setState(ShooterStates.shoot);
        barrel.setState(BarrelStates.shoot);
        if (barrel.getBeamBrake() == true) {
            shotTimer += 1;
        }
        if (shotTimer >= 20) {
            setSuperStates(superStates.unloaded);
        }
    }

    private void secure() {
        shooter.setState(ShooterStates.prepareShot);
        intake.setState(IntakeStates.transition);
        barrel.setState(BarrelStates.adjust);
        if (barrel.getBeamBrake() == false) {
            barrel.setState(BarrelStates.readjust);
            if (barrel.getBeamBrake() == true) {
                setSuperStates(superStates.loaded);
            }
        }
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