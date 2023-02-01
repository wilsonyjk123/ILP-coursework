package uk.ac.ed.inf.R1;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils;
import uk.ac.ed.inf.Scaffolding.ScaffoldingUtils.*;

import uk.ac.ed.inf.App;
import uk.ac.ed.inf.DroneMap;
import uk.ac.ed.inf.DroneUtils;
import uk.ac.ed.inf.LongLat;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PathValidationTest {
    @Test
    /**
     * This is the unit test to check if cross confinement area function is working correctly.
     */
    public void LineCrossTest(){
        ArrayList<Line2D> testSet = new ArrayList<>();
        Line2D P1 = new Line2D.Double();
        P1.setLine(-3.1918, 55.9427, -3.1921, 55.9458);
        Line2D P2 = new Line2D.Double();
        P2.setLine(-3.1921, 55.9458, -3.1851, 55.9458);
        Line2D P3 = new Line2D.Double();
        P2.setLine(-3.1851, 55.9458, -3.1844, 55.9427);
        Line2D P4 = new Line2D.Double();
        P2.setLine(-3.1844, 55.9427, -3.1918, 55.9427);
        testSet.add(P1);
        testSet.add(P2);
        testSet.add(P3);
        testSet.add(P4);
        ScaffoldingUtils scaffoldingUtils = new ScaffoldingUtils();

        boolean result1 = scaffoldingUtils.LineCrossForTest(-3.1867,55.9447, -3.1863, 55.9431,testSet);
        assert result1 == Boolean.FALSE;

        boolean result2 = scaffoldingUtils.LineCrossForTest(-3.1852,55.9457, -3.1908, 55.9431,testSet);
        assert result2 == Boolean.FALSE;

        boolean result3 = scaffoldingUtils.LineCrossForTest(-3.1870,55.9445, -3.1898, 55.9449,testSet);
        assert result3 == Boolean.FALSE;

        boolean result4 = scaffoldingUtils.LineCrossForTest(-3.1902,55.9419, -3.1908, 55.9431,testSet);
        assert result4 == Boolean.TRUE;

        boolean result5 = scaffoldingUtils.LineCrossForTest(-3.1902,55.9419, -3.1868, 55.9466,testSet);
        assert result5 == Boolean.TRUE;

        boolean result6 = scaffoldingUtils.LineCrossForTest(-3.1907,55.9423, -3.1878, 55.9468,testSet);
        assert result6 == Boolean.TRUE;
    }
    @Test
    public void NoFlyZoneTest(){
        DroneMap droneMap = new DroneMap("9898");
        DroneUtils droneUtils = new DroneUtils(droneMap);
        boolean result1 = droneUtils.isNoFlyZone(-3.1888,55.9454,-3.1881,55.9440);
        assert result1 == Boolean.TRUE;

        boolean result2 = droneUtils.isNoFlyZone(-3.1867,55.9447, -3.1863, 55.9431);
        assert result2 == Boolean.FALSE;

        boolean result3 = droneUtils.isNoFlyZone(-3.1870,55.9445, -3.1898, 55.9449);
        assert result3 == Boolean.TRUE;

        boolean result4 = droneUtils.isNoFlyZone(-3.1902,55.9419, -3.1908, 55.9431);
        assert result4 == Boolean.FALSE;

        boolean result5 = droneUtils.isNoFlyZone(-3.1902,55.9419, -3.1868, 55.9466);
        assert result5 == Boolean.TRUE;

        boolean result6 = droneUtils.isNoFlyZone(-3.1907,55.9423, -3.1878, 55.9468);
        assert result6 == Boolean.TRUE;

        boolean result7 = droneUtils.isNoFlyZone(-3.1852,55.9457, -3.1908, 55.9431);
        assert result7 == Boolean.FALSE;

        boolean result8 = droneUtils.isNoFlyZone(-3.1889,55.9451, -3.1890, 55.9442);
        assert result8 == Boolean.TRUE;
    }

    @Test
    public void ConfinementAreaTest(){
        DroneMap droneMap = new DroneMap("9898");
        DroneUtils droneUtils = new DroneUtils(droneMap);
        boolean result1 = droneUtils.isConfinementArea(-3.1888,55.9454,-3.1881,55.9440);
        assert result1 == Boolean.TRUE;

        boolean result2 = droneUtils.isConfinementArea(-3.1867,55.9447, -3.1863, 55.9431);
        assert result2 == Boolean.TRUE;

        boolean result3 = droneUtils.isConfinementArea(-3.1870,55.9445, -3.1898, 55.9449);
        assert result3 == Boolean.TRUE;

        boolean result4 = droneUtils.isConfinementArea(-3.1902,55.9419, -3.1908, 55.9431);
        assert result4 == Boolean.TRUE;

        boolean result5 = droneUtils.isConfinementArea(-3.1902,55.9419, -3.1868, 55.9466);
        assert result5 == Boolean.TRUE;

        boolean result6 = droneUtils.isConfinementArea(-4.1907,55.9423, -3.1878, 55.9468);
        assert result6 == Boolean.FALSE;

        boolean result7 = droneUtils.isConfinementArea(-3.1852,56.9457, -4.1908, 55.9431);
        assert result7 == Boolean.FALSE;

        boolean result8 = droneUtils.isConfinementArea(-3.1889,55.9451, -3.1890, 55.9442);
        assert result8 == Boolean.TRUE;
    }
    @Test
    public void IntegrationTest(){
        DroneMap droneMap = new DroneMap("9898");
        DroneUtils droneUtils = new DroneUtils(droneMap);

        boolean result1a = droneUtils.isNoFlyZone(-3.1888,55.9454,-3.1881,55.9440);
        boolean result1b = droneUtils.isConfinementArea(-3.1888,55.9454,-3.1881,55.9440);
        assert result1a == Boolean.TRUE && result1b == Boolean.TRUE;

        boolean result2a = droneUtils.isNoFlyZone(-3.1867,55.9447, -3.1863, 55.9431);
        boolean result2b = droneUtils.isConfinementArea(-3.1867,55.9447, -3.1863, 55.9431);
        assert result2a == Boolean.FALSE && result2b == Boolean.TRUE;

        boolean result3a = droneUtils.isNoFlyZone(-3.1870,55.9445, -3.1898, 55.9449);
        boolean result3b = droneUtils.isConfinementArea(-3.1870,55.9445, -3.1898, 55.9449);
        assert result3a == Boolean.TRUE && result3b == Boolean.TRUE;

        boolean result4a = droneUtils.isNoFlyZone(-3.1902,55.9419, -3.1868, 55.9466);
        boolean result4b = droneUtils.isConfinementArea(-3.1902,55.9419, -3.1868, 55.9466);
        assert result4a == Boolean.TRUE && result4b == Boolean.TRUE;

        boolean result5a = droneUtils.isNoFlyZone(-4.1888,55.9454,-3.1881,55.9440);
        boolean result5b = droneUtils.isConfinementArea(-4.1888,55.9454,-3.1881,55.9440);
        assert result5a == Boolean.FALSE && result5b == Boolean.TRUE;

        boolean result6a = droneUtils.isNoFlyZone(-3.1888,56.9454,-4.1881,55.9440);
        boolean result6b = droneUtils.isConfinementArea(-3.1888,56.9454,-4.1881,55.9440);
        assert result6a == Boolean.FALSE && result6b == Boolean.FALSE;
    }
}
