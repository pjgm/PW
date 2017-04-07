package guided_project.model;

import guided_project.graph.DirectedEdge;

import java.util.LinkedList;

public class User {

    private LinkedList<DirectedEdge> outEdges;
    private LinkedList<DirectedEdge> inEdges;
    private double rank;

    public User(double rank) {
        this.rank = rank;
        this.outEdges = new LinkedList<>();
        this.inEdges = new LinkedList<>();
    }

    public LinkedList<DirectedEdge> getOutEdges() {
        return outEdges;
    }

    public LinkedList<DirectedEdge> getInEdges() {
        return inEdges;
    }

    public void addOutEdge(DirectedEdge e) {
        outEdges.add(e);
    }

    public void addInEdge(DirectedEdge e) {
        inEdges.add(e);
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
