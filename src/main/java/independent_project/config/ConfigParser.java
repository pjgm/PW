package independent_project.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ConfigParser {

    public static Config parseFile(String configPath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(configPath));
        Config config = new Config();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#") || line.length() == 0)
                continue;
            parseFields(line, config);
        }
        return  config;
    }

    private static void parseFields(String line, Config config) {
        String parts[] = line.split(":", 2);
        String field = parts[0].trim().toLowerCase();
        String value = parts[1].trim();

        switch (field) {
            case "topics_path":
                parseTopicsPath(value, config);
                break;
            case "tweets_path":
                parseTweetsPath(value, config);
                break;
            case "index_path":
                parseIndexPath(value, config);
        }
    }

    private static void parseTopicsPath(String value, Config config) {
        config.setTopicsPath(value);
    }

    private static void parseTweetsPath(String value, Config config) {
        config.setTweetsPath(value);
    }

    private static void parseIndexPath(String value, Config config) {
        config.setIndexPath(value);
    }
}
