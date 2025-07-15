package frc.lib.pathfinding;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import edu.wpi.first.math.geometry.Pose2d;

public class Path {

    //Robot position as a supplied Pose2d
    private Supplier<Pose2d> robotPose;

    //Translation speed of the robot in each axis, and rotation.
    private DoubleSupplier xTransSpeed;
    private DoubleSupplier yTransSpeed;
    private DoubleSupplier rotTransSpeed;

    private int currentWaypointIndex = 0;
    
    private Waypoint[] waypoints;
    

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
     * Gets the current waypoint the robot is pathing to.
     */
    public int getCurrentWaypointIndex()
    {
        return currentWaypointIndex;
    }
    

    /**
     * Sets the suppliers for speed on the robot to be equal to local variables.
     * @param xSpeed -X axis speed in meters per second.
     * @param ySpeed -Y axis speed in meters per second.
     * @param rotSpeed -Rotation speed in digrees per second.
     */
    public void SetSpeedSuppliers(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rotSpeed)
    {
        xTransSpeed = xSpeed;
        yTransSpeed = ySpeed;
        rotTransSpeed = rotSpeed;
    }




}
