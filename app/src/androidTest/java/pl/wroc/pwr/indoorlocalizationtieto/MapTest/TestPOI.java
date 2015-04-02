package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.POI;

/**
 * Created by Mateusz on 2015-03-26.
 */
public class TestPOI  extends InstrumentationTestCase {
    POI poi;
    Point point;

    @Override
    public void setUp() throws Exception {
        point = new Point(10.0, 10.0);
        poi = new POI(1, point);
    }

    public void testGetGeometry() throws Exception {
        assertEquals(point, poi.getObjectGeometry());
    }

    public void testGetGeometries() throws Exception {
        assertEquals(point, poi.getGeometries().get(0));
    }

}
