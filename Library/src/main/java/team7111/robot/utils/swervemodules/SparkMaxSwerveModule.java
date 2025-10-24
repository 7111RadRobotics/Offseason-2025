package team7111.robot.utils.swervemodules;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team7111.robot.Constants.SwerveConstants;
import team7111.robot.utils.config.SwerveModuleConfig;
import team7111.robot.utils.encoder.GenericEncoder;

public class SparkMaxSwerveModule implements GenericSwerveModule {

    private GenericEncoder encoder;
    private double encoderOffsetDegrees;

    private SparkMax driveMotor;
    private SparkBaseConfig driveMotorConfig;
    private RelativeEncoder driveEncoder;
    private SparkClosedLoopController drivePID;
    private SimpleMotorFeedforward driveFeedforward;
    private double driveGearRatio;

    private SparkMax angleMotor;
    private SparkBaseConfig angleMotorConfig;
    private RelativeEncoder angleEncoder;
    private SparkClosedLoopController anglePID;
    private double angleGearRatio;

    public SparkMaxSwerveModule(SwerveModuleConfig config){
        this.encoder = config.encoder;
        encoderOffsetDegrees = config.canCoderOffsetDegrees;

        driveMotorConfig = config.driveMotor.getSparkMaxConfig();
        angleMotorConfig = config.angleMotor.getSparkMaxConfig();
        driveMotor = new SparkMax(config.driveMotor.id, MotorType.kBrushless);
        driveEncoder = driveMotor.getEncoder();
        driveFeedforward = config.driveMotor.ff;//new SimpleMotorFeedforward(SwerveConstants.driveKS, SwerveConstants.driveKV, SwerveConstants.driveKA);
        drivePID = driveMotor.getClosedLoopController();
        driveGearRatio = config.driveMotor.gearRatio;

        angleMotor = new SparkMax(config.angleMotor.id, MotorType.kBrushless);
        angleEncoder = angleMotor.getEncoder();
        anglePID = angleMotor.getClosedLoopController(); 
        angleGearRatio = config.angleMotor.gearRatio;
    }

    @Override
    public void setOpenDriveState(SwerveModuleState state) {
        double speed = state.speedMetersPerSecond / SwerveConstants.maxDriveVelocity;
        drivePID.setReference(speed, SparkMax.ControlType.kDutyCycle);
    }

    @Override
    public void setClosedDriveState(SwerveModuleState state) {
        double speedRPM = (state.speedMetersPerSecond * driveGearRatio * 60.0) / SwerveConstants.wheelCircumference;
        SmartDashboard.putNumber("converted velocity", speedRPM);
        SmartDashboard.putNumber("received state", state.speedMetersPerSecond);
        SmartDashboard.putNumber("reconverted state", speedRPM * SwerveConstants.wheelCircumference / (60.0 * driveGearRatio));
        drivePID.setReference(speedRPM, SparkMax.ControlType.kVelocity, ClosedLoopSlot.kSlot0, driveFeedforward.calculate(speedRPM));
    }

    @Override
    public double getDriveVelocity() {
        double velocity = driveEncoder.getVelocity() * SwerveConstants.wheelCircumference / (60.0 * driveGearRatio);
        return velocity;
    }

    @Override
    public double getDrivePosition() {
        double distance = driveEncoder.getPosition() * SwerveConstants.wheelCircumference / driveGearRatio;
        return distance;
    }

    @Override
    public void setAngle(Rotation2d angle){
        anglePID.setReference(angle.getRotations() * angleGearRatio, SparkMax.ControlType.kPosition);
    }

    @Override
    public Rotation2d getAngle() {
        return Rotation2d.fromRotations(angleEncoder.getPosition() / angleGearRatio);
    }

    @Override
    public GenericEncoder getEncoder(){
        return encoder;
    }

    @Override
    public void zeroWheels(){
        angleEncoder.setPosition(Units.degreesToRotations((encoder.getPosition().getDegrees()) - encoderOffsetDegrees));
    }

    public void configure(){
        //driveMotorConfig.encoder.positionConversionFactor(1);
        //driveMotorConfig.encoder.velocityConversionFactor(1);
        //driveMotorConfig.absoluteEncoder.positionConversionFactor(1);
        //driveMotorConfig.absoluteEncoder.velocityConversionFactor(1);
        angleMotorConfig.closedLoop.positionWrappingEnabled(true);
        //angleMotorConfig.closedLoop.outputRange(-0.5, 0.5);
        angleMotorConfig.closedLoop.positionWrappingInputRange(-0.5 * angleGearRatio, 0.5 * angleGearRatio);
        driveMotor.configure(driveMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        angleMotor.configure(angleMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    }
}
