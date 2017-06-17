package independent_project.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigParser {

    public static final String TOPICS_PATH = "topics_path";
    public static final String TWEETS_PATH = "tweets_path";
    public static final String INDEX_PATH = "index_path";
    public static final String RESULTS_PATH = "results_path";
    public static final String QRELS_PATH = "qrels_path";
    public static final String TOP_QRELS_PATH = "top_qrels_path";
    public static final String INPUT_PATH = "input_path";

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
            case TOPICS_PATH:
                parseTopicsPath(value, config);
                break;
            case TWEETS_PATH:
                parseTweetsPath(value, config);
                break;
            case INDEX_PATH:
                parseIndexPath(value, config);
                break;
            case RESULTS_PATH:
                parseResultsPath(value, config);
                break;
            case QRELS_PATH:
            	parseQRelsPath(value, config);
            	break;
            case TOP_QRELS_PATH:
                parseTopQRelsPath(value, config);
                break;
            case INPUT_PATH:
                parseInputPath(value, config);
                break;
        }
    }

    private static void parseResultsPath(String value, Config config) {
        config.setResultsPath(value);
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

    private static void parseQRelsPath(String value, Config config) {
        config.setQRelsPath(value);
    }

    private static void parseTopQRelsPath(String value, Config config) {
        config.setTopQRelsPath(value);
    }

    private static void parseInputPath(String value, Config config) {
        config.setInputPath(value);
    }
}
