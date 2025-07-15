package frc.lib.pathfinding;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Path extends SubsystemBase {

    //Robot position as a supplied Pose2d
    private Supplier<Pose2d> robotPose;

    //Translation speed of the robot in each axis, and rotation.
    private DoubleSupplier xTransSpeed;
    private DoubleSupplier yTransSpeed;
    private DoubleSupplier rotTransSpeed;

    private int currentWaypointIndex = 0;
    
    private Waypoint[] waypoints;
    private boolean isPathFinished = false;

    /**
     * Constructs a path from several waypoints. Uses pathMaster class to define parameters.
     * @param waypoints -An array of waypoint objects.
     * @param robotPose -A supplier to be assigned to the local variable, letting path know robot pose.
     */
    public Path(Waypoint[] waypoints, Supplier<Pose2d> robotPose)
    {
        this.robotPose = robotPose;
        this.waypoints = waypoints;
    }

    

    /**
     * Returns in meters per second.
     */
    public double getTranslationXSpeed()
    {
        return xTransSpeed.getAsDouble();
    }

    /**
     * Returns in meters per second.
     */
    public double getRotationYSpeed()
    {
        return yTransSpeed.getAsDouble();
    }

    /**
     * Returns in digrees per second, positive clockwise.
     */
    public double getRotationSpeed()
    {
        return rotTransSpeed.getAsDouble();
    }

    /**
     * Returns the current waypoint the robot is pathing to.
     */
    public int getCurrentWaypointIndex()
    {
        return currentWaypointIndex;
    }
    
    /**
     * Returns if the path is finished or not
     */
    public boolean isPathFinished()
    {
        return isPathFinished;
    }

    /**
     * Sets the suppliers for speed on the robot to be equal to local variables.
     * @param xSpeed -X axis speed in meters per second.
     * @param ySpeed -Y axis speed in meters per second.
     * @param rotSpeed -Rotation speed in digrees per second.
     */
    public void setSpeedSuppliers(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rotSpeed)
    {
        xTransSpeed = xSpeed;
        yTransSpeed = ySpeed;
        rotTransSpeed = rotSpeed;
    }

    /**
     * indexes waypoint to path to if there. 
     * If path is finished, sets path to finished and will not path to new waypoint.
     */
    public void periodic()
    {
        if(waypoints[currentWaypointIndex].isAtWaypoint(robotPose.get()))
        {
            if(currentWaypointIndex == waypoints.length)
            {
                isPathFinished = true;
                return;
            }
            currentWaypointIndex++;
        }
    }


}
