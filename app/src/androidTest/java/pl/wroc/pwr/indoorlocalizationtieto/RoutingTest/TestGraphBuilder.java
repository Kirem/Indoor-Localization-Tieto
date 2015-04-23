package pl.wroc.pwr.indoorlocalizationtieto.RoutingTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Graph;
import pl.wroc.pwr.indoorlocalizationtieto.routing.GraphBuilder;

/**
 * Created by Mateusz on 2015-04-23.
 */
public class TestGraphBuilder extends InstrumentationTestCase {

    Crossing crossroad;
    ArrayList<MapObject> mapObjects = new ArrayList<>();

    @Override
    public void setUp() throws Exception {
        Point point = new Point(0, 0);
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Road> roads = new ArrayList<>();

        points.clear();
        points.add(new Point(0,-5));
        points.add(point);
        points.add(new Point(0, 5));

        Road road = new Road(1, new LineString(points));
        mapObjects.add(road);
        roads.add(road);

        points.clear();
        points.add(new Point(5, 0));
        points.add(point);
        points.add(new Point(-5, 0));

        road = new Road(2, new LineString(points));
        mapObjects.add(road);
        roads.add(road);

        points.clear();
        points.add(new Point(5,-5));
        points.add(point);
        points.add(new Point(-5, 5));

        road = new Road(3, new LineString(points));
        mapObjects.add(road);
        roads.add(road);

        points.clear();
        points.add(new Point(-5,-5));
        points.add(point);
        points.add(new Point(5, 5));

        road = new Road(4, new LineString(points));
        mapObjects.add(road);
        roads.add(road);

        crossroad = new Crossing(5, roads, point, false);
        mapObjects.add(crossroad);
    }

    public void testBuildOutdoorGraph() throws Exception {
        GraphBuilder graphBuilder = new GraphBuilder();
        Graph graph = graphBuilder.buidOutdoorGraph(mapObjects);

        assertEquals(9, graph.getNumberOfVertexes());
    }
}
