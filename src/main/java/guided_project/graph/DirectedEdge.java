package guided_project.graph;

public class DirectedEdge {

    private int linkedUser;

    public DirectedEdge(int linkedUser) {
        this.linkedUser = linkedUser;
    }

    public int getLinkedUser() {
        return linkedUser;
    }

    public void setLinkedUser(int linkedUser) {
        this.linkedUser = linkedUser;
    }
}
