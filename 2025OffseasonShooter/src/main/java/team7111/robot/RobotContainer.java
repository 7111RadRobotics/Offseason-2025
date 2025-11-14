package team7111.robot;

import team7111.lib.pathfinding.Waypoint;
import team7111.lib.pathfinding.WaypointConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import team7111.lib.pathfinding.Path;
import team7111.robot.Constants.ControllerConstants;
import team7111.robot.Constants.SwerveConstants;
import team7111.robot.subsystems.SuperStructure;
import team7111.robot.subsystems.SwerveSubsystem;
import team7111.robot.subsystems.SwerveSubsystem.SwerveState;
import team7111.robot.subsystems.SuperStructure.controlState;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    public final CommandXboxController driverController = new CommandXboxController(ControllerConstants.driverControllerID);
    public final CommandXboxController operatorController = new CommandXboxController(ControllerConstants.driverControllerID);
    public final SwerveSubsystem swerve;
    public SendableChooser<Command> autoChooser;
    SuperStructure superStructure;

    public RobotContainer() {
        DriverStation.silenceJoystickConnectionWarning(true);
        swerve = new SwerveSubsystem();

        autoChooser = new SendableChooser<>();

        Waypoint[] waypoints = new Waypoint[]{
            new Waypoint(new Pose2d(7.217, 4.199, Rotation2d.fromDegrees(180.0)), new WaypointConstraints(10, 0, 0.25), new WaypointConstraints(360, 0, 10)),
            new Waypoint(new Pose2d(5.886, 4.199, Rotation2d.fromDegrees(180.0)), new WaypointConstraints(10, 0, 0.25), new WaypointConstraints(360, 0, 10)), 
            new Waypoint(new Pose2d(5.886, 5.601, Rotation2d.fromDegrees(180.0)), new WaypointConstraints(10, 3, 0.4), new WaypointConstraints(360, 0, 360)),
            new Waypoint(new Pose2d(1.486, 7.184, Rotation2d.fromDegrees(-53.7)), new WaypointConstraints(10, 0, 0.25), new WaypointConstraints(360, 0, 10)),
            new Waypoint(new Pose2d(3.644, 5.146, Rotation2d.fromDegrees(-60.1)), new WaypointConstraints(10, 0, 0.25), new WaypointConstraints(360, 0, 10)),
        };

        Path path = new Path(waypoints);

        autoChooser.addOption("Path_TEST", swerve.setPath(path).andThen(swerve.setSwerveStateCommand(SwerveState.initializePath)));

        SmartDashboard.putData("autoChooser", autoChooser);

        // Configure button bindings
        configureButtonBindings();
    }

    public Command getAutonomousCommand() {
        Command auto = Commands.print("autochooser null");
        if(autoChooser != null)
            auto = autoChooser.getSelected();
        return auto;
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        swerve.setJoysickInputs(
            () -> -ControllerConstants.xDriveLimiter.calculate((Math.pow(driverController.getLeftX(), 3) / SwerveConstants.sensitivity)), 
            () -> -ControllerConstants.yDriveLimiter.calculate((Math.pow(driverController.getLeftY(), 3) / SwerveConstants.sensitivity)),  
            () -> ControllerConstants.rotationLimiter.calculate((Math.pow(driverController.getRightX(), 3) / SwerveConstants.sensitivity)));

        swerve.setDriveFieldRelative(true);

        driverController.start().onTrue(swerve.zeroGyroCommand());

        driverController.a().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.aButton)));
        driverController.b().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.bButton)));
        driverController.x().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.xButton)));
        driverController.y().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.yButton)));
        driverController.rightBumper().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.rightBumper)));
        driverController.rightTrigger().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.rightTrigger)));
        driverController.leftBumper().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.leftBumper)));
        driverController.leftTrigger().onTrue(Commands.runOnce(() -> superStructure.setControlState(controlState.rightTrigger)));
    }
}
