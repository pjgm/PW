package guided_project.model;

import guided_project.graph.DirectedEdge;

import java.util.LinkedList;

public class User {

    private LinkedList<DirectedEdge> edges;
    private double rank;

    public User(double rank) {
        this.rank = rank;
        this.edges = new LinkedList<>();
    }

    public LinkedList<DirectedEdge> getEdges() {
        return edges;
    }

    public void addEdge(DirectedEdge e) {
        edges.add(e);
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
