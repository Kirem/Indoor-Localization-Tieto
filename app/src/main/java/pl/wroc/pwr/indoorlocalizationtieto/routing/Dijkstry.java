package pl.wroc.pwr.indoorlocalizationtieto.routing;

import android.util.Log;

import java.util.ArrayList;

public class Dijkstry {
    private ArrayList<Integer> pending;
    private ArrayList<Integer> considered;
    private ArrayList<Integer> precursor;
    private ArrayList<Integer> path;
    private ArrayList<Integer> result;
    private Graph graph;
    private int justAdded, goal;


    public Dijkstry(Graph g){
        setGraph(g);
        precursor = new ArrayList<>();
        considered = new ArrayList<>();
        path = new ArrayList<>();
        pending = new ArrayList<>();
        result = new ArrayList<>();
        for(int i=0 ; i < graph.getNumberOfVertexes() ; i++){
            precursor.add(-1);
            pending.add(i);
            path.add(Integer.MAX_VALUE);
        }
    }

    public ArrayList<Integer> findShortestPath(int start, int end){
        setTarget(start, end);
        while(!pending.isEmpty()){
            for(int neighbour: graph.getVertexes().get(justAdded).getNeighbours()){
                if(pending.contains(neighbour)) {
                   int newPath = path.get(justAdded) + graph.getEdgeDistance(justAdded, neighbour);
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
    public void setTarget(int start, int end){
        considered.add(start);
        pending.remove(pending.indexOf(start));
        path.set(start, 0);
        justAdded=start;
        goal = end;
    }
    public void setShorterPath(int neighbour, int newPath){
        path.set(neighbour, newPath);
        precursor.set(neighbour, justAdded);
    }
    public void addNextVertex(){
        int smallest = findSmallestValue();
        int position = pending.indexOf(smallest);
        pending.remove(pending.indexOf(smallest));
        considered.add(smallest);
        justAdded = smallest;
    }
    public int findSmallestValue(){
        int smallestValue = Integer.MAX_VALUE, index = -1;
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
