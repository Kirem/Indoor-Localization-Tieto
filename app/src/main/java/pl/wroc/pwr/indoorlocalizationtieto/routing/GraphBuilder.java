package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossroad;
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
    private ArrayList<Crossroad> crossroads;

    public GraphBuilder() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        roads = new ArrayList<>();
        crossroads = new ArrayList<>();
    }

    /**
     * Building outdoor graph from map ojects
     * @param mapObjects
     * @return
     */
    public Graph buidOutdoorGraph(ArrayList<MapObject> mapObjects) {
        getDataFromMap(mapObjects);

        //make vertexes from all crossroads first
        for(Crossroad crossroad : crossroads) {
            Point point = (Point) crossroad.getObjectGeometry();
            vertex = new Vertex(point.hashCode(), point);
            vertexes.add(vertex);
        }
        //make vertexes and edges for roads
        for(Road road : roads) {
            LineString geometry = (LineString)road.getObjectGeometry();
            ArrayList<Point> roadPoints = new ArrayList<>(geometry.getLineString());
            ArrayList<Vertex> tempVertexes = new ArrayList<>();

            //Add vertexes to temporary array list
            for(int i = 0; i < roadPoints.size(); i++) {
                vertex = isVertexCreated(roadPoints.get(i));
                if(vertex == null) {
                    vertex = new Vertex(roadPoints.get(i).hashCode(), roadPoints.get(i));
                    tempVertexes.add(vertex);
                } else {
                    tempVertexes.add(vertex);
                }
            }
            //If we got all vertexes created from road, add neighbours
            for(int i = 0; i < tempVertexes.size(); i++) {
                if(i == 0) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i+1));
                } else if (i > 0 && i < tempVertexes.size() - 1) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i+1));
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i-1));
                } else if (i == tempVertexes.size() - 1) {
                    tempVertexes.get(i).addNeighbour(tempVertexes.get(i-1));
                }
            }
            //Now add these vertexes to proper vertex list and make edges for them
            for(Vertex vertex1 : tempVertexes) {
                int index = isVertexCreated(vertex1);
                //if vertex doesn't exist
                if(index == -1)
                    vertexes.add(vertex1);
                //if vertex exist
                if(index >= 0 && index <= vertexes.size()) {
                    vertexes.get(index).setNeighbours(vertex1.getNeighbours());
                }
                //Taking care of edges
                ArrayList<Integer> neighbours = vertex1.getNeighbours();
                for(Integer id : neighbours) {
                    vertex = getVertex(id);
                    edge = new Edge(vertex1, vertex, vertex1.distanceTo(vertex));
                    if(!isEdgeCreated(edge)) {
                        edges.add(edge);
                    }
                }
            }
        }
        outdoorGraph = new Graph(vertexes, edges);
        return outdoorGraph;
    }

    private boolean isEdgeCreated(Edge edge) {
        for(Edge edge2 : edges) {
            if(edge2.equals(edge))
                return true;
        }
        return false;
    }

    /**
     * Find if vertex made from point alredy exists
     * It compares hashcode of point with id of vertex
     * @param point
     * @return true if vertex from point was created already, false if not
     */
    private Vertex isVertexCreated(Point point){
        for(Vertex vertex1 : vertexes) {
            if(vertex1.getId() == point.hashCode())
                return vertex1;
        }
        return null;
    }

    /**
     *
     * @param vertex1
     * @return index of ArrayList if vertex exists, -1 if not
     */
    private Integer isVertexCreated(Vertex vertex1) {
        for(int i = 0; i < vertexes.size(); i++) {
            if(vertex1.equals(vertexes.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private Vertex getVertex(int id) {
        for(Vertex vertex1 : vertexes) {
            if(vertex1.getId() == id)
                return vertex1;
        }
        return null;
    }

    /**
     * Gather all roads and crossroads from list of MapObjects
     * @param mapObjects
     */
    private void getDataFromMap(ArrayList<MapObject> mapObjects) {
        for(MapObject mapObject : mapObjects) {
            if(mapObject.getClass() == Road.class) {
                roads.add((Road)mapObject);
            }
            if(mapObject.getClass() == Crossroad.class) {
                crossroads.add((Crossroad)mapObject);
            }
        }
    }
}
