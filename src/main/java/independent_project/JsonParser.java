package independent_project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private String tweetsPath = "src/main/java/independent_project/tweets.jsonl";

    public JsonParser() {

        Gson gson = new Gson();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(tweetsPath));

            String line;
            JsonReader reader;
            List<Tweet> tweets = new ArrayList<>();

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                reader = new JsonReader(new StringReader(line));
                tweets.add(gson.fromJson(reader, Tweet.class));
            }
            bufferedReader.close();

            for (Tweet t : tweets) {
                System.out.println(t.user.location);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        JsonParser parser = new JsonParser();
    }

}
