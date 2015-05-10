package pl.wroc.pwr.indoorlocalizationtieto.RoutingTest;

import android.test.InstrumentationTestCase;
import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.routing.Dijkstra;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Graph;
import pl.wroc.pwr.indoorlocalizationtieto.routing.Vertex;

public class TestDijkstra extends InstrumentationTestCase{
    Graph graph = new Graph();
    Dijkstra dijkstra = new Dijkstra(graph);

    @Override
    public void setUp() throws Exception{
        for(int i=0; i<6; i++){
            Vertex v = new Vertex(i+5.5);
            graph.addVertex(v);
        }

        graph.addEdge(graph.getVertexById(5.5), graph.getVertexById(6.5), 3);
        graph.addEdge(graph.getVertexById(5.5), graph.getVertexById(9.5), 3);
        graph.addEdge(graph.getVertexById(6.5), graph.getVertexById(7.5), 10);
        graph.addEdge(graph.getVertexById(7.5), graph.getVertexById(10.5), 1);
        graph.addEdge(graph.getVertexById(8.5), graph.getVertexById(6.5), 3);
        graph.addEdge(graph.getVertexById(9.5), graph.getVertexById(10.5), 20);
        graph.addEdge(graph.getVertexById(10.5), graph.getVertexById(5.5), 6);
        graph.addEdge(graph.getVertexById(10.5), graph.getVertexById(8.5), 1);
    }

    public void testFindShortestPath() throws Exception{
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(5.5);
        expected.add(10.5);
        expected.add(7.5);
        assertEquals(expected, dijkstra.findShortestPath(7.5,9.5));

        expected.clear();
        expected.add(8.5);
        expected.add(6.5);
        assertEquals(expected, dijkstra.findShortestPath(6.5,10.5));

        graph.setEdgeMultiplier(graph.getVertexById(6.5), graph.getVertexById(8.5), 3);
        dijkstra = new Dijkstra(graph);
        expected.clear();
        expected.add(5.5);
        expected.add(6.5);
        assertEquals(expected, dijkstra.findShortestPath(6.5,10.5));
    }
}
