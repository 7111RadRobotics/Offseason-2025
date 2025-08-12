package team7111.lib.pathfinding;

import java.lang.reflect.Array;
import java.util.function.Supplier;

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

    private ProfiledPIDController profiledXPID;
    private ProfiledPIDController profiledYPID;
    private ProfiledPIDController profiledRotPID;

    
    private Supplier<Pose2d> suppliedPose;
    private Supplier<Rotation2d> gyroYaw;

    private double xCalculation;
    private double yCalculation;
    private double rotCalculation;

    private double profiledXCalculation;
    private double profiledYCalculation;
    private double profiledRotCalculation;

    private double invertedX = 1.0;
    private double invertedY = 1.0;
    private double invertedRot = 1.0;
    private double invertedGyro = 1.0;
    
    public PathMaster(Supplier<Pose2d> suppliedPose, Supplier<Rotation2d> gyroYaw){

        xPID = new PIDController(1, 0, 0);
        yPID = new PIDController(1, 0, 0);
        profiledXPID = new ProfiledPIDController(1, 0, 0, null);
        profiledYPID = new ProfiledPIDController(1, 0, 0, null);

        this.rotPID = new PIDController(1, 0, 0);
        this.profiledRotPID = new ProfiledPIDController(1, 0, 0, null);
        this.suppliedPose = suppliedPose;
        this.gyroYaw = gyroYaw;
    }
    
    public void useAllianceFlipping(boolean flipField)
    {

    }
    
    public void useFieldRelative(boolean isFieldRelative)
    {

    }

    public void setTranslationPID(double P, double I, double D){
        xPID.setPID(P, I, D);
        yPID.setPID(P, I, D);
    }

    public void setRotationPID(double P, double I, double D){
        rotPID.setPID(P, I, D);
    }

    public void setProfliedTranslationPID(double P, double I, double D){
        profiledXPID.setPID(P, I, D);
        profiledYPID.setPID(P, I, D);
    }

    public void setProfiledRotationPID(double P, double I, double D) {
        profiledRotPID.setPID(P, I, D);
    }

    public void setFieldElementMap(FieldElement[] fieldElementArray){
        
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

    public void initializePathProfiled(Path path){

    }

    public void periodic(Path path)
    {
        xCalculation = xPID.calculate(suppliedPose.get().getX(), path.getCurrentWaypoint().getPose().getX()) * invertedX;
        yCalculation = yPID.calculate(suppliedPose.get().getY(), path.getCurrentWaypoint().getPose().getY()) * invertedY;
        rotCalculation = rotPID.calculate(suppliedPose.get().getRotation().getDegrees(), path.getCurrentWaypoint().getPose().getRotation().getDegrees()) * invertedRot;

        profiledXCalculation = 0;
        profiledYCalculation = 0;
        profiledRotCalculation = 0;
    }

    public ChassisSpeeds getPathSpeeds(Path path, boolean avoidFieldElements, boolean fieldRelative){
        // Get desired module states.
        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(path.getTranslationXSpeed(), path.getTranslationYSpeed(), path.getRotationSpeed(), gyroYaw.get())
            : new ChassisSpeeds(path.getTranslationXSpeed(), path.getTranslationYSpeed(), path.getRotationSpeed());

        return chassisSpeeds;

    }
    public ChassisSpeeds getPathSpeedsProfiled(Path path, boolean avoidFieldElements, boolean fieldRelative){
        ChassisSpeeds profiledPathSpeeds = new ChassisSpeeds();
        return profiledPathSpeeds;
    }
}
