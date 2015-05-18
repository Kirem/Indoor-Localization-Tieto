package pl.wroc.pwr.indoorlocalizationtieto.SearcherTest;

import android.test.InstrumentationTestCase;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.SearchEngine.Searcher;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-05-28.
 */
public class SearcherTest extends InstrumentationTestCase {
    Map map ;
    Room room ;
    Searcher searcher;

    @Override
    public void setUp() throws Exception {
        map = new Map();
        room = new Room(123,new Polygon(),true );
        room.setName("123");
        map.addObject(room);
        searcher = new Searcher(map);
    }
    public void testfindElementsWithinQuery() throws Exception{
        assertEquals(room, searcher.findElementsWithinQuery("pokój 123").get(0));
    }
}
