package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

public class Dijkstry {
    private ArrayList<Integer> pending;
    private ArrayList<Integer> precursor;
    private ArrayList<Double> path;
    private ArrayList<Integer> result;
    private Graph graph;
    private int justAdded, goal;


    public Dijkstry(Graph g){
        setGraph(g);
        precursor = new ArrayList<>();
        path = new ArrayList<>();
        pending = new ArrayList<>();
        result = new ArrayList<>();
    }

    public ArrayList<Integer> findShortestPath(int start, int end){
        clearIfExist();
        setLists();
        setTarget(start, end);
        while(!pending.isEmpty()){
            for(int neighbour: graph.getVertexes().get(justAdded).getNeighbours()){
                if(pending.contains(neighbour)) {
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

    public void clearIfExist(){
        pending.clear();
        path.clear();
        precursor.clear();
        result.clear();
    }

    public void setLists(){
        for(int i=0 ; i < graph.getNumberOfVertexes() ; i++){
            precursor.add(-1);
            pending.add(i);
            path.add(Double.MAX_VALUE);
        }
    }

    public void setTarget(int start, int end){
        pending.remove(pending.indexOf(start));
        path.set(start, 0.0);
        justAdded=start;
        goal = end;
    }

    public void setShorterPath(int neighbour, double newPath){
        path.set(neighbour, newPath);
        precursor.set(neighbour, justAdded);
    }

    public void addNextVertex(){
        int smallest = findSmallestValue();
        pending.remove(pending.indexOf(smallest));
        justAdded = smallest;
    }

    public int findSmallestValue(){
        double smallestValue = Double.MAX_VALUE;
        int index = -1;
        for(int next: pending) {
            if (path.get(next) < smallestValue) {
                smallestValue = path.get(next);
                index = next;
            }
        }
        return index;
    }

    public void createSolution(){
        int previous = goal;
        while(precursor.get(previous) != -1){
            result.add(precursor.get(previous));
            previous = precursor.get(previous);
        }
    }
}
