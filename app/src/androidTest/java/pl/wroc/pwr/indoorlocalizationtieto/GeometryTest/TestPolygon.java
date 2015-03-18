package pl.wroc.pwr.indoorlocalizationtieto.GeometryTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class TestPolygon extends InstrumentationTestCase {
    Polygon polygonArr;
    Polygon polygonList;

    List<Point> pointsList = new ArrayList<>();
    Point[] pointsArr;

    @Override
    public void setUp() throws Exception {

        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(8.0, 10.0));
        pointsList.add(new Point(8.0, 0.0));

        pointsArr = pointsList.toArray(new Point[pointsList.size()]);

        polygonList = new Polygon(pointsList);
        polygonArr = new Polygon(pointsArr);
    }

    public void testCalculateLength() throws Exception {
        assertEquals(36.0, polygonArr.calculateLength());
        assertEquals(36.0, polygonList.calculateLength());
    }
}
