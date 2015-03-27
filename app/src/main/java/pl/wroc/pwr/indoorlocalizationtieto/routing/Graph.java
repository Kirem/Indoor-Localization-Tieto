package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Vertex> vertexes;
    private ArrayList<Edge> edges;

    public Graph() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, int dist) throws IllegalArgumentException{

        if(! (vertexes.contains(vertex1) && vertexes.contains(vertex2))) {
            throw new IllegalArgumentException("List vertexes doesn't contain at least one vertex");
        }

        Edge edge = new Edge(vertex1,vertex2,dist);
        edges.add(edge);
        vertex1.addNeighbour(vertex2);
        vertex2.addNeighbour(vertex1);
    }
    public void setEdgeMultiplier(Vertex vertex1, Vertex vertex2, int multiplier){
        Edge edge = new Edge(vertex1, vertex2, 0);
        for(Edge e: edges){
            if(e.equals(edge)){
                e.setMultiplier(multiplier);
            }
        }
    }
    public void resetMultiplierForEdges(){
        for(Edge e: edges){
            e.setMultiplier(1);
        }
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

    public ArrayList<Vertex> getVertexes(){ return vertexes; }
    public ArrayList<Edge> getEdges(){ return edges; }

    public int getEdgeDistance(int vertex1, int vertex2){
        Vertex v1 = new Vertex(vertex1);
        Vertex v2 = new Vertex(vertex2);
        Edge e = new Edge(v1, v2, 0);
        for(Edge edge: edges){
            if(edge.equals(e)){
                return edge.getDistance();
            }
        }
        return -1;
    }
    public Vertex getVertex(int which){
        return vertexes.get(which);
    }

    public String toString(){
        String str = new String();
        for(Edge e: edges){
            str += e.getVertex1Id() + "-" + e.getVertex2Id() + ":" + e.getDistance() + "\n";
        }
        return str;
    }


}
