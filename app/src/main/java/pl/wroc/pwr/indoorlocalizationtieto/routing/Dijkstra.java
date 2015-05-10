package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dijkstra {
    private Map<Double, Integer> pending;
    private Map<Double, Double> precursor;
    private Map<Double, Double> path;
    private ArrayList<Double> result;
    private Graph graph;
    private double justAdded, goal;


    public Dijkstra(Graph g){
        setGraph(g);
        result = new ArrayList<>();
        precursor = new HashMap<>();
        pending = new HashMap<>();
        path = new HashMap<>();
    }

    public ArrayList<Double> findShortestPath(double start, double end){
        clearIfExist();
        setLists();
        setTarget(start, end);
        while(!pending.isEmpty()){
            for(double neighbour: graph.getNeighboursForVertex(justAdded)){
                if(pending.containsKey(neighbour)) {
                   double newPath = path.get(justAdded) + graph.getEdgeDistance(justAdded, neighbour);
                   if(path.get(neighbour)> newPath){
                       setShorterPath(neighbour, newPath);
                   }
                }
            }
            addNextVertex();
        }
        createSolution();
        return result;
    }

    public void setGraph(Graph g){
        graph = g;
    }

    private void clearIfExist(){
        pending.clear();
        path.clear();
        precursor.clear();
        result.clear();
    }

    private void setLists(){
        for(int i=0 ; i < graph.getNumberOfVertexes() ; i++){
            precursor.put(graph.getVertex(i).getId(), -1.0);
            pending.put(graph.getVertex(i).getId(), i);
            path.put(graph.getVertex(i).getId(), Double.MAX_VALUE);
        }
    }

    private void setTarget(double start, double end){
        pending.remove(start);
        path.put(start, 0.0);
        justAdded = start;
        goal = end;
    }

    private void setShorterPath(double neighbour, double newPath){
        path.put(neighbour, newPath);
        precursor.put(neighbour, justAdded);
    }

    private void addNextVertex(){
        double smallest = findSmallestValue();
        pending.remove(smallest);
        justAdded = smallest;
    }

    private double findSmallestValue(){
        double smallestValue = Double.MAX_VALUE;
        double index = -1;
        for(double next: pending.keySet()) {
            if (path.get(next) < smallestValue) {
                smallestValue = path.get(next);
                index = next;
            }
        }
        return index;
    }

    private void createSolution(){
        double previous = goal;
        while(precursor.get(previous) != -1){
            result.add(precursor.get(previous));
            previous = precursor.get(previous);
        }
    }
}
