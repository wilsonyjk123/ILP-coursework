package uk.ac.ed.inf;

import com.mapbox.geojson.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DroneMap {
    // private fields
    private final double ATLong = -3.186874;
    private final double ATLat = 55.944494;
    private final double FHLong = -3.192473;
    private final double FHLat = 55.946233;
    private final double KFCLong = -3.184319;
    private final double KFCLat = 55.946233;
    private final double MeadowsLong = -3.192473;
    private final double MeadowsLat = 55.942617;
    private final double BuccleuchLong = -3.184319;
    private final double BuccleuchLat = 55.942617;
    private ArrayList<Feature> lfLandmarks = new ArrayList<>();
    private ArrayList<Feature> lfNoFlyZone = new ArrayList<>();
    private ArrayList<Point> confinementArea = new ArrayList<>();
    private String webPort;


    // Class Constructor
    DroneMap(String webPort){
        this.webPort = webPort;
    }

    // Getters
    public double getATLong(){ return ATLong; }

    public double getATLat(){ return ATLat; }

    public double getFHLong(){ return FHLong; }

    public double getFHLat(){ return FHLat; }

    public double getKFCLong(){ return KFCLong; }

    public double getKFCLat(){ return KFCLat; }

    public double getMeadowsLong(){ return MeadowsLong; }

    public double getMeadowsLat(){ return MeadowsLat; }

    public double getBuccleuchLong(){ return BuccleuchLong; }

    public double getBuccleuchLat(){ return BuccleuchLat; }

    public ArrayList<Feature> returnLfLandmarks(){ return lfLandmarks; }

    public ArrayList<Feature> returnLfNoFlyZone(){ return lfNoFlyZone; }

    // Methods
    public String getURLStringForNoFlyZones(){ return "http://localhost:" + webPort + "/buildings/no-fly-zones.geojson"; }

    public String getURLStringForLandmarks(){
        return "http://localhost:" + webPort + "/buildings/landmarks.geojson";
    }

    public void getConfinementArea(){
        Point pointFH = Point.fromLngLat(FHLong,FHLat);
        Point pointKFC = Point.fromLngLat(KFCLong,KFCLat);
        Point pointMeadows = Point.fromLngLat(MeadowsLong,MeadowsLat);
        Point pointBuccleuch = Point.fromLngLat(BuccleuchLong,BuccleuchLat);
        confinementArea = new ArrayList<>();
        confinementArea.add(pointFH);
        confinementArea.add(pointKFC);
        confinementArea.add(pointMeadows);
        confinementArea.add(pointBuccleuch);
    }

    public void getLandMarks(){
        WebConn webConn = new WebConn(webPort);
        HttpResponse<String> response = webConn.createResponse(webConn.createRequest(getURLStringForLandmarks()));
        FeatureCollection fc = FeatureCollection.fromJson(response.body());
        lfLandmarks = (ArrayList<Feature>) fc.features();
        System.out.println(lfLandmarks);
    }

    public void getNoFlyZone(){
        WebConn webConn = new WebConn(webPort);
        HttpResponse<String> response = webConn.createResponse(webConn.createRequest(getURLStringForNoFlyZones()));
        FeatureCollection fc = FeatureCollection.fromJson(response.body());
        lfNoFlyZone = (ArrayList<Feature>) fc.features();
        System.out.println(lfNoFlyZone);
    }
}
