package team7111.lib.pathfinding;

import java.lang.reflect.Array;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class PathMaster {
    PIDController xTranslationPidController;
    PIDController yTranslationPidController;
    PIDController rotationPidController;

    ProfiledPIDController profiledXTranslationPidController;
    ProfiledPIDController profiledYTranslationPidController;
    ProfiledPIDController profiledRotationPidController;

    
    Supplier<Pose2d> suppliedPose;
    public PathMaster(PIDController translationPidController, ProfiledPIDController profiledTranslationPidController, PIDController rotationPidController,
        ProfiledPIDController profiledRotationPidController,  Supplier<Pose2d> suppliedPose){

        xTranslationPidController = translationPidController;
        yTranslationPidController = translationPidController;
        profiledXTranslationPidController = profiledTranslationPidController;
        profiledYTranslationPidController = profiledTranslationPidController;

        this.rotationPidController = rotationPidController;
        this.profiledRotationPidController = profiledRotationPidController;
        this.suppliedPose = suppliedPose;
    }
    
    public void setTranslationPID(double P, double I, double D){
        xTranslationPidController.setPID(P, I, D);
        yTranslationPidController.setPID(P, I, D);
    }

    public void setRotationPID(double P, double I, double D){
        rotationPidController.setPID(P, I, D);
    }

    public void setProfliedTranslationPID(double P, double I, double D){
        profiledXTranslationPidController.setPID(P, I, D);
        profiledYTranslationPidController.setPID(P, I, D);
    }

    public void setProfiledRotationPID(double P, double I, double D) {
        profiledRotationPidController.setPID(P, I, D);
    }


    public void setMotionProfile(TrapezoidProfile trapezoidProfile){

    }
    public void setFieldElementMap(FieldElement[] fieldElementArray){
        
    }
    public ChassisSpeeds getPathSpeeds(Path path, boolean avoidFieldElements, boolean fieldRelative){
        ChassisSpeeds pathSpeeds = new ChassisSpeeds();
        return pathSpeeds;
    }
    public ChassisSpeeds getPathSpeedsProfiled(Path path, boolean avoidFieldElements, boolean fieldRelative){
        ChassisSpeeds profiledPathSpeeds = new ChassisSpeeds();
        return profiledPathSpeeds;
    }
}
