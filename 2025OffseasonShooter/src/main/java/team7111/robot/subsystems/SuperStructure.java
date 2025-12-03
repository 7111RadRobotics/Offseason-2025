
package team7111.robot.subsystems;

import team7111.robot.subsystems.IntakeSubsystem.IntakeState;
import team7111.robot.subsystems.ShooterSubsystem.ShooterState;
import team7111.robot.subsystems.BarrelSubsystem.BarrelState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team7111.robot.subsystems.BarrelSubsystem;

public class SuperStructure extends SubsystemBase{

    public enum ControlState {
        intakeTrigger,
        ejectTrigger,
        prepareShotTrigger,
        shootTrigger,
        manualToggle,
        manualIntakeWheels,
        manualIntakePivot,
        manualBarrelWheels,
        manualShooterWheels,
        manualShooterPivot,
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

    private boolean intakeTrigger = false;
    private boolean ejectTrigger = false;
    private boolean prepareShotTrigger = false;
    private boolean shootTrigger = false;
    private boolean manualToggle = false;
    //TODO Add manual Logic in manual method
    private boolean manualForwardIntakeWheels = false;
    private boolean manualBackwardIntakeWheels = false;
    private boolean manualForwardIntakePivot = false;
    private boolean manualBackwardIntakePivot = false;
    private boolean manualForwardBarrelWheels = false;
    private boolean manualBackwardBarrelWheels = false;
    private boolean manualForwardsShooterWheels = false;
    private boolean manualBackwardShooterWheels = false;
    private boolean manualForwardShooterPivot = false;
    private boolean manualBackwardShooterPivot = false;

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
        SmartDashboard.putString("SuperState", superState.name());
        SmartDashboard.putBoolean("ManualToggle", manualToggle);
        if (manualToggle)
            setSuperState(superState.manual);
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
            case intakeTrigger:
                intakeTrigger = state;
                break;
            case ejectTrigger:
                ejectTrigger = state;
                break;
            case prepareShotTrigger:
                prepareShotTrigger = state;
                break;
            case shootTrigger:
                shootTrigger = state;
                break;
            case manualToggle:
                manualToggle = state;
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

    private void flipManual() {
        manualToggle = !manualToggle;
        
    }

    public Command flipManualCommand() {
        return runOnce(() -> flipManual());
    }

    private void unloaded() {
        // Sets intake to store, barrel to unload, and shooter to idle.
        intake.setState(IntakeState.store);
        barrel.setState(BarrelState.unload);
        shooter.setState(ShooterState.idle);
        if (intakeTrigger) {
            setSuperState(SuperState.intake);
        }
        if (superState != superState.manual && manualToggle == true)
            setSuperState(superState.manual);
        


    }

    private void intake() {
        // Sets intake to deploy, and barrel to intake. If beambreak is active, sets main state to secure
        intake.setState(IntakeState.deploy);
        barrel.setState(BarrelState.intake);
        System.out.println("Intake Switch");
        if (barrel.getBeamBreak()) {
            setSuperState(SuperState.secure);
        }
        if (!intakeTrigger) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void secure() {
        // Sets shooter to prepare shot, inatke to transition, and barrel to adjust. If beambreak is false, sets barrel to readjust. If beambreak active sets superstate to loaded
        shooter.setState(ShooterState.prepareShot);
        intake.setState(IntakeState.transition);
        barrel.setState(BarrelState.readjust);
        if (!barrel.getBeamBreak()) {
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

    private void loaded() {
        intake.setState(IntakeState.store);
        barrel.setState(BarrelState.loaded);
        shooter.setState(ShooterState.idle);
        if (ejectTrigger) {
            setSuperState(SuperState.eject);
        }
        if (prepareShotTrigger) {
            setSuperState(SuperState.prepareShot);
        }
    }

    private void eject() {
        // Sets the shooter state to shoot, to eject the game piece from intake and set superstate to unloaded
        intake.setState(IntakeState.eject);
        barrel.setState(BarrelState.reverse);
        if (!barrel.getBeamBreak()) {
            ejectTimer += 1;
        }
        if (ejectTimer >= 500) {
            setSuperState(SuperState.unloaded);
            ejectTimer = 0;
        }
        if (!ejectTrigger) {
            setSuperState(SuperState.secure);
        }
    }

    private void prepareShot() {
        shooter.setState(ShooterState.prepareShot);
        intake.setState(IntakeState.store);
        barrel.setState(BarrelState.loaded);
        if (shootTrigger) {
            setSuperState(SuperState.shoot);
        }
    }

    private void prepareShotVision() {

    }

    private void shoot() {
        // Sets shooter, and barrel to shoot. Starts the shot timer. When shot timer ends, loops, and set Super state to unloaded
        shooter.setState(ShooterState.shoot);
        barrel.setState(BarrelState.shoot);
        intake.setState(IntakeState.store);
        if (!barrel.getBeamBreak() || shotTimer >= 50) {
            shotTimer = 0;
        }
        if (barrel.getBeamBreak()) {
            shotTimer += 1;
        }
        if (shotTimer >= 50) {
            setSuperState(SuperState.unloaded);
        }
    }

    private void shootVision() {
        // sets the shooter state to shoot. Will aim using vision
        shooter.setState(ShooterState.shoot);
    }

    private void manual() {
        // Sets shooter, and inatke to manual for manual controlState
        shooter.setState(ShooterState.manual);
        intake.setState(IntakeState.manual);
        if (!manualToggle)
            setSuperState(superState.unloaded);
    }

    private void defaultState() {
        // Sets shooter, and intake to their default state, making them inactive
        shooter.setState(ShooterState.defaultState);
        intake.setState(IntakeState.defualtState);
        if (manualForwardBarrelWheels) {
            barrel.setManualSpeed(1);
        } else if (manualBackwardBarrelWheels) {
            barrel.setManualSpeed(-1);
        } else {
            barrel.setManualSpeed(0);  
        }
        if (manualForwardIntakePivot) {
            intake.addManualAngle(5);
        } else if (manualBackwardIntakePivot) {
            intake.addManualAngle(-5);
        } else {
            intake.addManualAngle(0);
        }
        if (manualForwardShooterPivot) {
            shooter.addManualAngle(5);
        } else if (manualBackwardShooterPivot) {
            shooter.addManualAngle(-5);
        } else {
            shooter.addManualAngle(0);
        }
        if (manualForwardsShooterWheels) {
            shooter.setManualSpeed(1);
        } else if (manualBackwardShooterWheels) {
            shooter.setManualSpeed(-1);
        } else {
            shooter.setManualSpeed(0);
        }
    }
}