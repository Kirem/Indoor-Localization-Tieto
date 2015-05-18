package pl.wroc.pwr.indoorlocalizationtieto.SearcherTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.SearchEngine.Searcher;
import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-05-28.
 */
public class SearcherTest extends InstrumentationTestCase {
    Map map ;
    Room room ;
    Room room2 ;
    Room room3 ;
    Building building ;
    Building building2 ;
    Building building3 ;

    Searcher searcher;
    ArrayList<Room> expectedRoomsList;
    ArrayList<Building> expectedBuildingList;


    @Override
    public void setUp() throws Exception {
        map = new Map();
        room = new Room(1,new Polygon(),true );
        room2 = new Room(2,new Polygon(),true );
        room3 = new Room(3,new Polygon(),true );
        building = new Building (1, new Polygon());
        building2 = new Building (2, new Polygon());
        building3 = new Building (3, new Polygon());

        expectedRoomsList = new ArrayList<>();
        expectedBuildingList = new ArrayList<>();


        room.setName("123");
        room2.setName("125");
        room3.setName("21");

        building.setName("Serowiec");
        building2.setName("Serowiec 2");
        building3.setName("Technopolis");

        map.addObject(room);
        map.addObject(room2);
        map.addObject(room3);
        map.addObject(building);
        map.addObject(building2);
        map.addObject(building3);

        searcher = new Searcher(map);

        expectedRoomsList.add(room);
        expectedRoomsList.add(room2);

        expectedBuildingList.add(building);
        expectedBuildingList.add(building2);

    }
    public void testfindElementsWithinQuery() throws Exception{
        assertEquals(expectedRoomsList, searcher.findElementsWithinQuery("12"));
        assertEquals(expectedRoomsList, searcher.findElementsWithinQuery("pokój 12"));
        assertEquals(expectedRoomsList, searcher.findElementsWithinQuery("Pokój 12"));
        assertEquals(expectedBuildingList, searcher.findElementsWithinQuery("Serowiec"));
        assertEquals(expectedBuildingList, searcher.findElementsWithinQuery("serowiec"));
        assertEquals(expectedBuildingList, searcher.findElementsWithinQuery("budynek Ser"));
        assertEquals(expectedBuildingList, searcher.findElementsWithinQuery("Budynek Ser"));
        assertEquals(expectedBuildingList, searcher.findElementsWithinQuery("budynek seR"));
    }
}
