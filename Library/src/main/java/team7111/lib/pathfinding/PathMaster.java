package team7111.lib.pathfinding;

import java.lang.reflect.Array;
import java.util.function.Supplier;

import com.pathplanner.lib.util.FlippingUtil;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import team7111.robot.Constants.SwerveConstants;


public class PathMaster {
    private PIDController xPID;
    private PIDController yPID;
    private PIDController rotPID;
    
    private Supplier<Pose2d> suppliedPose;
    private Supplier<Rotation2d> gyroYaw;

    private double xCalculation;
    private double yCalculation;
    private double rotCalculation;

    private double invertedX = 1.0;
    private double invertedY = 1.0;
    private double invertedRot = 1.0;
    private double invertedGyro = 1.0;

    //Houses pointers to field elements
    private FieldElement fieldElements[];

    private boolean fieldFlipped = false;
    private boolean fieldRelative = false;
    
    public PathMaster(Supplier<Pose2d> suppliedPose, Supplier<Rotation2d> gyroYaw){

        xPID = new PIDController(1, 0, 0);
        yPID = new PIDController(1, 0, 0);

        this.rotPID = new PIDController(1, 0, 0);
        this.suppliedPose = suppliedPose;
        this.gyroYaw = gyroYaw;
    }
    
    public void useAllianceFlipping(boolean flipField)
    {
        fieldFlipped = flipField;
    }
    
    public void useFieldRelative(boolean isFieldRelative)
    {
        fieldRelative = isFieldRelative;
    }

    public void setTranslationPID(double P, double I, double D){
        xPID.setPID(P, I, D);
        yPID.setPID(P, I, D);
    }

    public void setRotationPID(double P, double I, double D){
        rotPID.setPID(P, I, D);
    }

    public void setFieldElementMap(FieldElement[] fieldElementArray){
        fieldElements = fieldElementArray;
    }

    public void setInversions(boolean invertX, boolean invertY, boolean invertRot, boolean invertGyro)
    {
        invertedX = 1.0;
        invertedY = 1.0;
        invertedRot = 1.0;
        invertedGyro = 1.0;

        if (invertX){
            invertedX = - 1.0;
        }

        if (invertY){
            invertedY = - 1.0;
        }

        if (invertRot){
            invertedRot = -1.0;
        }

        if (invertGyro){
            invertedGyro = -1.0;
            gyroYaw = ()-> Rotation2d.fromDegrees(gyroYaw.get().getDegrees() * invertedGyro) ;
        }
    }

    public void initializePath(Path path){
        path.setPoseSupplier(suppliedPose);
        path.setSpeedSuppliers(()-> xCalculation, ()-> yCalculation, ()-> rotCalculation);
    }

    public void periodic(Path path)
    {
        xCalculation = xPID.calculate(suppliedPose.get().getX(), path.getCurrentWaypoint().getPose().getX()) * invertedX;
        yCalculation = yPID.calculate(suppliedPose.get().getY(), path.getCurrentWaypoint().getPose().getY()) * invertedY;
        rotCalculation = rotPID.calculate(suppliedPose.get().getRotation().getDegrees(), path.getCurrentWaypoint().getPose().getRotation().getDegrees()) * invertedRot;
    }

    public ChassisSpeeds getPathSpeeds(Path path, boolean avoidFieldElements, boolean fieldRelative){
        // Get desired module states.
        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(path.getTranslationXSpeed(), path.getTranslationYSpeed(), path.getRotationSpeed(), gyroYaw.get())
            : new ChassisSpeeds(path.getTranslationXSpeed(), path.getTranslationYSpeed(), path.getRotationSpeed());

        return chassisSpeeds;

    }
}
