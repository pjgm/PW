package guided_project.search;

import guided_project.graph.DirectedEdge;
import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.User;

import java.util.LinkedList;

public class PageRank {

    public static final int INITIAL_RANK = 1;
    static final double DAMPING = 0.85;
    static final int ITERATIONS = 10;

    private EdgeWeightedDigraph graph;


    private PageRank() {
        Parser parser = new Parser();
        this.graph = parser.getGraph();
        for (User u : graph.getUsers()) {
            u.setRank(1/(double)graph.V());
        }

        double sum = 0;
        for (User u : graph.getUsers()) {
            sum += u.getRank();
        }
        System.out.println(sum);

        computePageRank(ITERATIONS);

        sum = 0;
        for (User u : graph.getUsers()) {
            sum += u.getRank();
        }
        System.out.println(sum);
    }

    public void computePageRank(int iterations) {
        for(int i = 0; i < iterations; i++) {
            for(User u : graph.getUsers()) {
                double sum = 0;
                for (int e : graph.getInLinks(u)) {
                    LinkedList outLinks = graph.getOutLinks(graph.getVertex(e));
                    sum += graph.getVertex(e).getRank() / outLinks.size();
                }
                double newRank = ((1 - DAMPING) / graph.V()) + (DAMPING * sum);
                u.setRank(newRank);
            }
        }
    }

    public static void main(String args[]) {
        PageRank pr = new PageRank();
    }
}
