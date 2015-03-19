package pl.wroc.pwr.indoorlocalizationtieto.GeometryTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class TestMultipolygon extends InstrumentationTestCase {
    List<Point> pointsList = new ArrayList<>();
    List<Polygon> polygonList = new ArrayList<>();
    Polygon[] polygonArr;
    Multipolygon multipolygonList;
    Multipolygon multipolygonArr;

    @Override
    public void setUp() throws Exception {

        //10x10 sqare
        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(10.0, 10.0));
        pointsList.add(new Point(10.0, 0.0));

        polygonList.add(new Polygon(pointsList));

        pointsList.clear();
        //1x1 square
        pointsList.add(new Point(1.0, 1.0));
        pointsList.add(new Point(1.0, 2.0));
        pointsList.add(new Point(2.0, 2.0));
        pointsList.add(new Point(2.0, 1.0));

        polygonList.add(new Polygon(pointsList));

        pointsList.clear();
        //1x1 square
        pointsList.add(new Point(8.0, 8.0));
        pointsList.add(new Point(8.0, 9.0));
        pointsList.add(new Point(9.0, 9.0));
        pointsList.add(new Point(9.0, 8.0));

        polygonList.add(new Polygon(pointsList));

        polygonArr = polygonList.toArray(new Polygon[polygonList.size()]);

        multipolygonArr = new Multipolygon(polygonArr);
        multipolygonList = new Multipolygon(polygonList);
    }

    public void testCalculateLength() throws Exception {
        assertEquals(48.0, multipolygonArr.calculateLength());
        assertEquals(48.0, multipolygonList.calculateLength());
    }
}
