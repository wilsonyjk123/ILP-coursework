package uk.ac.ed.inf;
import java.lang.Math;

public class LongLat {
    // Public fields
    public double longitude;
    public double latitude;

    /**
     * LongLat constructor
     *
     * @param longitude - Longitude is the measurement east or west of the prime meridian
     * @param latitude  - Latitude is the measurement of distance north or south of the Equator
     */
    LongLat(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Calculate the distance between the current point and the given point
     *
     * @param longLat   - a LongLat object that contains the position of the next point
     * @return the Pythagorean distance between the current point and the given point
     */
    public double distanceTo(LongLat longLat){
        return Math.sqrt(Math.pow((latitude-longLat.latitude),2) + Math.pow((longitude-longLat.longitude),2));
    }

    /**
     * Determine whether the distance between the two points is in the given tolerance value
     *
     * @param longLat   - a LongLat object that contains the position of the next point
     * @return true if the points are close to each other with the given tolerance value and vice versa
     */
    public boolean closeTo(LongLat longLat){
        return (distanceTo(longLat) < 0.00015);
    }

    /**
     * Give the new position of the drone after a movement is executed
     *
     * @param angle - A representation of the direction of the drone's next movement
     * @return a LongLat object that represents the new position of drone
     */
    public LongLat nextPosition(int angle){
        // Check if the parameter is in a valid form base on the given rules
        if((angle <= 350) && (angle >= 0) && (angle%10 == 0)){
            // Convert degree to radian
            double angleRadian = Math.toRadians(angle);

            // Calculate the new longitude and latitude
            double newLongitude = longitude + 0.00015 * Math.cos(angleRadian);
            double newLatitude = latitude + 0.00015 * Math.sin(angleRadian);

            // Return a LongLat object with the new longitude and latitude
            return new LongLat(newLongitude,newLatitude);
        }else if(angle == -999){
            // Return a LongLat object with the original longitude and latitude since it performs a hovering
            return new LongLat(longitude,latitude);
        }else{
            // Throw an exception if the input angle is invalid
            throw new IllegalArgumentException("The input angle should be a multiples of ten between 0 and 350 or -999 for representing hovering");
        }
    }

}
