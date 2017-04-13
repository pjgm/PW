package guided_project.search;


import java.util.LinkedList;

import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.User;

public class PageRank {

    public static final int INITIAL_RANK = 1;
    static final double DAMPING = 0.85;
    static final int ITERATIONS = 10;
    private double maxValue = Double.MIN_VALUE;
    private double minValue = Double.MAX_VALUE;

    private EdgeWeightedDigraph graph;
    private Parser parser;

    public PageRank() {
        parser = new Parser();
        graph = parser.getGraph();
        computePageRank(ITERATIONS);
    }

    public void computePageRank(int iterations) {
        for(int i = 0; i < iterations; i++) {
            for(User u : graph.getUsers()) {
            	if(i==0)
            		u.setRank(1/(double)graph.getNumOfVertex());
                double sum = 0;
                for (int e : graph.getInLinks(u)) {
                    LinkedList<Integer> outLinks = graph.getOutLinks(graph.getVertex(e));
                    sum += graph.getVertex(e).getRank() / (double)outLinks.size();
                }
                double newRank = ((1 - DAMPING) / graph.getNumOfVertex()) + (DAMPING * sum);
                u.setRank(newRank);
                if(newRank < minValue)
                	minValue = newRank;
                if(newRank > maxValue)
                	maxValue = newRank;
            }
        }
    }

    EdgeWeightedDigraph getGraph() {
        return graph;
    }
    
    public double getMinValue(){
    	return minValue;
    }
    
    public double getMaxValue(){
    	return maxValue;
    }
    
    public void updateValue(int id, double value){
    	if(value < minValue)
        	minValue = value;
        if(value > maxValue)
        	maxValue = value;
        User user = graph.getVertex(id);
        user.setRank(value);
        graph.updateUser(id, user);
    }
    
    public Parser getParser(){
    	return parser;
    }
}
