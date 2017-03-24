package guided_project;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Indexer {

    private static String indexPath = "index";
    @SuppressWarnings("FieldCanBeLocal")
    private static String documentsPath = "Answers.csv";
    private static String queriesPath = "src/main/java/lab0/evaluation/queries.txt";

    private IndexWriter openIndex(Analyzer analyzer) throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index instead of appending to existing index
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        return new IndexWriter(dir, iwc);
    }

    private void indexDocuments(IndexWriter iw) throws java.text.ParseException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(documentsPath));
        StringBuilder sb = new StringBuilder();
        br.readLine();
        String line;

        while ((line = br.readLine()) != null) {

            if (line.length() == 0)
                continue;

            sb.append(line).append(System.lineSeparator());

            if (line.equals("\"")) {
                indexDoc(iw, sb.toString());
                sb.setLength(0);
            }
        }
    }

    private void indexDoc(IndexWriter iw, String documentStr) throws java.text.ParseException, IOException {
        // Each document is organized as:
        // Id,OwnerUserId,CreationDate,ParentId,Score,Body
        Document doc = new Document();
        String[] parts = documentStr.split(",", 6);

        // Parse Id
        int id = Integer.parseInt(parts[0]);

        doc.add(new StoredField("Id", id));

        // Parse OwnerUserId
        try {
            int ownerUserId = Integer.parseInt(parts[1]);
            doc.add(new IntPoint("OwnerUserId", ownerUserId));
            doc.add(new StoredField("OwnerUserId", ownerUserId));
        } catch (NumberFormatException e) {
            System.err.println("Error parsing OwnerUserID of document " + id + ". " + "Skipping...");
            return;
        }

        // Parse CreationDate
        Date creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(parts[2]);
        doc.add(new LongPoint("CreationDate", creationDate.getTime()));
        doc.add(new StoredField("CreationDate", creationDate.getTime()));

        // Parse ParentId
        int parentId = Integer.parseInt(parts[3]);
        doc.add(new IntPoint("ParentId", parentId));
        doc.add(new StoredField("ParentId", parentId));

        // Parse Score
        int score = Integer.parseInt(parts[4]);
        doc.add(new IntPoint("Score", score));
        doc.add(new StoredField("Score", score));

        // Parse Body
        doc.add(new TextField("Body", parts[5], Field.Store.YES));

        iw.addDocument(doc);
    }

    private void parseQueries(Analyzer analyzer) throws IOException, ParseException {
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
        QueryParser parser = new QueryParser("Body", analyzer);

        Query query = parser.parse(queryStr);

        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;

        System.out.println("Query number " + queryID);
        System.out.println(numTotalHits + " total matching documents");

        int rank = 1;

        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            writer.println(queryID + "\t" + "Q0" + "\t" + doc.getField("Id").numericValue().intValue() + "\t" +
                    rank++ + "\t" + hit.score + "\t" + "run-1");
        }
    }

    public static void main(String args[]) {

        Analyzer analyzer = new StandardAnalyzer();
        Indexer indexer = new Indexer();

        try {
            IndexWriter iw = indexer.openIndex(analyzer);
            indexer.indexDocuments(iw);
            iw.close();
            indexer.parseQueries(analyzer);
        } catch (IOException e) {
            System.err.println("Error reading file");
        } catch (ParseException e) {
            System.err.println("Error parsing query");
        } catch (java.text.ParseException e) {
            System.err.println("Error parsing document field");
        }
    }
}
