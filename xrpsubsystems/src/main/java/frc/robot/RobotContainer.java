package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ArmSubsystem.ArmState;

public class RobotContainer {
    public ArmSubsystem armSubsystem;
    public DrivetrainSubsystem driveSubsystem;

    private CommandXboxController controller = new CommandXboxController(0);

    public RobotContainer(){
        armSubsystem = new ArmSubsystem();
        driveSubsystem = new DrivetrainSubsystem();

        configureBindings();
    }

    private void configureBindings(){
        controller.a().onTrue(armSubsystem.setStateCommand(ArmState.down))
            .onFalse(armSubsystem.setStateCommand(ArmState.up));
    }

    public Command getAutonomousCommand(){
        return Commands.print("No Autonomous Created");
    }
}
