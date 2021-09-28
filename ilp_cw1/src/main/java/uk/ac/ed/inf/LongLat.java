package uk.ac.ed.inf;
import java.lang.Math;

public class LongLat {
    /*two public double fields named longitude and latitude*/
    public double longitude;
    public double latitude;
    /*The constructor that accepts two double-precision numbers*/
    LongLat(double longitude,double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
    /*The LongLat class should have a no-parameter method called isConfined which returns true if the point is within the drone confinement area and false if it is not.*/
    public boolean isConfined(){
        boolean confinement;
        confinement = (-3.192473 < longitude) && (longitude < -3.184319) && (55.942617 < latitude) && (latitude < 55.946233);
        return confinement;
    }
    /*Return the Pythagorean distance between the current point and the given point*/
    public double distanceTo(LongLat longLat){
        return Math.sqrt(Math.pow((latitude-longLat.latitude),2) + Math.pow((longitude-longLat.longitude),2));
    }
    /*Returns true if the points are close to each other and false otherwise*/
    public boolean closeTo(LongLat longLat){
        boolean isCloseTo;
        isCloseTo = distanceTo(longLat) < 0.00015;
        return isCloseTo;
    }
    /*takes an int angle as a parameter and returns a LongLat object that represents the new position of drone if it makes a move in the direction of the angle*/
    public LongLat nextPosition(int angle){
        if((angle <= 350) && (angle >= 0) && (angle%10 == 0)){
            /*Convert degree to radian*/
            double angleRadian = Math.toRadians(angle);

            /*Calculate the new longitude and latitude*/
            double newLongitude = longitude + 0.00015 * Math.cos(angleRadian);
            double newLatitude = latitude + 0.00015 * Math.sin(angleRadian);

            /*Return a LongLat object with the new longitude and latitude*/
            return new LongLat(newLongitude,newLatitude);
        }else if(angle == -999){
            /*Return a LongLat object with the original longitude and latitude*/
            return new LongLat(longitude,latitude);
        }else{
            /*throw an exception if the input angle is invalid*/
            throw new IllegalArgumentException("The input angle should be a multiples of ten between 0 and 350");
        }
    }

}
