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
    public ArrayList<Feature> lfLandmarks = new ArrayList<>();
    public ArrayList<Feature> lfNoFlyZone = new ArrayList<>();
    public ArrayList<Point> confinementArea = new ArrayList<>();

    public ArrayList<Line> lines = new ArrayList<>();
    public ArrayList<LongLat> landmarks = new ArrayList<>();

    public String webPort;


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

//    public ArrayList<Feature> returnLfLandmarks(){ return lfLandmarks; }
//
//    public ArrayList<Feature> returnLfNoFlyZone(){ return lfNoFlyZone; }


    public String getURLStringForNoFlyZones(){ return "http://localhost:" + webPort + "/buildings/no-fly-zones.geojson"; }

    public String getURLStringForLandmarks(){
        return "http://localhost:" + webPort + "/buildings/landmarks.geojson";
    }

    // Methods
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
        assert lfLandmarks != null;
        for(Feature feature: lfLandmarks) {
            Point point = (Point) feature.geometry();
            assert point != null;
            double lng = point.coordinates().get(0);
            double lat = point.coordinates().get(1);
            LongLat landmark = new LongLat(lng, lat);
            landmarks.add(landmark);
        }
    }

    public void getNoFlyZone(){
        WebConn webConn = new WebConn(webPort);
        HttpResponse<String> response = webConn.createResponse(webConn.createRequest(getURLStringForNoFlyZones()));
        FeatureCollection fc = FeatureCollection.fromJson(response.body());
        lfNoFlyZone = (ArrayList<Feature>) fc.features();
        assert lfNoFlyZone != null;
        for (Feature feature: lfNoFlyZone){  //iterate each building(Polygon)
            Polygon polygon = (Polygon) feature.geometry();
            assert polygon != null;
            for(int i = 0;i<polygon.coordinates().get(0).size()-1;i++){
                Double lng1 = polygon.coordinates().get(0).get(i).coordinates().get(0);
                Double lat1 = polygon.coordinates().get(0).get(i).coordinates().get(1);
                Double lng2 = polygon.coordinates().get(0).get(i+1).coordinates().get(0);
                Double lat2 = polygon.coordinates().get(0).get(i+1).coordinates().get(1);
                LongLat point1 = new LongLat(lng1,lat1);
                LongLat point2 = new LongLat(lng2,lat2);
                Line line = new Line(point1,point2);
                lines.add(line);
            }
        }
    }
}
