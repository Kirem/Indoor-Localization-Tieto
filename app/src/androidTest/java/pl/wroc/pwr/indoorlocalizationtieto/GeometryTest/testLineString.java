package pl.wroc.pwr.indoorlocalizationtieto.GeometryTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class testLineString extends InstrumentationTestCase {

    LineString lineStringArr;
    LineString lineStringList;
    List<Point> pointList = new ArrayList<>();
    Point[] pointArr;

    @Override
    public void setUp() throws Exception {

        pointList.add(new Point(0.0, 0.0));
        pointList.add(new Point(0.0, 10.0));
        pointList.add(new Point(8.0, 10.0));
        pointList.add(new Point(8.0, 15.0));

        pointArr = pointList.toArray(new Point[pointList.size()]);

        lineStringArr = new LineString(pointArr);
        lineStringList = new LineString(pointList);
    }

    public void testCalculateLength() throws Exception {
        assertEquals(23.0, lineStringArr.calculateLength());
        assertEquals(23.0, lineStringList.calculateLength());
    }
}
