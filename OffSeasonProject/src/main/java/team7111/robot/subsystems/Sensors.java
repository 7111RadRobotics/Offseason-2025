package team7111.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

class Sensors {

    Encoder shooterEncoder;
    DigitalInput beambrake;

    /**
     * Returns current state of the beambreak sensor.
     * <p>
     * Returns false if broken.
     */
    public boolean getBeam() {
        return beambrake.get();
    }

    /**
     * Periodic function for sensors.
     * <p>
     * Returns false if an error has occurred.
     */
    public boolean periodic() {
        return true;
    }
}