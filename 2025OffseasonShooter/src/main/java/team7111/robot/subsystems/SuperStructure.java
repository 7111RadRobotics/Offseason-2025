
package team7111.robot.subsystems;

import team7111.robot.subsystems.IntakeSubsystem.IntakeState;
import team7111.robot.subsystems.ShooterSubsystem.ShooterState;
import team7111.robot.subsystems.BarrelSubsystem.BarrelState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team7111.robot.subsystems.BarrelSubsystem;

public class SuperStructure extends SubsystemBase{

    public enum ControlState {
        unloadedButton,
        loadedButton,
        prepareButton,
        yButton,
        rightBumper,
        rightTrigger,
        leftBumper,
        intakeTrigger,
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

    private boolean unloadedButton = false;
    private boolean loadedButton = false;
    private boolean prepareButton = false;
    private boolean ejectButton = false;
    private boolean rightBumper = false;
    private boolean rightTrigger = false;
    private boolean leftBumper = false;
    private boolean intakeTrigger = false;

    private SuperState superState = SuperState.unloaded;

    private double shotTimer = 0;
    private double ejectTimer = 0;

    public SuperStructure(
        VisionSubsystem vision, SwerveSubsystem swerve, PathSubsystem paths, 
        IntakeSubsystem intake, BarrelSubsystem barrel, ShooterSubsystem shooter
    ){
        this.vision = vision;
        this.swerve = swerve;
        this.paths = paths;
        this.intake = intake;
        this.barrel = barrel;
        this.shooter = shooter;
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
            case loaded:
                loaded();
                break;
            default:
                break;
        }
    }
    private void setControlState(ControlState button, boolean state) {
        switch (button) {
            case unloadedButton:
                unloadedButton = state;
                break;
            case loadedButton:
                loadedButton = state;
                break;
            case prepareButton:
                prepareButton = state;
                break;
            case yButton:
                ejectButton = state;
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
            case intakeTrigger:
                intakeTrigger = state;
                break;
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

    private void intake() {
        // Sets intake to deploy, and barrel to intake. If beambreak is active, sets main state to secure
        intake.setState(IntakeState.deploy);
        barrel.setState(BarrelState.intake);
        if (barrel.getBeamBrake()) {
            setSuperState(SuperState.secure);
        }
        if (!intakeTrigger) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void loaded() {
        if (ejectButton) {
            setSuperState(SuperState.eject);
        }
        if (loadedButton) {
            setSuperState(SuperState.prepareShot);
        }
    }

    private void shootVision() {
        // sets the shooter state to shoot. Will aim using vision
        shooter.setState(ShooterState.shoot);
    }
    
    private void eject() {
        // Sets the shooter state to shoot, to eject the game piece from intake and set superstate to unloaded
        intake.setState(IntakeState.eject);
        barrel.setState(BarrelState.reverse);
        if (!barrel.getBeamBrake()) {
            ejectTimer += 1;
        }
        if (ejectTimer >= 500) {
            setSuperState(SuperState.unloaded);
            ejectTimer = 0;
        }
        if (!ejectButton) {
            setSuperState(SuperState.secure);
        }
    }

    private void prepareShot() {
        shooter.setState(ShooterState.prepareShot);
        if (prepareButton) {
            setSuperState(SuperState.shoot);
        }
    }

    private void shoot() {
        // Sets shooter, and barrel to shoot. Starts the shot timer. When shot timer ends, loops, and set Super state to unloaded
        shooter.setState(ShooterState.shoot);
        barrel.setState(BarrelState.shoot);
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
        shooter.setState(ShooterState.prepareShot);
        intake.setState(IntakeState.transition);
        barrel.setState(BarrelState.adjust);
        if (!barrel.getBeamBrake()) {
            if (barrel.getState() == BarrelState.readjust) {
            } else {
                barrel.setState(BarrelState.readjust);
            }
        } else {
            if (barrel.getState() == BarrelState.readjust) {
                setSuperState(SuperState.loaded);
            }
        }
    }

    private void manual() {
        // Sets shooter, and inatke to manual for manual controlState
        shooter.setState(ShooterState.manual);
        intake.setState(IntakeState.manual);
    }

    private void defaultState() {
        // Sets shooter, and intake to their default state, making them inactive
        shooter.setState(ShooterState.defaultState);
        intake.setState(IntakeState.defualtState);
    }

    private void unloaded() {
        // Sets intake to store, barrel to unload, and shooter to idle.
        intake.setState(IntakeState.store);
        barrel.setState(BarrelState.unload);
        shooter.setState(ShooterState.idle);
        if (intakeTrigger) {
            setSuperState(SuperState.intake);
        }
    }
}