package pl.wroc.pwr.indoorlocalizationtieto.ParserTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.Elements;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.Node;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.Parser;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by Mateusz on 2015-03-22.
 */
public class TestParser extends InstrumentationTestCase {

    Elements elements;
    Parser parser;
    @Override
    public void setUp() throws Exception {
        elements = new Elements(51.1080643,17.0647986,100);
        parser = new Parser();
        parser.parseRegion();
    }

    public void testElements() throws Exception {
        assertTrue(Elements.getWaysList().containsKey(22661312L));
    }
}
