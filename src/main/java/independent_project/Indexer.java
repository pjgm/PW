package independent_project;

import independent_project.model.interest_profile.Topic;
import independent_project.model.runs.Run;
import independent_project.model.twitter.Tweet;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class Indexer {

    public static final String TFIDF = "TFIDF";
    public static final String BM25 = "BM25";
    public static final String LMD = "LMD";
    public static final long HALFEVENT = 1470484800;

    private Path indexPath;
    private Analyzer analyzer;
    //Key->Day ; Value -> <Key -> TopicId ; Value -> List of Documents >(Tweets))
    private Map<String,Map<String,List<Document>>> topTweets;

    public Indexer(Path indexPath, Analyzer analyzer) {
        this.indexPath = indexPath;
        this.analyzer = analyzer;
        this.topTweets = new HashMap();
    }

    IndexWriter openIndex() throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = FSDirectory.open(indexPath);
        return new IndexWriter(dir, iwc);
    }

    void indexTweets(IndexWriter writer, List<Tweet> tweets) throws IOException {
        for (Tweet t: tweets) {
            Document doc = new Document();
            doc.add(new StoredField("id", t.id));

            doc.add(new StringField("created_at", t.created_at.toString(), Field.Store.YES));

            doc.add(new TextField("text", t.text, Field.Store.YES));

            doc.add(new StringField("source", t.source, Field.Store.YES));

            doc.add(new LongPoint("timestamp_ms", t.timestamp_ms));
            doc.add(new StoredField("timestamp_ms", t.timestamp_ms));
            // add more when needed
            writer.addDocument(doc);
        }
        writer.commit();
    }

    List<Run> searchInterestTopics(List<Topic> topics, String day, String simMode, boolean tempEv) throws IOException, ParseException, java.text.ParseException {

        List<Run> runs = new ArrayList<Run>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));

        IndexSearcher searcher = new IndexSearcher(reader);

        switch (simMode){
            case TFIDF:
                searcher.setSimilarity(new ClassicSimilarity());
                break;
            case BM25:
                searcher.setSimilarity(new BM25Similarity());
                break;
            case LMD:
                searcher.setSimilarity(new LMDirichletSimilarity());
                break;
        }

        QueryParser parser = new QueryParser("text", analyzer); // only considering tweet text

        for (Topic topic : topics) {
            List<Run> topicRuns = searchInterestTopic(topic, parser, searcher, day, tempEv);
            runs.addAll(topicRuns);
        }
        return runs;
    }

    List<Run> searchInterestTopic(Topic topic, QueryParser parser, IndexSearcher searcher, String day, boolean tempEv) throws
            ParseException, IOException, java.text.ParseException {

        List<Run> runs = new ArrayList<>();

        Query query = parser.parse(topic.title); // TODO: Use description or narrative

        System.out.println(query.toString());

        TopDocs results = searcher.search(query, 50);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;

        System.out.println("Query number: " + topic.topid);
        System.out.println("Query body: " + topic.title);
        System.out.println(numTotalHits + " total matching documents");

        int rank = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date dayDate = dateFormat.parse(day);
        long todayTimeStamp = dayDate.getTime();

        if(tempEv) {
            System.out.println("ENTREI");
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                long tweetTimestamp = doc.getField("timestamp_ms").numericValue().longValue();
                System.out.println("ORIGINAL: "+hit.score);
                hit.score = hit.score * tweetTimeDecayScore(todayTimeStamp, tweetTimestamp);
                System.out.println("COMBINADO: "+hit.score);
                //String date = "YYYYMMDD"; //TODO: change to day for which result was calculated
            }
            Arrays.sort(hits, Comparator.comparing((ScoreDoc h) -> h.score).reversed());
        }

        if(!topTweets.containsKey(day))
            topTweets.put(day, new HashMap());

        List<Document> topDocs = new LinkedList();

        for (int i=0; i<10; i++){
            topDocs.add(searcher.doc(hits[i].doc));
            //System.out.println("Score : "+i+" "+hits[i].score);
            long tweet_id = searcher.doc(hits[i].doc).getField("id").numericValue().longValue();
            Run run = new Run(day, topic.topid, "Q0", tweet_id, rank++, hits[i].score, "run-1");
            runs.add(run);
        }
        topTweets.get(day).put(topic.topid, topDocs);

        return runs;
    }

    private float tweetTimeDecayScore(long queryTimeStamp, long tweetTimestamp){
        //System.out.println("QUERYTIME: "+queryTimeStamp);
        //System.out.println("TWEETTIME: "+tweetTimestamp);
        //System.out.println("CONTAS: "+(float)(queryTimeStamp-tweetTimestamp)/HALFEVENT);
        double denominator = Math.pow(2,(float)(queryTimeStamp-tweetTimestamp)/HALFEVENT);
        //System.out.println("DENOMINATOR: "+denominator);
        System.out.println("RESULT: "+(float)(1/denominator));
        return (float)(1/denominator);
    }
}
