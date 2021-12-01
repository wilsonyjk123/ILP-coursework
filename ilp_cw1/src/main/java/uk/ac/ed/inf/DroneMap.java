package uk.ac.ed.inf;

import com.mapbox.geojson.*;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DroneMap {
    private final double FHLong = -3.192473;
    private final double FHLat = 55.946233;
    private final double KFCLong = -3.184319;
    private final double KFCLat = 55.946233;
    private final double MeadowsLong = -3.192473;
    private final double MeadowsLat = 55.942617;
    private final double BuccleuchLong = -3.184319;
    private final double BuccleuchLat = 55.942617;
    public ArrayList<Feature> lfLandmarks = new ArrayList<>();
    public ArrayList<Point> confinementArea = new ArrayList<>();
    public ArrayList<LongLat> landmarks = new ArrayList<>();
    ArrayList<Line2D> line2DArrayListNoFlyZone = new ArrayList<>();
    ArrayList<Line2D> line2DArrayListConfinementArea = new ArrayList<>();

    public String webPort;


    // Class Constructor
    DroneMap(String webPort){
        this.webPort = webPort;
    }

    // Getters
    public double getATLong(){ // private fields
        return -3.186874; }

    public double getATLat(){
        return 55.944494; }

    public double getFHLong(){ return FHLong; }

    public double getFHLat(){ return FHLat; }

    public double getKFCLong(){ return KFCLong; }

    public double getKFCLat(){ return KFCLat; }

    public double getMeadowsLong(){ return MeadowsLong; }

    public double getMeadowsLat(){ return MeadowsLat; }

    public double getBuccleuchLong(){ return BuccleuchLong; }

    public double getBuccleuchLat(){ return BuccleuchLat; }

    public String getURLStringForNoFlyZones(){ return "http://localhost:" + webPort + "/buildings/no-fly-zones.geojson"; }

    public String getURLStringForLandmarks(){
        return "http://localhost:" + webPort + "/buildings/landmarks.geojson";
    }

    // Methods
    public ArrayList<Line2D> getConfinementArea(){
        Point pointFH = Point.fromLngLat(getFHLong(),getFHLat());
        Point pointKFC = Point.fromLngLat(getKFCLong(),getKFCLat());
        Point pointMeadows = Point.fromLngLat(getMeadowsLong(),getMeadowsLat());
        Point pointBuccleuch = Point.fromLngLat(getBuccleuchLong(),getBuccleuchLat());
        confinementArea = new ArrayList<>();
        confinementArea.add(pointFH);
        confinementArea.add(pointKFC);
        confinementArea.add(pointMeadows);
        confinementArea.add(pointBuccleuch);
        ArrayList<Point2D> point2DS = new ArrayList<>();
        for (Point point:confinementArea){
            Point2D point2D = new Point2D.Double();
            point2D.setLocation(point.coordinates().get(0),point.coordinates().get(1));
            point2DS.add(point2D);
        }
        for (int i = 0;i<point2DS.size();i++){
            Line2D line2D = new Line2D.Double();
            if (i == point2DS.size()-1){
                line2D.setLine(point2DS.get(i),point2DS.get(0));
            }else {
                line2D.setLine(point2DS.get(i),point2DS.get(i+1));
            }
            line2DArrayListConfinementArea.add(line2D);
        }
        return line2DArrayListConfinementArea;
    }

    public ArrayList<LongLat> getLandMarks(){
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
        return landmarks;
    }

    public ArrayList<Line2D> getNoFlyZone(){
        WebConn webConn = new WebConn(webPort);
        HttpResponse<String> response = webConn.createResponse(webConn.createRequest(getURLStringForNoFlyZones()));
        FeatureCollection featureCollection = FeatureCollection.fromJson(response.body());
        List<Feature> features = featureCollection.features();
        assert features != null;
        for(Feature feature:features){
            Polygon polygon = (Polygon)feature.geometry();
            assert polygon != null;
            for(List<Point> listPoint:polygon.coordinates()){
                ArrayList<Point2D> point2DS = new ArrayList<>();
                for (Point point:listPoint){
                    Point2D point2D = new Point2D.Double();
                    point2D.setLocation(point.coordinates().get(0),point.coordinates().get(1));
                    point2DS.add(point2D);
                }
                for (int i = 0;i<point2DS.size();i++){
                    Line2D line2D = new Line2D.Double();
                    if (i == point2DS.size()-1){
                        line2D.setLine(point2DS.get(i),point2DS.get(0));
                    }else {
                        line2D.setLine(point2DS.get(i),point2DS.get(i+1));
                    }
                    line2DArrayListNoFlyZone.add(line2D);
                }
            }
        }
        return line2DArrayListNoFlyZone;
    }

    //打印路线
    public void printRoute(ArrayList<Point> pl, String day,String month,String year) throws IOException {
        LineString lineString = LineString.fromLngLats(pl);
        Feature feature = Feature.fromGeometry(lineString);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        String flightPath = featureCollection.toJson();
        try {
            FileWriter fileWriter = new FileWriter("drone-" + day + "-" + month + "-" + year + ".geojson");
            fileWriter.write(flightPath);
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
