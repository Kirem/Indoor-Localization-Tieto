package pl.wroc.pwr.indoorlocalizationtieto.routing;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Vertex {
    private double id;
    private Point point;
    private ArrayList<Double> neighbours;

    public Vertex(double id, Point point) {
        this.id = id;
        this.point = point;
        neighbours = new ArrayList<>();
    }

    public void addNeighbour(Vertex neighbour){
        neighbours.add(neighbour.getId());
    }

    public double distanceTo(Vertex vertex) {
        double distance = this.getPoint().distanceTo(vertex.getPoint());
        return distance;
    }
    public double getId() {
        return id;
    }

    public Point getPoint() { return point; }

    public ArrayList<Double> getNeighbours(){
        return neighbours;
    }
    public void setNeighbours(ArrayList<Double> neighbours) {
        this.neighbours.clear();
        this.neighbours.addAll(neighbours);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            return this.id == ((Vertex) o).id;
        }
        return false;
    }
}