package team7111.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import team7111.lib.pathfinding.Path;
import team7111.lib.pathfinding.Waypoint;
import team7111.lib.pathfinding.WaypointConstraints;

public class PathSubsystem {
    
    private Path currentPath;

    /**
     * Defaults current path to be null.
     */
    public PathSubsystem() 
    {
        currentPath = null;
    }

    public enum Paths {
        home,
        straight,
    };

 

    public Path getPath(Paths pathName) {
        Waypoint[] waypoints;

        switch (pathName) {
            case home:
                waypoints = new Waypoint[]{};
                break;
            case straight:
                waypoints = new Waypoint[] {
                    new Waypoint(
                        new Pose2d(0, 1, new Rotation2d()), 
                        new WaypointConstraints(5, 0, 0.1), 
                        new WaypointConstraints(360, 0, 5)
                    ),
                };
                break;
            default:
                waypoints = new Waypoint[]{};
        }

        return new Path(waypoints);
    }


    /**
     * Calls periodic method for the current path.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
        currentPath.periodic();
    }
}
