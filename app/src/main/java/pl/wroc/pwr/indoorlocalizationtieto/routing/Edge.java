package pl.wroc.pwr.indoorlocalizationtieto.routing;


public class Edge {
    private Vertex vertex1, vertex2;
    private int distance;

    public Edge(Vertex v1, Vertex v2, int dis){
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.distance = dis;
    }

    public int getDistance(){
        return distance;
    }
    public int getVertex1Id(){
        return vertex1.getId();
    }
    public int getVertex2Id(){
        return vertex2.getId();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Edge){
            return ((this.vertex1 == ((Edge) o).vertex1 && this.vertex2 == ((Edge) o).vertex2 ) || (this.vertex1 == ((Edge) o).vertex2 && this.vertex2 == ((Edge) o).vertex1 ));
        }
        return false;
    }


}
