package team7111.lib.pathfinding;

import edu.wpi.first.math.geometry.Pose2d;

public class Waypoint {
    Pose2d pose;
    double translationTolerance;
    double rotationTolerance;
    double maxTranslationSpeed;
    double maxRotationSpeed;

    /**
     * Constructs a new {@code Waypoint} with the given pose and tolerances/speeds.
     * <p> - By ChatGPT-o4 2025
     *
     * @param pose The desired position and orientation of the waypoint (as a {@code Pose2d}).
     * @param translationTolerance The allowable positional error in meters for reaching this waypoint.
     * @param rotationTolerance The allowable angular error in degrees for reaching this waypoint.
     * @param maxTranslationSpeed The maximum speed in meters per second to approach this waypoint.
     * @param maxRotationSpeed The maximum rotational speed in degrees per second when turning toward this waypoint.
     */
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

        if (robotPose.getX() < (pose.getX() + translationTolerance) && robotPose.getX() > (pose.getX() - translationTolerance) ){

            if (robotPose.getY() < (pose.getY() + translationTolerance) && robotPose.getY() > (pose.getY() - translationTolerance) ){

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

    public Pose2d getPose(){
        return pose;
    }


}
