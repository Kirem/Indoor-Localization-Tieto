package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

public class Graph {
    ArrayList<Vertex> vertexes;
    ArrayList<Edge> edges;

    public Graph() {
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, int dist){
        Edge edge = new Edge(vertex1,vertex2,dist);
        edges.add(edge);
        vertex1.addNeighbour(vertex2);
        vertex2.addNeighbour(vertex1);
    }

    public void addVertex(Vertex vertexId){
        vertexes.add(vertexId);
    }

    public int getNumberOfVertexes(){
        return vertexes.size();
    }
    public int getNumberOfEdges(){
        return edges.size();
    }

    public String toString(){
        String str = new String();
        for(Edge e: edges){
            str += e.getVertex1Id() + "-" + e.getVertex2Id() + ":" + e.getDistance() + "\n";
        }
        return str;
    }

}
