package team7111.robot.utils.config;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import team7111.robot.utils.encoder.CTREEncoder;
import team7111.robot.utils.swervemodules.GenericSwerveModule;
import team7111.robot.utils.swervemodules.SimSwerveModule;
import team7111.robot.utils.swervemodules.SparkMaxSwerveModule;
import team7111.robot.utils.swervemodules.TalonFXSwerveModule;

public class DrivebaseConfig {
    
    public GenericSwerveModule[] moduleTypes;
    public SwerveModuleConfig[] moduleConstants;
    public double width;
    public double length;
    public double wheelDiameter;
    public SwerveMotorConfig swerveMotorConfig;

    public DrivebaseConfig(
        GenericSwerveModule[] moduleTypes, SwerveModuleConfig[] moduleConstants, 
        double width, double length, double wheelDiameter
    ){
        this.moduleTypes = moduleTypes;
        this.moduleConstants = moduleConstants;
        this.width = width;
        this.length = length;
        this.wheelDiameter = wheelDiameter;
    }

    /**
     * Hi simon :)
     */
    public static DrivebaseConfig getSoundWave(boolean isSim){

        double width = Units.inchesToMeters(28);
        double length = Units.inchesToMeters(28);
        double wheelDiameter = Units.inchesToMeters(4);

        double driveGearing = 6.75 / 1.0;
        double angleGearing = 6.75 / 1.0;
        double driveMOI = 0.019835507; //weight of robot in pounds 56.1
        double angleMOI = 0.0000000001;
        int driveCurrentLimit = 40;
        int angleCurrentLimit = 40;
        boolean driveInversion = false;
        boolean angleInversion = true;
        boolean driveBrakeMode = true;
        boolean angleBrakeMode = false;
        PIDController drivePID = new PIDController(5, 0.0, 0.0);
        PIDController anglePID = new PIDController(5, 0.0, 0.0);
        SimpleMotorFeedforward driveFF = new SimpleMotorFeedforward(0, 0 /*0.001, 0.0*/);
        SimpleMotorFeedforward angleFF = new SimpleMotorFeedforward(0, 0 /*0.001, 0.0*/);

        SwerveMotorConfig driveMotorConfig = new SwerveMotorConfig(DCMotor.getNEO(1), driveInversion, driveBrakeMode, driveGearing, driveMOI, driveCurrentLimit, drivePID, driveFF);
        SwerveMotorConfig angleMotorConfig = new SwerveMotorConfig(DCMotor.getNEO(1), angleInversion, angleBrakeMode, angleGearing, angleMOI, angleCurrentLimit, anglePID, angleFF);
        CANcoderConfiguration encoderConfig = new CANcoderConfiguration().withMagnetSensor(new MagnetSensorConfigs().withSensorDirection(SensorDirectionValue.CounterClockwise_Positive));

        double canCoder1Offset = isSim
            ? 0
            : 134.4;
        double canCoder2Offset = isSim
            ? 0
            : 91.4;
        double canCoder3Offset = isSim
            ? 0
            : 128.49;
        double canCoder4Offset = isSim
            ? 0
            : 75.49;

        SwerveModuleConfig[] moduleConstants = new SwerveModuleConfig[]{
            new SwerveModuleConfig(
                new SwerveMotorConfig(3, driveMotorConfig), 
                new SwerveMotorConfig(4, angleMotorConfig), 
                new CTREEncoder(2, encoderConfig), canCoder2Offset),

            new SwerveModuleConfig(
                new SwerveMotorConfig(5, driveMotorConfig), 
                new SwerveMotorConfig(6, angleMotorConfig), 
                new CTREEncoder(3, encoderConfig), canCoder3Offset),

            new SwerveModuleConfig(
                new SwerveMotorConfig(1, driveMotorConfig), 
                new SwerveMotorConfig(2, angleMotorConfig), 
                new CTREEncoder(1, encoderConfig), canCoder1Offset),

            new SwerveModuleConfig(
                new SwerveMotorConfig(7, driveMotorConfig), 
                new SwerveMotorConfig(8, angleMotorConfig), 
                new CTREEncoder(4, encoderConfig), canCoder4Offset),
        };

        GenericSwerveModule[] moduleTypes;
        if(isSim){
            moduleTypes = new GenericSwerveModule[]{
                new SimSwerveModule(moduleConstants[0]),
                new SimSwerveModule(moduleConstants[1]),
                new SimSwerveModule(moduleConstants[2]),
                new SimSwerveModule(moduleConstants[3]),
            };
        }else{
            moduleTypes = new GenericSwerveModule[]{
                new SparkMaxSwerveModule(moduleConstants[0]),
                new SparkMaxSwerveModule(moduleConstants[1]),
                new SparkMaxSwerveModule(moduleConstants[2]),
                new SparkMaxSwerveModule(moduleConstants[3]),
            };
        }
        
        return new DrivebaseConfig(moduleTypes, moduleConstants, width, length, wheelDiameter);
    }
    public static DrivebaseConfig getStormSurge(boolean isSim){
        double width = Units.inchesToMeters(21.25);
        double length = Units.inchesToMeters(23.25);
        double wheelDiameter = Units.inchesToMeters(3.75);

        double driveGearing = 6.72 / 1.0;
        double angleGearing = 468.0 / 35.0;
        double driveMOI = 0.25;
        double angleMOI = 0.001;
        int driveCurrentLimit = 80;
        int angleCurrentLimit = 40;
        boolean driveInversion = false;
        boolean angleInversion = true;
        boolean driveBrakeMode = true;
        boolean angleBrakeMode = false;
        PIDController drivePID = new PIDController(50, 0.0, 0.0);
        PIDController anglePID = new PIDController(50, 0.0, 0.0);
        SimpleMotorFeedforward driveFF = new SimpleMotorFeedforward(0.001, 0.0);
        SimpleMotorFeedforward angleFF = new SimpleMotorFeedforward(0.001, 0.0);
        SwerveMotorConfig driveMotorConfig = new SwerveMotorConfig(DCMotor.getKrakenX60(1), driveInversion, driveBrakeMode, driveGearing, driveMOI, driveCurrentLimit, drivePID, driveFF);
        SwerveMotorConfig angleMotorConfig = new SwerveMotorConfig(DCMotor.getKrakenX60(1), angleInversion, angleBrakeMode, angleGearing, angleMOI, angleCurrentLimit, anglePID, angleFF);
        CANcoderConfiguration encoderConfig = new CANcoderConfiguration().withMagnetSensor(
            new MagnetSensorConfigs().withSensorDirection(SensorDirectionValue.CounterClockwise_Positive));

        SwerveModuleConfig[] moduleConstants = new SwerveModuleConfig[]{
            new SwerveModuleConfig(
                new SwerveMotorConfig(1, driveMotorConfig),
                new SwerveMotorConfig(2, angleMotorConfig),
                new CTREEncoder(1, encoderConfig), 0),

            new SwerveModuleConfig(
                new SwerveMotorConfig(3, driveMotorConfig), 
                new SwerveMotorConfig(4, angleMotorConfig), 
                new CTREEncoder(2, encoderConfig), 0),

            new SwerveModuleConfig(
                new SwerveMotorConfig(5, driveMotorConfig), 
                new SwerveMotorConfig(6, angleMotorConfig), 
                new CTREEncoder(3, encoderConfig), 0),

            new SwerveModuleConfig(
                new SwerveMotorConfig(7, driveMotorConfig), 
                new SwerveMotorConfig(8, angleMotorConfig), 
                new CTREEncoder(4, encoderConfig), 0),
        };

        GenericSwerveModule[] moduleTypes;
        if(isSim){
            moduleTypes = new GenericSwerveModule[]{
                new SimSwerveModule(moduleConstants[0]),
                new SimSwerveModule(moduleConstants[1]),
                new SimSwerveModule(moduleConstants[2]),
                new SimSwerveModule(moduleConstants[3]),
            };
        }else{
            moduleTypes = new GenericSwerveModule[]{
                new TalonFXSwerveModule(moduleConstants[0]),
                new TalonFXSwerveModule(moduleConstants[1]),
                new TalonFXSwerveModule(moduleConstants[2]),
                new TalonFXSwerveModule(moduleConstants[3]),
            };
        }
        
        return new DrivebaseConfig(moduleTypes, moduleConstants, width, length, wheelDiameter);
    }

    public static DrivebaseConfig getBoxChassis(){
        GenericSwerveModule[] moduleTypes = new GenericSwerveModule[]{
            
        };
        SwerveModuleConfig[] moduleConstants = new SwerveModuleConfig[]{

        };
        double width = 0;
        double length = 0;
        double wheelDiameter = 4;
        double moi = 0.001;

        return new DrivebaseConfig(moduleTypes, moduleConstants, width, length, wheelDiameter);
    }
}
