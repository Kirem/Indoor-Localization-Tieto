package pl.wroc.pwr.indoorlocalizationtieto.RoutingTest;

import android.test.InstrumentationTestCase;
import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Dijkstra;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Graph;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Vertex;

public class TestDijkstra extends InstrumentationTestCase{
    Graph graph = new Graph();
    Dijkstra dijkstra = new Dijkstra(graph);

    @Override
    public void setUp() throws Exception{
        Point point = new Point(0, 0);
        for(int i=0; i<6; i++){
            Vertex v = new Vertex(i, point);
            graph.addVertex(v);
        }

        graph.addEdge(graph.getVertex(0), graph.getVertex(1), 3);
        graph.addEdge(graph.getVertex(0), graph.getVertex(4), 3);
        graph.addEdge(graph.getVertex(1), graph.getVertex(2), 10);
        graph.addEdge(graph.getVertex(2), graph.getVertex(5), 1);
        graph.addEdge(graph.getVertex(3), graph.getVertex(1), 3);
        graph.addEdge(graph.getVertex(4), graph.getVertex(5), 20);
        graph.addEdge(graph.getVertex(5), graph.getVertex(0), 6);
        graph.addEdge(graph.getVertex(5), graph.getVertex(3), 1);
    }

    public void testFindShortestPath() throws Exception{
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(0d);
        expected.add(5d);
        expected.add(2d);
        assertEquals(expected, dijkstra.findShortestPath(2,4));

        expected.clear();
        expected.add(3d);
        expected.add(1d);
        assertEquals(expected, dijkstra.findShortestPath(1,5));

        graph.setEdgeMultiplier(graph.getVertex(1), graph.getVertex(3), 3);
        dijkstra = new Dijkstra(graph);
        expected.clear();
        expected.add(0d);
        expected.add(1d);
        assertEquals(expected, dijkstra.findShortestPath(1,5));
    }
}
