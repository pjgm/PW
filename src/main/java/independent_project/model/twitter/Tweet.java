package independent_project.model.twitter;

import java.util.Date;

public class Tweet {

    public Date created_at;
    public long id; //always equal to id_str
    public String text;
    public String source;
    public boolean truncated;

    public long in_reply_to_status_id; //always equal to in_reply_to_status_id_str
    public long in_reply_to_user_id; //always equal to in_reply_to_user_id_str
    public String in_reply_to_screen_name;

    public User user;

    public Geolocation geo;
    public Coordinates coordinates;
    public Place place;
    //String contributors; <- always null
    public boolean is_quote_status;
    public int retweet_count;
    public int favorite_count;

    public Entities entities;

    public boolean favorited;
    public boolean retweeted;
    public String filter_level;
    public String lang;
    public String timestamp_ms;
}
