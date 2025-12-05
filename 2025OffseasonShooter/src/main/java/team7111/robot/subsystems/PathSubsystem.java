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

    public enum paths {
        home,
    };

    /**
     * Sets current path using an enum for presets to choose from.
     */
    public void setCurrentPath(paths path) {
        switch (path) {
            case home:
            /*
                Waypoint waypoints[] = {
                new Waypoint(new Pose2d(0, 0, new Rotation2d(0)), new WaypointConstraints(1, 0, 0), new WaypointConstraints(1, 0, 0)),
            };
                currentPath = new Path(waypoints);*/
                currentPath = null;
                break;
            default:
                currentPath = null;
                break;
        }
    }

    /**
     * Sets the current path to be a custom path rather than a preset.
     */
    public void setCustomPath(Path path) {
        currentPath = path;
    }


    /**
     * Calls periodic method for the current path.
     * <p>
     * Returns false if an error has occurred.
     */
    public void periodic() {
    }
}
