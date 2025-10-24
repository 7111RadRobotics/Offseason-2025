package team7111.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotBase;

import team7111.lib.pathfinding.*;
import team7111.robot.Constants.ControllerConstants;
import team7111.robot.Constants.SwerveConstants;
import team7111.robot.utils.SwerveModule;
import team7111.robot.utils.gyro.NavXGyro;
import team7111.robot.utils.singleaxisgyro.GenericSwerveGyro;
import team7111.robot.utils.singleaxisgyro.RealSwerveGyro;
import team7111.robot.utils.singleaxisgyro.SimSwerveGyro;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveModule[] modules;

    private final SwerveDriveOdometry swerveOdometry;
    private Field2d field = new Field2d();

    private final GenericSwerveGyro gyro;
    private SwerveModuleState[] states = new SwerveModuleState[]{};

    private StructArrayPublisher<SwerveModuleState> commandedStatePublisher = 
            NetworkTableInstance.getDefault().getStructArrayTopic("Commanded Swerve States", SwerveModuleState.struct).publish();
    private StructArrayPublisher<SwerveModuleState> actualStatePublisher = 
            NetworkTableInstance.getDefault().getStructArrayTopic("Actual Swerve States", SwerveModuleState.struct).publish();
    private StructPublisher<Pose2d> robotPosePublisher = 
            NetworkTableInstance.getDefault().getStructTopic("Robot Pose", Pose2d.struct).publish();
    
    private PathMaster pathMaster = null;

    private SwerveState currentSwerveState = SwerveState.manual;

    private Path path = null;

    private boolean isDriveFieldRelative;

    private DoubleSupplier joystickYTranslation = () -> 0;
    private DoubleSupplier joystickXTranslation = () -> 0;
    private DoubleSupplier joystickYaw = () -> 0;

    public enum SwerveState{
        initializePath,
        runPath,
        manual,
        stationary
    };

    public SwerveSubsystem() {
        modules = new SwerveModule[] {
            new SwerveModule(0, SwerveConstants.drivebaseConfig.moduleTypes[0]),
            new SwerveModule(1, SwerveConstants.drivebaseConfig.moduleTypes[1]),
            new SwerveModule(2, SwerveConstants.drivebaseConfig.moduleTypes[2]),
            new SwerveModule(3, SwerveConstants.drivebaseConfig.moduleTypes[3]),
        };

        gyro = RobotBase.isReal()
            ? new RealSwerveGyro(new NavXGyro())
            : new SimSwerveGyro(this::getStates, SwerveConstants.kinematics);
        gyro.setInverted(true);
        zeroGyro();

        pathMaster = new PathMaster(this::getPose, () -> getYaw());
        pathMaster.setTranslationPID(5.0, 0.0, 0.0);
        pathMaster.setRotationPID(5.0, 0, 0);
        pathMaster.setInversions(false, false, true, false);

        swerveOdometry = new SwerveDriveOdometry(SwerveConstants.kinematics, getYaw(), getPositions());
        
    }

    public void manageSwerveState(){
        switch(currentSwerveState){
            case initializePath:
                if(path == null){
                    setSwerveState(SwerveState.stationary);
                    break;
                }
                pathMaster.initializePath(path);
                setSwerveState(SwerveState.runPath);
                break;
                
            case runPath:
                if(path == null){
                    setSwerveState(SwerveState.initializePath);
                    break;
                }
                if(path.isPathFinished()){
                    setSwerveState(SwerveState.stationary);
                    break;
                }
                pathMaster.periodic(path);
                ChassisSpeeds speeds = pathMaster.getPathSpeeds(path, false, true);
                setModuleStates(SwerveConstants.kinematics.toSwerveModuleStates(speeds)); 
                if(path.getWaypoints().length == 0){
                    path = null;
                }
                break;

            case manual:
                manual(joystickXTranslation.getAsDouble(), joystickYTranslation.getAsDouble(), joystickYaw.getAsDouble(), isDriveFieldRelative, false);
                break;
            case stationary:
                manual(0, 0, 0, false, false);
            default:
                break;
        }
    }

    public void manual(double forwardBack, double leftRight, double rotation, boolean isFieldRelative, boolean isOpenLoop){
        // Adding deadzone.
        forwardBack = Math.abs(forwardBack) < ControllerConstants.axisDeadzone ? 0 : forwardBack;
        leftRight = Math.abs(leftRight) < ControllerConstants.axisDeadzone ? 0 : leftRight;
        rotation = Math.abs(rotation) < ControllerConstants.axisDeadzone ? 0 : rotation;

        // Converting to m/s
        forwardBack *= SwerveConstants.maxDriveVelocity;
        leftRight *= SwerveConstants.maxDriveVelocity;
        rotation *= SwerveConstants.maxAngularVelocity;

        // Get desired module states.
        ChassisSpeeds chassisSpeeds = isFieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(forwardBack, leftRight, rotation, getYaw())
            : new ChassisSpeeds(forwardBack, leftRight, rotation);

        SwerveModuleState[] states = SwerveConstants.kinematics.toSwerveModuleStates(chassisSpeeds);

        setModuleStates(states, isOpenLoop);
    }

    public Command setSwerveStateCommand(SwerveState swerveState){
        return runOnce(()-> currentSwerveState = swerveState);
    }

    public void setSwerveState(SwerveState swerveState){
        currentSwerveState = swerveState;
    }

    public SwerveState getSwerveState(){
        return currentSwerveState;
    }

    /** To be used by auto. Use the drive method during teleop. */
    public void setModuleStates(SwerveModuleState[] states) {
        setModuleStates(states, false);
    }

    private void setModuleStates(SwerveModuleState[] states, boolean isOpenLoop) {
        // Makes sure the module states don't exceed the max speed.
        SwerveDriveKinematics.desaturateWheelSpeeds(states, SwerveConstants.maxDriveVelocity);
        this.states = states;
        for (int i = 0; i < modules.length; i++) {
            modules[i].setState(states[modules[i].moduleNumber], isOpenLoop);
        }
    }

    public SwerveModuleState[] getStates() {
        SwerveModuleState currentStates[] = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++) {
            currentStates[i] = modules[i].getState();
        }
        return currentStates;
    }

    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition currentStates[] = new SwerveModulePosition[modules.length];
        for (int i = 0; i < modules.length; i++) {
            currentStates[i] = modules[i].getPosition();
        }

        return currentStates;
    }

    public Rotation2d getYaw() {
        return gyro.getRotation(); //Rotation2d.fromDegrees(gyro.getYaw());
    }

    public Command zeroGyroCommand() {
        return runOnce(this::zeroGyro).withName("ZeroGyroCommand");
    }

    private void zeroGyro() {
        gyro.setRotation(Rotation2d.kZero);
    }

    public Pose2d getPose() {
        return swerveOdometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getPositions(), pose);
    }

    public ChassisSpeeds getRelSpeeds() {
        ChassisSpeeds relSpeed = SwerveConstants.kinematics.toChassisSpeeds(getStates());
        return relSpeed;
    }

    public void setRelSpeeds(ChassisSpeeds speeds){
        speeds.omegaRadiansPerSecond = -speeds.omegaRadiansPerSecond;
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = SwerveConstants.kinematics.toSwerveModuleStates(speeds);
        setModuleStates(states);
    }

    public void setJoysickInputs(DoubleSupplier joystickYTranslation, DoubleSupplier joystickXTranslation, DoubleSupplier joystickYaw){
        this.joystickXTranslation = joystickXTranslation;
        this.joystickYTranslation = joystickYTranslation;
        this.joystickYaw = joystickYaw;
    }

    public void setDriveFieldRelative(boolean isFieldRelative){
        isDriveFieldRelative = isFieldRelative;
    }

    public Command setPath(Path path){
        return runOnce(() -> this.path = path);
    }

    @Override 
    public void periodic() {
        gyro.update();
        swerveOdometry.update(getYaw(), getPositions());
        commandedStatePublisher.set(states);

        for(SwerveModule mod : modules){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getEncoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Drive Meters", mod.getPosition().distanceMeters);
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Drive Rotations", mod.getPosition().distanceMeters / SwerveConstants.wheelCircumference);
        }
        SmartDashboard.putNumber("Gyro Yaw", getYaw().getDegrees());

        field.setRobotPose(getPose());
        SmartDashboard.putData(field);
        robotPosePublisher.set(getPose());

        actualStatePublisher.set(getStates());

        manageSwerveState();

        SmartDashboard.putString("swerveState", currentSwerveState.name());
    }

    public void simulationPeriodic(){
        for (SwerveModule mod : modules) {
            mod.module.update();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        for (SwerveModule module : modules) {
            builder.addDoubleProperty(
            String.format("Drive Pos %d", module.moduleNumber),
            () -> module.getPosition().distanceMeters,
            null);

            
            builder.addDoubleProperty(
            String.format("Angle %d", module.moduleNumber),
            () -> module.getAngle().getDegrees(),
            null);
        }
    }
}
