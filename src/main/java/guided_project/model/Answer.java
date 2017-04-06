package guided_project.model;

import java.util.Date;

public class Answer {

    private int id;
    private int ownerUserId;
    private Date creationDate;
    private int parentId;
    private int score;
    private String body;

    public Answer (int id, int ownerUserId, Date creationDate, int parentId, int score, String body) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.creationDate = creationDate;
        this.parentId = parentId;
        this.score = score;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
