package guided_project.search;

import guided_project.model.Answer;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

class Indexer {

    private static double alfa = 0.5;
    private static String indexPath = "index";
    private static String queriesPath = "src/main/java/lab0/evaluation/queries.txt";

    IndexWriter openIndex(Analyzer analyzer) throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index instead of appending to existing index
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        return new IndexWriter(dir, iwc);
    }

    void indexDocuments(IndexWriter iw, List<Answer> answers) throws IOException {

        for (Answer a : answers) {
            Document doc = new Document();

            doc.add(new StoredField("Id", a.getId()));

            doc.add(new IntPoint("OwnerUserId", a.getOwnerUserId()));
            doc.add(new StoredField("OwnerUserId", a.getOwnerUserId()));

            doc.add(new LongPoint("CreationDate", a.getCreationDate().getTime()));
            doc.add(new StoredField("CreationDate", a.getCreationDate().getTime()));

            doc.add(new IntPoint("ParentId", a.getParentId()));
            doc.add(new StoredField("ParentId", a.getParentId()));

            doc.add(new IntPoint("Score", a.getScore()));
            doc.add(new StoredField("Score", a.getScore()));

            doc.add(new TextField("Body", a.getBody(), Field.Store.YES));

            iw.addDocument(doc);
        }
    }

    void parseQueries(Analyzer analyzer) throws IOException, ParseException {
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

        BufferedReader br = new BufferedReader(new FileReader(queriesPath));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":", 2);
            searchQuery(writer, analyzer, parts[0], parts[1]);
        }

        writer.close();
    }

    private void searchQuery(PrintWriter writer, Analyzer analyzer, String queryID, String queryStr) throws IOException,
            ParseException {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));

        IndexSearcher searcher = new IndexSearcher(reader);
//        searcher.setSimilarity(new ClassicSimilarity());
        QueryParser parser = new QueryParser("Body", analyzer);

        Query query = parser.parse(queryStr);

        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;

        System.out.println("Query number " + queryID);
        System.out.println("Query body " + queryStr);
        System.out.println(numTotalHits + " total matching documents");

        computeCombinedScore(hits);

        int rank = 1;

        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            writer.println(queryID + "\t" + "Q0" + "\t" + doc.getField("Id").numericValue().intValue() + "\t" +
                    rank++ + "\t" + hit.score + "\t" + "run-1");
        }
    }

    private void computeCombinedScore(ScoreDoc[] hits) {
        Document doc;
        for (ScoreDoc hit : hits) {
            System.out.println(hit.score);
        }
    }
}
