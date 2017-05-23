package independent_project.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import independent_project.model.twitter.Tweet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TweetParser {

    public static List<Tweet> parseFile(String filePath) throws IOException {
        //Sat Aug 06 12:39:04 +0000 2016
        Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss Z yyyy").create();

        String line;
        JsonReader reader;
        List<Tweet> tweets = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        while((line = bufferedReader.readLine()) != null) {
            reader = new JsonReader(new StringReader(line));
            tweets.add(gson.fromJson(reader, Tweet.class));
        }
        bufferedReader.close();

        return tweets;
    }

}
