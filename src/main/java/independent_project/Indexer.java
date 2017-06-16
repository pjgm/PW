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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Indexer {

    private Path indexPath;
    private Analyzer analyzer;

    public Indexer(Path indexPath, Analyzer analyzer) {
        this.indexPath = indexPath;
        this.analyzer = analyzer;
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
            // add more when needed
            writer.addDocument(doc);
        }
        writer.commit();
    }

    List<Run> searchInterestTopics(List<Topic> topics, String day) throws IOException, ParseException {

        List<Run> runs = new ArrayList<Run>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));

        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity()); // TF-IDF
//        searcher.setSimilarity(new LMDirichletSimilarity()); // LMD
//        searcher.setSimilarity(new BM25Similarity()); //BM25

  
        
        QueryParser parser = new QueryParser("text", analyzer); // only considering tweet text

        for (Topic topic : topics) {
            List<Run> topicRuns = searchInterestTopic(topic, parser, searcher, day);
            runs.addAll(topicRuns);
        }
        return runs;
    }

    List<Run> searchInterestTopic(Topic topic, QueryParser parser, IndexSearcher searcher, String day) throws
            ParseException, IOException {

        List<Run> runs = new ArrayList<>();

        Query query = parser.parse(topic.title); // TODO: Use description or narrative

        System.out.println(query.toString());

        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;

        System.out.println("Query number: " + topic.topid);
        System.out.println("Query body: " + topic.title);
        System.out.println(numTotalHits + " total matching documents");

        int rank = 1;

        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);

            //String date = "YYYYMMDD"; //TODO: change to day for which result was calculated
            long tweet_id = doc.getField("id").numericValue().longValue();

            Run run = new Run(day, topic.topid, "Q0", tweet_id, rank++, hit.score, "run-1");
            runs.add(run);
        }

        return runs;
    }
}
