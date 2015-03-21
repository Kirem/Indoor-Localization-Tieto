package pl.wroc.pwr.indoorlocalizationtieto.GeometryTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-03-18.
 * s
 */
public class testLine extends InstrumentationTestCase {

    Point p1, p2;
    Line l1;

    @Override
    public void setUp() throws Exception {

        p1 = new Point(0.0, 0.0);
        p2 = new Point(0.0, 12.0);

        l1 = new Line(p1, p2);
    }

    public void testCalculateLength() {
        assertEquals(12.0, l1.calculateLength());
    }

}
