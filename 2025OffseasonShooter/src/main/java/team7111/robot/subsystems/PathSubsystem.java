package team7111.robot.subsystems;

import team7111.lib.pathfinding.Path;

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
