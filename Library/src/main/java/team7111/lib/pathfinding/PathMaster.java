package team7111.lib.pathfinding;

import java.lang.reflect.Array;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import team7111.robot.Constants.SwerveConstants;


public class PathMaster {
    PIDController translationPidController;
    ProfiledPIDController profiledTranslationPidController;
    PIDController rotationPidController;
    ProfiledPIDController profiledRotationPidController;
    Supplier<Pose2d> suppliedPose;
    Supplier<Rotation2d> gyroYaw;
    
    public PathMaster(PIDController translationPidController, ProfiledPIDController profiledTranslationPidController, PIDController rotationPidController,
        ProfiledPIDController profiledRotationPidController,  Supplier<Pose2d> suppliedPose, Supplier<Rotation2d> gyroYaw){

        this.rotationPidController = rotationPidController;
        this.profiledRotationPidController = profiledRotationPidController;
        this.translationPidController = translationPidController;
        this.profiledTranslationPidController = profiledTranslationPidController;
        this.suppliedPose = suppliedPose;
        this.gyroYaw = gyroYaw;
    }
    
    public void setTranslationPID(double P, double I, double D){
        translationPidController.setPID(P, I, D);
    }

    public void setRotationPID(double P, double I, double D){
        rotationPidController.setPID(P, I, D);
    }
    
    public void setProfliedTranslationPID(double P, double I, double D){
        profiledTranslationPidController.setPID(P, I, D);
    }

    public void setProfiledRotationPID(double P, double I, double D) {
        profiledRotationPidController.setPID(P, I, D);
    }


    public void setMotionProfile(TrapezoidProfile trapezoidProfile){

    }
    public void setFieldElementMap(FieldElement[] fieldElementArray){
        
    }
    public ChassisSpeeds getPathSpeeds(Path path, boolean avoidFieldElements, boolean fieldRelative){
        // Get desired module states.
        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(path.getTranslationXSpeed(), path.getRotationYSpeed(), path.getRotationSpeed(), gyroYaw.get())
            : new ChassisSpeeds(path.getTranslationXSpeed(), path.getRotationYSpeed(), path.getRotationSpeed());

        return chassisSpeeds;

    }
    public ChassisSpeeds getPathSpeedsProfiled(Path path, boolean avoidFieldElements, boolean fieldRelative){
        ChassisSpeeds profiledPathSpeeds = new ChassisSpeeds();
        return profiledPathSpeeds;
    }
}
