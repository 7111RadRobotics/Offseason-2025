package team7111.lib.pathfinding;


/**
 * Class to hold waypoint's constraints
 */
public class WaypointConstraints
{
    private double maxTransSpeed;
    private double minTransSpeed;
    /**In digrees per second, defualted to 360, or 1rpm*/
    private double maxRotSpeed = 360;
    /**In meters*/
    private double transTolerance;
    /**In digrees, defaulted to a full 360*/
    private double rotTolerance = 360;

    /**
     * Constructs the waypoint parameter object. Sets all values.
     * @param maxTransSpeed -Maximum translation speed in meters per second.
     * @param minTransSpeed -Minimum translation speed the robot must be going during both travel and end of waypoint.
     * @param maxRotSpeed -Maximum rotation speed in digrees per second.
     * @param transTolerance -Translation tolerance to the waypoint the robot must be to be considered done.
     * @param rotTolerance -Rotation tolerance the robot must have to the waypoint's rotation.
     */
    public WaypointConstraints(double maxTransSpeed, double minTransSpeed, double maxRotSpeed, double transTolerance, double rotTolerance)
    {
        this.maxTransSpeed = maxTransSpeed;
        this.minTransSpeed = minTransSpeed;
        this.maxRotSpeed = maxRotSpeed;
        this.transTolerance = transTolerance;
        this.rotTolerance = rotTolerance;
    }

    /**
     * Constructs the waypoint parameter object. Sets just translation values.
     * <p> Defaults rotation parameters to: Rotation tolerance is 360 (full circle), and max rotation speed is 1rpm.
     * @param maxTransSpeed -Maximum translation speed in meters per second.
     * @param minTransSpeed -Minimum translation speed the robot must be going during both travel and end of waypoint.
     * @param transTolerance -Translation tolerance to the waypoint the robot must be to be considered done.
     */
    public WaypointConstraints(double maxTransSpeed, double minTransSpeed, double transTolerance)
    {
        this.maxTransSpeed = maxTransSpeed;
        this.minTransSpeed = minTransSpeed;
        this.transTolerance = transTolerance;
    }

    /**
     * Returns the rotation tolerance the robot needs to be to the waypoint in digrees.
     */
    public double getRotationTolerance()
    {
        return this.rotTolerance;
    }

    /**
     * Returns the translation tolerance the robot needs to be to the waypoint as a radius in meters.
     */
    public double getTransTolerance()
    {
        return this.transTolerance;
    }


    /**
     * Returns maximum allowed translation speed in meters per second.
     */
    public double getMaxTranslationSpeed()
    {
        return this.maxTransSpeed;
    }

    /**
     * Returns minimum allowed translation speed in meters per second.
     */
    public double getMinTranslationSpeed()
    {
        return this.minTransSpeed;
    }

    /**
     * Returns maximum allowed rotation speed in digrees per second.
     */
    public double getMaxRotationSpeed()
    {
        return maxRotSpeed;
    }

};