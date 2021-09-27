package uk.ac.ed.inf;

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
        if((-3.192473<this.longitude) && (this.longitude<-3.184319) && (55.942617<this.latitude)&&(this.latitude<55.946233)){
            confinement = true;
        }else{
            confinement = false;
        }
        return confinement;
    }

    public double distanceTo(LongLat longLat){
        return 2.1;
    }
}
