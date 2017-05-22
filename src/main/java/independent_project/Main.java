package independent_project;

import independent_project.config.Config;
import independent_project.config.ConfigParser;
import independent_project.parsing.InterestParser;
import independent_project.model.interest_profile.Topic;
import independent_project.model.twitter.Tweet;
import independent_project.parsing.TweetParser;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException {

        Config config = ConfigParser.parseFile("src/main/java/independent_project/config/cfg.txt");

        List<Tweet> tweets = TweetParser.parseFile(config.getTweetsPath());
        List<Topic> topics = InterestParser.parseFile(config.getTopicsPath());

        InterestParser interestParser = new InterestParser();
        TweetParser tweetParser = new TweetParser();
        Analyzer analyzer = new Analyzer();
        Indexer indexer = new Indexer();

        IndexWriter iw = indexer.openIndex(analyzer, Paths.get(config.getIndexPath()));
    }
}
