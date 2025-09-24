package team7111.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
//import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.auto.NamedCommands;

import team7111.lib.pathfinding.Waypoint;
import team7111.lib.pathfinding.WaypointConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import team7111.lib.pathfinding.Path;
import team7111.robot.Constants.ControllerConstants;
import team7111.robot.Constants.SwerveConstants;
import team7111.robot.subsystems.swerve.SwerveSubsystem;
import team7111.robot.subsystems.swerve.SwerveSubsystem.SwerveState;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    public final CommandXboxController driverController = new CommandXboxController(ControllerConstants.driverControllerID);
    public final CommandXboxController operatorController = new CommandXboxController(ControllerConstants.driverControllerID);
    public PathPlannerAuto auto1;
    public final SwerveSubsystem swerve;
    public SendableChooser<Command> autoChooser;

    public RobotContainer() {
        swerve = new SwerveSubsystem();

        autoChooser = new SendableChooser<>();

        Waypoint[] waypoints = new Waypoint[]{
            new Waypoint(new Pose2d(3, 1, Rotation2d.fromDegrees(0)), new WaypointConstraints(1, 0, 0.1), new WaypointConstraints(360, 0, 5)), 
            new Waypoint(new Pose2d(7, 6, Rotation2d.fromDegrees(0)), new WaypointConstraints(1, 0, 0.1), new WaypointConstraints(360, 0, 5)),
        };


        autoChooser.addOption("Path_TEST", swerve.setPath(new Path(waypoints)));

        SmartDashboard.putData("autoChooser", autoChooser);

        NamedCommands.registerCommand("test command", auto1);

        // Configure button bindings
        configureButtonBindings();
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected().alongWith(swerve.setSwerveStateCommand(SwerveState.initializePath));
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
    }
}
