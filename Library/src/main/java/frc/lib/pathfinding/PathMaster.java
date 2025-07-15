package frc.lib.pathfinding;

import java.lang.reflect.Array;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class PathMaster {
    PIDController pathPidController;
    ProfiledPIDController profiledPathPidController;
    Supplier<Pose2d> suppliedPose;
    public PathMaster(PIDController pathPidController, ProfiledPIDController profiledPathPidController, Supplier<Pose2d> suppliedPose){
        this.pathPidController = pathPidController;
        this.profiledPathPidController = profiledPathPidController;
        this.suppliedPose = suppliedPose;
    }
    public void setPID(double P, double I, double D){
        pathPidController.setPID(P,I,D);
    }
    public void setProfliedPID(double P, double I, double D){
        profiledPathPidController.setPID(P, I, D);
    }
    public void setMotionProfile(TrapezoidProfile trapezoidProfile){

    }
    public void setFieldElementMap(FieldElement[] fieldElementArray){
        
    }
    public ChassisSpeeds getPathSpeeds(Path path, boolean avoidFieldElements, boolean simonmayremember){
        ChassisSpeeds pathSpeeds = new ChassisSpeeds();
        return pathSpeeds;
    }
    public ChassisSpeeds getPathSpeedsProfiled(Path path, boolean avoidFieldElements, boolean simonmayremember){
        ChassisSpeeds profiledPathSpeeds = new ChassisSpeeds();
        return profiledPathSpeeds;
    }
}
