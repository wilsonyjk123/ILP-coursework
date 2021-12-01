package uk.ac.ed.inf;

public class FlightPath {

    private final String orderNo;
    private final double fromLongitude;
    private final double fromLatitude;
    private final int angle;
    private final double toLongitude;
    private final double toLatitude;

    FlightPath(String orderNo,double fromLongitude,double fromLatitude,int angle,double toLongitude,double toLatitude){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    public String getOrderNo(){ return orderNo; }

    public double getFromLongitude(){ return fromLongitude; }

    public double getFromLatitude(){ return fromLatitude; }

    public int getAngle(){ return angle; }

    public double getToLongitude() { return toLongitude; }

    public double getToLatitude() { return toLatitude; }

}
