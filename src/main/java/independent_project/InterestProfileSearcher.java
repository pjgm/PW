package independent_project;

import independent_project.config.Config;
import independent_project.config.ConfigParser;
import independent_project.model.interest_profile.Topic;
import independent_project.model.runs.Run;
import independent_project.model.twitter.Tweet;
import independent_project.parsing.InterestParser;
import independent_project.parsing.TweetParser;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InterestProfileSearcher {

    public InterestProfileSearcher() throws IOException, ParseException {
        Config config = ConfigParser.parseFile("src/main/java/independent_project/config/cfg.txt");

        List<Tweet> tweets = TweetParser.parseFile(config.getTweetsPath());
        List<Topic> topics = InterestParser.parseFile(config.getTopicsPath());

        Collections.sort(tweets, Comparator.comparing(t -> t.created_at)); // orders the tweets by date (ascending)

        tweets.remove(0); // Mon Aug 01 19:51:04 WEST 2016
        tweets.remove(tweets.size()-1); // Thu Aug 18 18:20:38 WEST 2016

        Analyzer analyzer = new Analyzer();
        Indexer indexer = new Indexer(Paths.get(config.getIndexPath()), analyzer);
        IndexWriter iw = indexer.openIndex();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String day = dateFormat.format(tweets.get(0).created_at);
        int initDayIndex = 0; // starting index for a given day

        List<Run> runs = new ArrayList<>();

        for (Tweet t: tweets) {
            String currentDay = dateFormat.format(t.created_at);
            if (currentDay.equals(day))
                continue;

            int endDayIndex = tweets.indexOf(t); // ending index for a given day
            indexTweets(indexer, iw, tweets.subList(initDayIndex, endDayIndex));
            runs.addAll(searchTopics(indexer, topics, day));
            day = currentDay;
            initDayIndex = endDayIndex;
        }

        iw.close();

        PrintWriter pw = new PrintWriter(config.getResultsPath());

        for (Run r: runs)
            pw.println(r.toString());

        pw.close();
    }

    /**
     * Indexes a list of tweets
     */
    private void indexTweets(Indexer indexer, IndexWriter iw, List<Tweet> tweets) throws IOException {
        indexer.indexTweets(iw, tweets);
    }

    /**
     * Searches for interest profiles in a given indexer
     * @return list of runs
     */
    private List<Run> searchTopics(Indexer indexer, List<Topic> topics, String day) throws IOException,
            ParseException {
        return indexer.searchInterestTopics(topics, day);
    }


    public static void main(String args[]) throws IOException, ParseException {
        new InterestProfileSearcher();
    }
}
