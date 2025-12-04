package team7111.robot.utils.encoder;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;

public class ThroughBore implements GenericEncoder {

      Encoder m_encoder = new Encoder(0, 1);

    private Rotation2d zeroOffset = new Rotation2d();

    public ThroughBore(double conversionFactor) {
        m_encoder.setDistancePerPulse(conversionFactor);
    }

    @Override
    public Rotation2d getPosition() {
       return Rotation2d.fromDegrees(m_encoder.get());
    }

    @Override
    public void setPosition(Rotation2d desired) {
        m_encoder.reset();
    }
}
