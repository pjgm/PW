package guided_project.search;

import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.User;

import java.util.LinkedList;
import java.util.Set;

public class PageRank {

    public static final int INITIAL_RANK = 1;
    static final double DAMPING = 0.85;
    static final int ITERATIONS = 10;

    private EdgeWeightedDigraph graph;

    PageRank() {
        Parser parser = new Parser();
        this.graph = parser.getGraph();
        for (User u : graph.getUsers()) {
            u.setRank(1/(double)graph.V());
        }
        computePageRank(ITERATIONS);

        double sum = 0;
        for (User u: graph.getUsers()) {
            sum += u.getRank();
        }
        System.out.println(sum);
    }

    public void computePageRank(int iterations) {
        for(int i = 0; i < iterations; i++) {
            for(User u : graph.getUsers()) {
                double sum = 0;
                for (int e : graph.getInLinks(u)) {
                    Set outLinks = graph.getOutLinks(graph.getVertex(e));
                    sum += graph.getVertex(e).getRank() / (double) outLinks.size();
                }
                double newRank = ((1 - DAMPING) / (double) graph.V()) + (DAMPING * sum);
                u.setRank(newRank);
            }
        }
    }

    private void normalizePR() {

    }

    EdgeWeightedDigraph getGraph() {
        return graph;
    }
}
