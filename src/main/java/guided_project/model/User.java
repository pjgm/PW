package guided_project.model;

public class User {

    private double rank;
    private int id;

    public User(int id) {
        rank = 0;
        this.id = id;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}