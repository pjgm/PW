package guided_project.graph;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EdgeWeightedDigraph {

    private int numOfVertex;
    private int numOfEdges;

    private Map<Integer, User> adj;
    private Map<User, LinkedList<Integer>> inlinks;
    private Map<User, LinkedList<Integer>> outLinks;

    public EdgeWeightedDigraph() {
        numOfVertex = 0;
        numOfEdges = 0;
        adj = new HashMap<Integer, User>();
        inlinks = new HashMap<User, LinkedList<Integer>>();
        outLinks = new HashMap<User, LinkedList<Integer>>();
    }

    public void addVertex(int id) {
        if (!adj.containsKey(id)) {
            User u = new User();
            adj.put(id, u);
            inlinks.put(u, new LinkedList<Integer>());
            outLinks.put(u, new LinkedList<Integer>());
            numOfVertex++;
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
        numOfEdges++;
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

    public int getNumOfVertex() {
        return numOfVertex;
    }

    public void updateUser(int id, User user){
    	adj.put(id, user);
    }
    public int getNumOfEdges() {
        return numOfEdges;
    }
}