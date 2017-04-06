package guided_project.model;

import java.util.Date;

public class Question {

    private int id;
    private int ownerUserId;
    private Date creationDate;
    private int score;
    private String title;
    private String body;

    public Question(int id, int ownerUserId, Date creationDate, int score, String title, String body) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.creationDate = creationDate;
        this.score = score;
        this.title = title;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
