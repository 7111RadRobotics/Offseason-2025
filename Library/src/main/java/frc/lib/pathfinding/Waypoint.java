package frc.lib.pathfinding;

import edu.wpi.first.math.geometry.Pose2d;

public class Waypoint {
        Pose2d pose = pos;
        double translationTolerance = transTolerance;
        double rotationTolerance = rotTolerance;
        double maxTranslationSpeed = maxTransSpeed;
        double maxRotationSpeed = maxRotSpeed;
    }
    
    
    boolean isAtWaypoint(Pose2d robotPose){
        if (robotPose < pose + translationTolerance || robotPose > pose - translationTolerance){
            
        }
    }
}
