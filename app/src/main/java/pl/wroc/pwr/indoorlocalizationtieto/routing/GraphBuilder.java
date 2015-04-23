package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;

/**
 * Created by Mateusz on 2015-04-23.
 */
public class GraphBuilder {

    private Graph outdoorGraph;
    private Vertex vertex;
    private Edge edge;
    private ArrayList<Vertex> vertexes;
    private ArrayList<Edge> edges;
    private ArrayList<Road> roads;
    private ArrayList<Crossing> crossroads;

    public GraphBuilder() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        roads = new ArrayList<>();
        crossroads = new ArrayList<>();
    }

    /**
     * Building outdoor graph from map objects
     *
     * @param mapObjects list of all map objects
     * @return graph for outdoor
     */
    public Graph buidOutdoorGraph(ArrayList<MapObject> mapObjects) {
        getDataFromMap(mapObjects);

        //make vertexes from all crossroads first
        for (Crossing crossroad : crossroads) {
            if (!crossroad.isCrossing()) {
                Point point = (Point) crossroad.getObjectGeometry();
                vertex = new Vertex(point.hashCode(), point);
                vertexes.add(vertex);
            }
        }

        //make vertexes and edges for roads
        for (Road road : roads) {
            LineString geometry = (LineString) road.getObjectGeometry();
            ArrayList<Point> roadPoints = new ArrayList<>(geometry.getLineString());
            ArrayList<Vertex> tempVertexes = new ArrayList<>();

            //Add vertexes to temporary array list
            for (int i = 0; i < roadPoints.size(); i++) {
                vertex = isVertexCreated(roadPoints.get(i));
                if (vertex == null && (i == 0 || i == roadPoints.size() - 1)) {
                    vertex = new Vertex(roadPoints.get(i).hashCode(), roadPoints.get(i));
                    tempVertexes.add(vertex);
                } else if (vertex != null) {
                    tempVertexes.add(vertex);
                }
            }
            //If we got all vertexes created from road, add neighbours
            for (int i = 0; i < tempVertexes.size(); i++) {
                if (i == 0) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i + 1));
                } else if (i > 0 && i < tempVertexes.size() - 1) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i + 1));
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i - 1));
                } else if (i == tempVertexes.size() - 1) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i - 1));
                }
            }
            //Now add these vertexes to proper vertex list and make edges for them
            for (Vertex vertex1 : tempVertexes) {
                int index = isVertexCreated(vertex1);
                //if vertex doesn't exist
                if (index == -1)
                    vertexes.add(vertex1);
                //if vertex exist
                if (index >= 0 && index <= vertexes.size()) {
                    vertexes.get(index).setNeighbours(vertex1.getNeighbours());
                }
                //Taking care of edges
                ArrayList<Integer> neighbours = vertex1.getNeighbours();
                for (Integer id : neighbours) {
                    vertex = getVertex(id);
                    edge = new Edge(vertex1, vertex, countDistance(vertex1, vertex, road));
                    if (!isEdgeCreated(edge)) {
                        edges.add(edge);
                    }
                }
            }
        }
        outdoorGraph = new Graph(vertexes, edges);
        return outdoorGraph;
    }

    /**
     * Calculate distance between vertex1 and vertex2, including all points between them on the road
     *
     * @param vertex1 vertex on beginning of the edge
     * @param vertex2 vertex on end of the edge
     * @param road    road, in which vertex1 and vertex2 lie
     * @return distance road distance between vertexes
     */
    private double countDistance(Vertex vertex1, Vertex vertex2, Road road) {
        double distance = 0;
        boolean counting = false;
        LineString geometry = (LineString) road.getObjectGeometry();
        ArrayList<Point> roadPoints = new ArrayList<>(geometry.getLineString());
        for (int i = 0; i < roadPoints.size(); i++) {
            if (roadPoints.get(i).equals(vertex1.getPoint()) || roadPoints.get(i).equals(vertex2.getPoint())) {
                counting = !counting;
            }
            if (counting) {
                distance += roadPoints.get(i).distanceTo(roadPoints.get(i + 1));
            }
        }
        return distance;
    }

    /**
     * Find if edge is already created
     *
     * @param edge
     * @return true if edge is in edges list, false if not
     */
    private boolean isEdgeCreated(Edge edge) {
        for (Edge edge2 : edges) {
            if (edge2.equals(edge))
                return true;
        }
        return false;
    }

    /**
     * Find if vertex made from point already exists
     * It compares hashcode of point with id of vertex
     *
     * @param point
     * @return true if vertex from point was created already, false if not
     */
    private Vertex isVertexCreated(Point point) {
        for (Vertex vertex1 : vertexes) {
            if (vertex1.getId() == point.hashCode())
                return vertex1;
        }
        return null;
    }

    /**
     * Find if vertex is already created
     *
     * @param vertex1
     * @return index of ArrayList if vertex exists, -1 if not
     */
    private Integer isVertexCreated(Vertex vertex1) {
        for (int i = 0; i < vertexes.size(); i++) {
            if (vertex1.equals(vertexes.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private Vertex getVertex(int id) {
        for (Vertex vertex1 : vertexes) {
            if (vertex1.getId() == id)
                return vertex1;
        }
        return null;
    }

    /**
     * Gather all roads and crossroads from list of MapObjects
     * to lists of roads and crossroads
     *
     * @param mapObjects
     */
    private void getDataFromMap(ArrayList<MapObject> mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject.getClass() == Road.class) {
                roads.add((Road) mapObject);
            }
            if (mapObject.getClass() == Crossing.class) {
                crossroads.add((Crossing) mapObject);
            }
        }
    }
}
