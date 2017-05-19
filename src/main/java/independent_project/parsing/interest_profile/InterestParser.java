package independent_project.parsing.interest_profile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InterestParser {
    //private String path = "src/main/java/independent_project/data/TREC2015-MB-eval-topics.json";
    private String path = "src/main/java/independent_project/data/TREC2015-MB-noeval-topics-culled.json";
    //private String path = "src/main/java/independent_project/data/TREC2016-RTS-topics.json";



    public InterestParser() {
        try {
            Type listType = new TypeToken<ArrayList<Topic>>(){}.getType();

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(path));

            List<Topic> topics = gson.fromJson(reader, listType);

            for (Topic t: topics) {
                System.out.println(t.topid);
                System.out.println(t.title);
                System.out.println(t.description);
                System.out.println(t.narrative);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new InterestParser();
    }
}
