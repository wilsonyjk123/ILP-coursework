package uk.ac.ed.inf.Scaffolding;

import com.mapbox.geojson.Point;
import org.junit.Test;
import uk.ac.ed.inf.DroneMap;
import uk.ac.ed.inf.FlightPath;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ScaffoldingUtils {

    DroneMap droneMap = new DroneMap("9898");

    /**
     * This is the function that checks if the current line cross the no-fly zones(Only for testing)
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public boolean LineCrossForTest(double lng1, double lat1, double lng2, double lat2, ArrayList<Line2D> testSet){
        boolean isCrossed = false;
        Line2D possiblePath = new Line2D.Double();
        possiblePath.setLine(lng1, lat1, lng2, lat2);
        for(Line2D line2D: testSet){
            isCrossed = line2D.intersectsLine(possiblePath);
            if(isCrossed){
                break;
            }
        }
        return isCrossed;
    }

    public boolean LinesCrossForTest(ArrayList<Line2D> flightPaths){
        boolean isCrossedNoflyZone = false;
        boolean isCrossedConfinementArea = false;
        boolean isCrossed = false;

        ArrayList<Line2D> noFlyZone2D = droneMap.getNoFlyZone();
        ArrayList<Line2D> confinementArea2D = droneMap.getConfinementArea();

        for(Line2D line2D:flightPaths){
            for(Line2D line2D1:noFlyZone2D){
                isCrossedNoflyZone = line2D.intersectsLine(line2D1);
                if(isCrossedNoflyZone){
                    break;
                }
            }
        }

        for(Line2D line2D:flightPaths){
            for(Line2D line2D1:confinementArea2D){
                isCrossedConfinementArea = line2D.intersectsLine(line2D1);
                if(isCrossedConfinementArea){
                    break;
                }
            }
        }
        isCrossed = isCrossedConfinementArea && isCrossedNoflyZone;
        return isCrossed;
    }

    public ArrayList<Line2D> ToLine2D(ArrayList<FlightPath> flightPaths){
        ArrayList<Line2D> line2DS = new ArrayList<>();
        for(FlightPath flightPath:flightPaths){
            double latFrom = flightPath.getFromLatitude();
            double lngFrom = flightPath.getFromLongitude();
            double latTo = flightPath.getToLatitude();
            double lngTo = flightPath.getToLongitude();
            Line2D line2D = new Line2D.Double();
            line2D.setLine(lngFrom,latFrom,lngTo,latTo);
            line2DS.add(line2D);
        }
        return line2DS;
    }

    public Point2D ToLastPoint2D(ArrayList<FlightPath> flightPaths){
        Point2D point2 = new Point2D.Double();
        int lastIndex = flightPaths.size()-1;
        FlightPath flightPath = flightPaths.get(lastIndex);
        double lng = flightPath.getFromLongitude();
        double lat = flightPath.getFromLatitude();
        point2.setLocation(lng,lat);
        return point2;
    }

    public double RoundDown(double value){
        BigDecimal bigDecimal = new BigDecimal(value);
        double result = bigDecimal.setScale(4,BigDecimal.ROUND_DOWN).doubleValue();
        return result;
    }
}
