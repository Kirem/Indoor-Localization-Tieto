package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

public class Vertex {
    private double id;
    private ArrayList<Double> neighbours;

    public Vertex(double id) {
        this.id = id;
        neighbours = new ArrayList<>();
    }

    public void addNeighbour(Vertex neighbour){
        neighbours.add(neighbour.getId());
    }

    public double getId() {
        return id;
    }

    public ArrayList<Double> getNeighbours(){
        return neighbours;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            return this.id == ((Vertex) o).id;
        }
        return false;
    }
}