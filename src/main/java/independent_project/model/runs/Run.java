package independent_project.model.runs;

public class Run {

    private String date;
    private String topic_id;
    private String iter; // Q0
    private long tweet_id;
    private int rank;
    private float score;
    private String runTag;

    public Run(String date, String topic_id, String iter, long tweet_id, int rank, float score, String runTag) {
        this.date = date;
        this.topic_id = topic_id;
        this.iter = iter;
        this.tweet_id = tweet_id;
        this.rank = rank;
        this.score = score;
        this.runTag = runTag;
    }

    public String toString() {
        return date + "\t" + topic_id + "\t" + iter + "\t" + tweet_id + "\t" + rank + "\t" + score + "\t" + runTag;
    }
}
