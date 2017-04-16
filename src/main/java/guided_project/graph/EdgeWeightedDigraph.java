package guided_project.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import guided_project.model.User;

public class EdgeWeightedDigraph {

    private int numOfVertex;
    private int numOfEdges;

    private Map<Integer, User> adj;
    private Map<User, LinkedList<Edge>> inlinks;
    private Map<User, LinkedList<Edge>> outLinks;

    public EdgeWeightedDigraph() {
        numOfVertex = 0;
        numOfEdges = 0;
        adj = new HashMap<Integer, User>();
        inlinks = new HashMap<User, LinkedList<Edge>>();
        outLinks = new HashMap<User, LinkedList<Edge>>();
    }

    public void addVertex(int id) {
        if (!adj.containsKey(id)) {
            User u = new User(id);
            adj.put(id, u);
            inlinks.put(u, new LinkedList<Edge>());
            outLinks.put(u, new LinkedList<Edge>());
            numOfVertex++;
        }
    }

    public User getVertex(int id) {
        return adj.get(id);
    }

    public void addEdge(int src, int srcWeight, int dst, int dstWeight) {
    	//src = answer
        LinkedList<Edge> srcOutLinks = outLinks.get(adj.get(src));
        srcOutLinks.add(new Edge(dst, (0.5*srcWeight)+(0.5*dstWeight)));
        
        //dst = question
        LinkedList<Edge> dstInLinks = inlinks.get(adj.get(dst));
        dstInLinks.add(new Edge(src, (0.5*srcWeight)+(0.5*dstWeight)));
        numOfEdges++;
    }

    public LinkedList<Edge> getInLinks(User u) {
        return inlinks.get(u);
    }

    public LinkedList<Edge> getOutLinks(User u) {
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