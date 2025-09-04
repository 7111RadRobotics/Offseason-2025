package frc.robot;

public class Lift {
    
    private double savedNumber = 0;

    public Lift(double num){
        savedNumber = num;
    }

    public void printNumber(){
        System.out.println(savedNumber);
    }
}
