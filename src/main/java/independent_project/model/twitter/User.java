package independent_project.model.twitter;

import java.util.Date;

public class User {
    public long id; //equals id_str
    public String name;
    public String screen_name;
    public String location;
    public String url;
    public String description;
    public boolean is_protected;
    public boolean verified;
    public int followers_count;
    public int friends_count;
    public int listed_count;
    public int favourites_count;
    public int statuses_count;
    public Date created_at;
    public int utc_offset;
    public String time_zone;
    public boolean geo_enabled;
    public String lang;
    public boolean contributors_enabled;
    public boolean is_translator;
    public String profile_background_color;
    public String profile_background_image_url;
    public String profile_background_image_url_https;
    public boolean profile_background_tile;
    public String profile_link_color;
    public String profile_sidebar_border_color;
    public String profile_sidebar_fill_color;
    public String profile_text_color;
    public boolean profile_use_background_image;
    public String profile_image_url;
    public String profile_image_url_https;
    public boolean default_profile;
    public boolean default_profile_image;
    //String following; <- always null in the dataset
    //String follow_request_sent; <- always null in the dataset
    //String notifications; <- always null in the dataset
}
