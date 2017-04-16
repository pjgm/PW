package guided_project.search;

import java.util.LinkedList;

import guided_project.graph.Edge;
import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.User;

public class PageRank {

	private double maxValue = Double.MIN_VALUE;
	private double minValue = Double.MAX_VALUE;

	private EdgeWeightedDigraph graph;
	private Parser parser;

	public PageRank() {
		parser = new Parser();
		graph = parser.getGraph();
		computePageRank(Properties.ITERATIONS);
	}

	public void computePageRank(int iterations) {
		for (int x = 0; x < iterations; x++) {
			for (User i : graph.getUsers()) {
				double newRank;
				if (x == 0) {
					newRank = 1 / (double) graph.getNumOfVertex();
				} else {
					double sum = 0;
					for (Edge j : graph.getInLinks(i)) {
						LinkedList<Edge> outLinks = graph.getOutLinks(graph.getVertex(j.getId()));
						sum += graph.getVertex(j.getId()).getRank() / (double) outLinks.size();
					}
					newRank = ((1 - Properties.DAMPING) / graph.getNumOfVertex()) + (Properties.DAMPING * sum);
				}
				
				boolean lastIteration = (x == iterations-1);
				this.updateValue(i.getId(), newRank, lastIteration); 
			}
		}
		
		if (Properties.DEBUGMODE) {
			System.out.println("DEBUG Max PageRank Before Normalization: " + getMaxValue());
			System.out.println("DEBUG Min PageRank Before Normalization: " + getMinValue());
		}

		normalize(maxValue, minValue);

		if (Properties.DEBUGMODE) {
			System.out.println("DEBUG Max PageRank After Normalization: " + getMaxValue());
			System.out.println("DEBUG Min PageRank After Normalization: " + getMinValue());
		}
	}

	private void normalize(double maxValue, double minValue) {
		this.maxValue = Double.MIN_VALUE;
		this.minValue = Double.MAX_VALUE;
		for (User u : graph.getUsers()) {
			double newRank = (u.getRank() - minValue) / (maxValue - minValue);
			this.updateValue(u.getId(), newRank, true);
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

	public void updateValue(int id, double value, boolean updateMinMax) {
		if (updateMinMax) {
			if (value < minValue)
				minValue = value;

			if (value > maxValue)
				maxValue = value;
		}
		
		User user = graph.getVertex(id);
		user.setRank(value);
		graph.updateUser(id, user);
	}

	public Parser getParser() {
		return parser;
	}
}
