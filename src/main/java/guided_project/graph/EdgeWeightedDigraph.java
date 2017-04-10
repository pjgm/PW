package guided_project.graph;

import guided_project.model.User;
import guided_project.search.PageRank;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EdgeWeightedDigraph {

    private int V;
    private int E;

    private Map<Integer, User> adj;
    private Map<User, LinkedList<Integer>> inlinks;
    private Map<User, LinkedList<Integer>> outLinks;

    public EdgeWeightedDigraph() {
        this.V = 0;
        this.E = 0;
        adj = new HashMap<>();
        inlinks = new HashMap<>();
        outLinks = new HashMap<>();
    }

    public void addVertex(int id) {
        if (!adj.containsKey(id)) {
            User u = new User(PageRank.INITIAL_RANK);
            adj.put(id, u);
            inlinks.put(u, new LinkedList<>());
            outLinks.put(u, new LinkedList<>());
            V++;
        }
    }

    public User getVertex(int id) {
        return adj.get(id);
    }

    public void addEdge(int src, int dst) {
        LinkedList<Integer> srcOutLinks = outLinks.get(adj.get(src));
        srcOutLinks.add(dst);
        LinkedList<Integer> dstInLinks = inlinks.get(adj.get(dst));
        dstInLinks.add(src);
        E++;
    }

    public LinkedList<Integer> getInLinks(User u) {
        return inlinks.get(u);
    }

    public LinkedList<Integer> getOutLinks(User u) {
        return outLinks.get(u);
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