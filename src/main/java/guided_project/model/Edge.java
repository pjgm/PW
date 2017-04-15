package guided_project.model;

public class Edge {

	private int id;
	private double weight;
	
	
	public Edge(int id, double weight) {
		super();
		this.id = id;
		this.weight = weight;
	}
	
	public int getId() {
		return id;
	}
	public double getWeight() {
		return weight;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
