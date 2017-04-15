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
		for (int x = 0; x < iterations; x++) {
			for (User i : graph.getUsers()) {
				if (x == 0) {
					i.setRank(1 / (double) graph.getNumOfVertex());
				} else {
					double sum = 0;
					for (int j : graph.getInLinks(i)) {
						LinkedList<Integer> outLinks = graph.getOutLinks(graph.getVertex(j));
						sum += graph.getVertex(j).getRank() / (double) outLinks.size();
					}
					double newRank = ((1 - DAMPING) / graph.getNumOfVertex()) + (DAMPING * sum);
					i.setRank(newRank);

					if (newRank < minValue)
						minValue = newRank;
					
					if (newRank > maxValue)
						maxValue = newRank;
				}
			}
		}
		normalize(maxValue, minValue);
	}

	private void normalize(double maxValue, double minValue) {
		for (User u : graph.getUsers()) {
			double newRank = (u.getRank() - minValue) / (maxValue - minValue);
			this.updateValue(u.getId(), newRank);
		}
	}

	EdgeWeightedDigraph getGraph() {
		return graph;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void updateValue(int id, double value) {
		if (value < minValue)
			minValue = value;
		if (value > maxValue)
			maxValue = value;
		User user = graph.getVertex(id);
		user.setRank(value);
		graph.updateUser(id, user);
	}

	public Parser getParser() {
		return parser;
	}
}
