
package team7111.robot.subsystems;

import team7111.robot.subsystems.IntakeSubsystem.IntakeState;
import team7111.robot.subsystems.ShooterSubsystem.ShooterStates;
import team7111.robot.subsystems.BarrelSubsytem.BarrelStates;
import team7111.robot.subsystems.BarrelSubsytem;

public class SuperStructure {


    VisionSubsystem vision;
    IntakeSubsystem intake;
    ShooterSubsystem shooter;
    BarrelSubsytem barrel;

    public enum controlState {
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

    public void setControlState(controlState state) {
        control = state;
    }

    private double shotTimer = 0;
    private double ejectTimer = 0;

    private void manageControl() {
        switch (control) {
            case aButton:
            //TODO change this button to the proper one
            // If the a button is held down, and superstate is unloaded, sets superstate to intake
                if (actual == SuperState.unloaded) {
                    setSuperState(SuperState.intake);
                }
                break;
            case bButton:
            //TODO change this button to the proper one
            // if the b Button is held, and superstate is equal to loaded, sets superstate to eject
                if (actual == SuperState.loaded) {
                    setSuperState(SuperState.eject);
                }
                break;
            case xButton:
            //TODO change this button to the proper one
            // if x button is pressed, and superstate is equal to loaded, set superstate to prepareShot
                if (actual == SuperState.loaded) {
                    setSuperState(SuperState.prepareShot);
                }
                break;
            case yButton:
            //TODO change this button to the proper one
            // if y button is pressed, and superstate is equal to prepareShot, set superstate to shoot
                if (actual == SuperState.prepareShot) {
                    setSuperState(SuperState.shoot);
                }
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
            case prepareShot:
                prepareShot();
                break;
            case prepareShotVision:
                prepareShotVision();
                break;
            default:
                break;
        }
    }

    private void intakeState() {
        // Sets intake to deploy, and barrel to intake. If beambreak is active, sets main state to secure
        intake.setState(IntakeState.deploy);
        barrel.setState(BarrelStates.intake);
        if (barrel.getBeamBrake() == true) {
            setSuperState(SuperState.secure);
        }
        if (control != controlState.aButton) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void shootVision() {
        // sets the shooter state to shoot. Will aim using vision
        shooter.setState(ShooterStates.shoot);
    }
    
    private void eject() {
        // Sets the shooter state to shoot, to eject the game piece from intake and set superstate to unloaded
        if (barrel.getBeamBrake() == false) {
            ejectTimer += 1;
        }
        if (ejectTimer == 500) {
            setSuperState(SuperState.unloaded);
        }
        if (control != controlState.bButton) {
            setSuperState(SuperState.secure);
        }
    }

    private void prepareShot() {
        shooter.setState(ShooterStates.prepareShot);

    }

    private void shoot() {
        // Sets shooter, and barrel to shoot. Starts the shot timer. When shot timer ends, loops, and set Super state to unloaded
        shooter.setState(ShooterStates.shoot);
        barrel.setState(BarrelStates.shoot);
        if (barrel.getBeamBrake() == true || shotTimer >= 20) {
            shotTimer = 0;
        }
        if (barrel.getBeamBrake() == false) {
            shotTimer += 1;
        }
        if (shotTimer >= 20) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void prepareShotVision() {

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