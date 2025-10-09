package team7111.robot.utils.swerve.config;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class SwerveMotorConfig {
    public DCMotor dcMotor;
    public int id;
    public boolean isCCW;
    public boolean isBreakMode;
    public double gearRatio;
    public double moi;
    public int currentLimit;
    public PIDController pid;
    public SimpleMotorFeedforward ff;
    public SparkBaseConfig sparkMaxConfig;
    public TalonFXConfiguration talonFXConfig;

    public SwerveMotorConfig(DCMotor dcMotor, int id, boolean isCCW, boolean isBreakMode, double gearRatio, double moi, 
            int currentLimit, PIDController pid, SimpleMotorFeedforward ff){
                this.dcMotor = dcMotor;
                this.id = id;
                this.isCCW = isCCW;
                this.isBreakMode = isBreakMode;
                this.gearRatio = gearRatio;
                this.moi = moi;
                this.currentLimit = currentLimit;
                this.pid = pid;
                this.ff = ff;
            
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            /*sparkMaxConfig = isBreakMode
                ? sparkMaxConfig.idleMode(IdleMode.kBrake)
                : sparkMaxConfig.idleMode(IdleMode.kCoast);
            sparkMaxConfig
                .inverted(isBreakMode)
                .smartCurrentLimit(currentLimit);
            sparkMaxConfig.closedLoop
                .p(pid.getP())
                .i(pid.getI())
                .d(pid.getD())
                .velocityFF(ff.getKv());*/
=======
=======
>>>>>>> Stashed changes
            
    }
    
    public SwerveMotorConfig(int id, SwerveMotorConfig motorConfig) {
        this(motorConfig.dcMotor, id, motorConfig.isCCW, motorConfig.isBreakMode, motorConfig.gearRatio,
             motorConfig.moi, motorConfig.currentLimit, motorConfig.pid, motorConfig.ff);
    }
    //hi gidho[ te dpdmrv]
    public SparkMaxConfig getSparkMaxConfig() {
        SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
        sparkMaxConfig.closedLoopRampRate(0);
        
        sparkMaxConfig.absoluteEncoder.positionConversionFactor(gearRatio);  
        
        if (isBreakMode) {
            sparkMaxConfig.idleMode(IdleMode.kBrake);
        }
        else {sparkMaxConfig.idleMode(IdleMode.kCoast);}
            

        sparkMaxConfig.inverted(isCCW)
            .smartCurrentLimit(currentLimit);

        sparkMaxConfig.closedLoop.pid(pid.getP(), pid.getI(), pid.getD())
            .velocityFF(ff.getKv());


        return sparkMaxConfig;
    }

    public TalonFXConfiguration geTalonFXConfiguration() {
        TalonFXConfiguration talonFXConfig = new TalonFXConfiguration();
        talonFXConfig.Feedback.SensorToMechanismRatio = gearRatio;
        talonFXConfig.CurrentLimits.StatorCurrentLimit = currentLimit;
        talonFXConfig.Slot0.kP = pid.getP();
        talonFXConfig.Slot0.kI = pid.getI();
        talonFXConfig.Slot0.kD = pid.getD();
        talonFXConfig.Slot0.kV = ff.getKv();
        talonFXConfig.MotorOutput.Inverted = isCCW
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive;
        talonFXConfig.MotorOutput.NeutralMode = isBreakMode
            ? NeutralModeValue.Brake
            : NeutralModeValue.Coast;

        return talonFXConfig;

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }

    

    
}