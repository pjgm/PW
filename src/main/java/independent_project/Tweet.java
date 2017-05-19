package independent_project;

public class Tweet {

    String created_at;
    long id; //always equal to id_str
    String text;
    String source;
    boolean truncated;

    long in_reply_to_status_id; //always equal to in_reply_to_status_id_str
    long in_reply_to_user_id; //always equal to in_reply_to_user_id_str
    String in_reply_to_screen_name;

    User user;

    //String geo;
    //String coordinates;
    //String place;
    String contributors;
    boolean is_quote_status;
    int retweet_count;
    int favorite_count;

    boolean favorited;
    boolean retweeted;
    String filter_level;
    String lang;
    String timestamp_ms;
}
