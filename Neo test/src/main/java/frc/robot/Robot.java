// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

  private PIDController pid = new PIDController(0.01, 0.00000000, 0.0008);

  private SparkMax spark1 = new SparkMax(3, MotorType.kBrushless);
  private SparkMax spark2 = new SparkMax(5, MotorType.kBrushless);
  private SparkMax spark3 = new SparkMax(1, MotorType.kBrushless);
  private SparkMax spark4 = new SparkMax(7, MotorType.kBrushless);

  private SparkClosedLoopController spark1PID;
  private SparkClosedLoopController spark2PID;
  private SparkClosedLoopController spark3PID;
  private SparkClosedLoopController spark4PID;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    //var spark1PIDConfig = new ClosedLoopConfig().pid(0.005, 0.000000001, 0.005);
    var spark1EncConfig = new EncoderConfig();
    
    spark4.configure(new SparkMaxConfig().apply(spark1EncConfig), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    spark1PID = spark1.getClosedLoopController();
    spark2PID = spark2.getClosedLoopController();
    spark3PID = spark3.getClosedLoopController();
    spark4PID = spark4.getClosedLoopController();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("velocity", spark4.getEncoder().getVelocity());
    SmartDashboard.putNumber("position", spark4.getEncoder().getPosition() * (6.75/1));
    SmartDashboard.putNumber("Error", pid.getError());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    spark4.setVoltage(pid.calculate(spark4.getEncoder().getVelocity() / 6.75, 1000));
    //spark1PID.setReference(0.25, ControlType.kDutyCycle);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
