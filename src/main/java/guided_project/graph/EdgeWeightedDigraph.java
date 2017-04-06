package guided_project.graph;

import guided_project.search.PageRank;
import guided_project.model.User;

import java.util.*;

public class EdgeWeightedDigraph {

    private int V;
    private int E;
    Map<Integer, User> adj;

    public EdgeWeightedDigraph() {
        this.V = 0;
        this.E = 0;
        adj = new HashMap<>();
    }

    public void addVertex(int id) {
        if (!adj.containsKey(id)) {
            adj.put(id, new User(PageRank.INITIAL_RANK));
            V++;
        }
    }

    public void addEdge(DirectedEdge e) {
        User user = adj.get(e.getFrom());
        user.addEdge(e);
        E++;
    }

    public Collection<User> getUsers() {
        return adj.values();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }
}