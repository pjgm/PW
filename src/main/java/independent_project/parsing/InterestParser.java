package independent_project.parsing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import independent_project.model.interest_profile.Topic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InterestParser {
    public static List<Topic> parseFile(String filePath) throws FileNotFoundException {
        Type listType = new TypeToken<ArrayList<Topic>>(){}.getType();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        return gson.fromJson(reader, listType);
    }
}
