package independent_project.parsing.tweet;

class User {
    long id; //equals id_str
    String name;
    String screen_name;
    String location;
    String url;
    String description;
    boolean is_protected;
    boolean verified;
    int followers_count;
    int friends_count;
    int listed_count;
    int favourites_count;
    int statuses_count;
    String created_at;
    int utc_offset;
    String time_zone;
    boolean geo_enabled;
    String lang;
    boolean contributors_enabled;
    boolean is_translator;
    String profile_background_color;
    String profile_background_image_url;
    String profile_background_image_url_https;
    boolean profile_background_tile;
    String profile_link_color;
    String profile_sidebar_border_color;
    String profile_sidebar_fill_color;
    String profile_text_color;
    boolean profile_use_background_image;
    String profile_image_url;
    String profile_image_url_https;
    boolean default_profile;
    boolean default_profile_image;
    //String following; <- always null in the dataset
    //String follow_request_sent; <- always null in the dataset
    //String notifications; <- always null in the dataset
}
