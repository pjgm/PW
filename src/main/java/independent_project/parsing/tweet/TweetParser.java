package independent_project.parsing.tweet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TweetParser {

    private String path = "src/main/java/independent_project/data/tweets.jsonl";

    public TweetParser() {

        Gson gson = new Gson();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            String line;
            JsonReader reader;
            List<Tweet> tweets = new ArrayList<>();

            while((line = bufferedReader.readLine()) != null) {
                reader = new JsonReader(new StringReader(line));
                tweets.add(gson.fromJson(reader, Tweet.class));
            }
            bufferedReader.close();

            for (Tweet t : tweets) {
                System.out.println(t.created_at);
            }

        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        TweetParser parser = new TweetParser();
    }

}
