
package team7111.robot.subsystems;

import team7111.robot.subsystems.Intake.IntakeState;
import team7111.robot.subsystems.ShooterSubsystem.ShooterStates;
import team7111.robot.subsystems.Barrel.BarrelStates;
import team7111.robot.subsystems.Barrel;

class SuperStructure {


    Vision vision;
    Intake intake;
    ShooterSubsystem shooter;
    Barrel barrel;

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
        unloaded,
        loaded,
        defaultState,
    }

    controlState control = controlState.defaultState;
    SuperState actual;
    BarrelStates barrelStates;

    private void setSuperState(SuperState state) {
        actual = state;
    }

    private double shotTimer = 0;


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
            case unloaded:
                unloaded();
                break;
            default:
                break;
        }
    }

    private void intakeState() {
        // Sets intake to deploy, and barrel to intake. If beambreak is active, sets main state to shoot
        intake.setState(IntakeState.deploy);
        barrel.setState(BarrelStates.intake);
        if (barrel.getBeamBrake() == true) {
            setSuperState(SuperState.shoot);
        }
    }

    private void shootVision() {
        // sets the shooter state to shoot. Will aim using vision
        shooter.setState(ShooterStates.shoot);
    }
    
    private void eject() {
        // Sets the shooter state to shoot, to eject the game piece
        shooter.setState(ShooterStates.shoot);
    }

    private void shoot() {
        // Sets chooter, and barrel to shoot. Starts the shot timer. When shot timer ends, loops, and set Super state to unloaded
        shooter.setState(ShooterStates.shoot);
        barrel.setState(BarrelStates.shoot);
        if (barrel.getBeamBrake() == true) {
            shotTimer = 0;
        }
        if (shotTimer >= 20) {
            shotTimer = 0;
        }
        if (barrel.getBeamBrake() == false) {
            shotTimer += 1;
        }
        if (shotTimer >= 20) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void secure() {
        // Sets shooter to prepare shot, inatke to transition, and barrel to adjust. If beambreak is false, sets barrel to readjust. If beambreak active sets superstate to loaded
        shooter.setState(ShooterStates.prepareShot);
        intake.setState(IntakeState.transition);
        barrel.setState(BarrelStates.adjust);
        if (barrel.getBeamBrake() == false) {
            if (barrelStates == BarrelStates.readjust) {
            } else {
                barrel.setState(BarrelStates.readjust);
            }
        } else {
            if (barrelStates == BarrelStates.readjust) {
                setSuperState(SuperState.loaded);
            }
        }
    }

    private void manual() {
        // Sets shooter, and inatke to manual for manual control
        shooter.setState(ShooterStates.manual);
        intake.setState(IntakeState.manual);
    }

    private void defaultState() {
        // Sets shooter, and intake to their default state, making them inactive
        shooter.setState(ShooterStates.defaultState);
        intake.setState(IntakeState.defualtState);
    }

    private void unloaded() {
        // Sets intake to store, barrel to unload, and shooter to idle.
        intake.setState(IntakeState.store);
        barrel.setState(BarrelStates.unload);
        shooter.setState(ShooterStates.idle);
    }

    public void periodic() {
        
    }
}