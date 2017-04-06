package guided_project.search;

import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.User;

public class PageRank {

    public static final int INITIAL_RANK = 1;
    static final double DAMPING = 0.85;


    private PageRank() {
        Parser parser = new Parser();
        EdgeWeightedDigraph graph = parser.getGraph();
        for (User u : graph.getUsers()) {
            u.setRank(1/(double)graph.V());
        }
    }

    public void computePageRank(int iterations) {
        for(int i = 0; i < iterations; i++) {

        }
    }

    public static void main(String args[]) {
        PageRank pr = new PageRank();

    }
}
