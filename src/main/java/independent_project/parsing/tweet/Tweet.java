package independent_project.parsing.tweet;

class Tweet {

    String created_at;
    long id; //always equal to id_str
    String text;
    String source;
    boolean truncated;

    long in_reply_to_status_id; //always equal to in_reply_to_status_id_str
    long in_reply_to_user_id; //always equal to in_reply_to_user_id_str
    String in_reply_to_screen_name;

    User user;

    Geolocation geo;
    Coordinates coordinates;
    Place place;
    //String contributors; <- always null
    boolean is_quote_status;
    int retweet_count;
    int favorite_count;

    Entities entities;

    boolean favorited;
    boolean retweeted;
    String filter_level;
    String lang;
    String timestamp_ms;
}
