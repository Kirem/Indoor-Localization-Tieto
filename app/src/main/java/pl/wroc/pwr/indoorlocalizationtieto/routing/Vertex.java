package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

public class Vertex {
    private int id;
    private ArrayList<Vertex> neighbours;

    public Vertex(int id) {
        this.id = id;
    }

    public void addNeighbour(Vertex neighbour) {
        neighbours.add(neighbour);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            return this.id == ((Vertex) o).id;
        }
        return false;
    }
}