package frc.lib.pathfinding;

import edu.wpi.first.math.geometry.Pose2d;

public class Waypoint {
    Pose2d pose;
    double translationTolerance;
    double rotationTolerance;
    double maxTranslationSpeed;
    double maxRotationSpeed;

    public Waypoint(Pose2d pose,double translationTolerance,double rotationTolerance,double maxTranslationSpeed,double maxRotationSpeed){
        this.pose = pose;
        this.translationTolerance = translationTolerance;
        this.rotationTolerance = rotationTolerance;
        this.maxTranslationSpeed = maxTranslationSpeed;
        this.maxRotationSpeed = maxRotationSpeed;
    }
    
    /**
     * Checks if the robot is within the bounds of the waypoint.
     * @param robotPose - The current pose of the robot.
     */
    public boolean isAtWaypoint(Pose2d robotPose){

        if (robotPose.getX() < (pose.getX() + translationTolerance) || robotPose.getX() > (pose.getX() - translationTolerance) ){

            if (robotPose.getY() < (pose.getY() + translationTolerance) || robotPose.getY() > (pose.getY() - translationTolerance) ){

                return true;
            }
        }
        return false;
    }
    public double getMaxRotationSpeed(){
        return maxRotationSpeed;
    }
    public double getMaxTranslationSpeed(){
        return maxTranslationSpeed;
    }
}
