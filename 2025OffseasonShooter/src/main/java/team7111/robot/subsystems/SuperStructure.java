
package team7111.robot.subsystems;

import team7111.robot.subsystems.IntakeSubsystem.IntakeState;
import team7111.robot.subsystems.ShooterSubsystem.ShooterStates;
import team7111.robot.subsystems.BarrelSubsystem.BarrelStates;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team7111.robot.subsystems.BarrelSubsystem;

public class SuperStructure extends SubsystemBase{

    public enum ControlState {
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

    private VisionSubsystem vision;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;
    private BarrelSubsystem barrel;
    private SwerveSubsystem swerve;
    private PathSubsystem paths;

    private boolean aButton = false;
    private boolean bButton = false;
    private boolean xButton = false;
    private boolean yButton = false;
    private boolean rightBumper = false;
    private boolean rightTrigger = false;
    private boolean leftBumper = false;
    private boolean leftTrigger = false;

    private ControlState controlState = ControlState.defaultState;
    private SuperState superState;

    private double shotTimer = 0;
    private double ejectTimer = 0;

    public SuperStructure(VisionSubsystem vision, SwerveSubsystem swerve, PathSubsystem paths, IntakeSubsystem intake, BarrelSubsystem barrel, ShooterSubsystem shooter){
        this.vision=vision;

    }

    public void periodic() {
        manageSuperState();
    }

    private void manageSuperState() {
        switch (superState) {
            case intake:
                intake();
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
    public void setControlState(ControlState button, boolean state) {
        switch (button) {
            case aButton:
                aButton = state;
                break;
            case bButton:
                bButton = state;
                break;
            case xButton:
                xButton = state;
                break;
            case yButton:
                yButton = state;
                break;
            case rightTrigger:
                rightTrigger = state;
                break;
            case rightBumper:
                rightBumper = state;
                break;
            case leftBumper:
                leftBumper = state;
                break;
            case leftTrigger:
                leftTrigger = state;
                break;
        }
    }

    public void setSuperState(SuperState state) {
        superState = state;
    }

    private void intake() {
        // Sets intake to deploy, and barrel to intake. If beambreak is active, sets main state to secure
        intake.setState(IntakeState.deploy);
        barrel.setState(BarrelStates.intake);
        if (barrel.getBeamBrake()) {
            setSuperState(SuperState.secure);
        }
        if (controlState != ControlState.aButton) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void shootVision() {
        // sets the shooter state to shoot. Will aim using vision
        shooter.setState(ShooterStates.shoot);
    }
    
    private void eject() {
        // Sets the shooter state to shoot, to eject the game piece from intake and set superstate to unloaded
        if (!barrel.getBeamBrake()) {
            intake.setState(IntakeState.eject);
            ejectTimer += 1;
        }
        if (ejectTimer == 500) {
            intake.setState(IntakeState.store);
            setSuperState(SuperState.unloaded);
            ejectTimer = 0;
        }
        if (controlState != ControlState.bButton) {
            intake.setState(IntakeState.store);
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
        if (barrel.getBeamBrake() || shotTimer >= 20) {
            shotTimer = 0;
        }
        if (!barrel.getBeamBrake()) {
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
        if (!barrel.getBeamBrake()) {
            if (barrel.getState() == BarrelStates.readjust) {
            } else {
                barrel.setState(BarrelStates.readjust);
            }
        } else {
            if (barrel.getState() == BarrelStates.readjust) {
                setSuperState(SuperState.loaded);
            }
        }
    }

    private void manual() {
        // Sets shooter, and inatke to manual for manual controlState
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
}