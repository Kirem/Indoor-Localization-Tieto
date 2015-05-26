package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private ArrayList<Vertex> vertexes;
    private ArrayList<Edge> edges;

    public Graph() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public Graph(ArrayList<Vertex> vertexes, ArrayList<Edge> edges) {
        this.vertexes = new ArrayList<>(vertexes);
        this.edges = new ArrayList<>(edges);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, double dist) throws IllegalArgumentException{

        if(! (vertexes.contains(vertex1) && vertexes.contains(vertex2))) {
            throw new IllegalArgumentException("List vertexes doesn't contain at least one vertex");
        }

        Edge edge = new Edge(vertex1,vertex2,dist);
        edges.add(edge);
        vertex1.addNeighbour(vertex2);
        vertex2.addNeighbour(vertex1);
    }

    public void setEdgeMultiplier(Vertex vertex1, Vertex vertex2, double multiplier){
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

    public ArrayList<Vertex> getVertexes(){ return vertexes; }

    public double getEdgeDistance(double vertex1ID, double vertex2ID) {
        Vertex v1 = getVertexById(vertex1ID); //new Vertex(vertex1);
        Vertex v2 = getVertexById(vertex2ID); //new Vertex(vertex2);
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

    public Vertex getVertexById(double id) {
        for(Vertex vertex : vertexes){
            if(vertex.getId() == id){
                return vertex;
            }
        }
        return null;
    }

    public List<Double> getNeighboursForVertex(double vertexId){
        //Vertex tem = new Vertex(vertexId);
        //return vertexes.get(vertexes.indexOf(tem)).getNeighbours();
        return getVertexById(vertexId).getNeighbours();
    }

    public String toString(){
        String str = new String();
        for(Edge e: edges){
            str += e.getVertex1Id() + "-" + e.getVertex2Id() + ":" + e.getDistance() + "\n";
        }
        return str;
    }
}