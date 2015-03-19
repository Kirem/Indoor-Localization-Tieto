package pl.wroc.pwr.indoorlocalizationtieto.GeometryTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class testPoint extends InstrumentationTestCase {

    Point point;

    @Override
    protected void setUp() throws Exception {
        point = new Point(5.0, 2.0);
    }

    public void testCalculateLength() throws Exception {
        assertEquals(0.0, point.calculateLength());
    }
}
