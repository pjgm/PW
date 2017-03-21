package lab0;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Indexer {

    private static String indexPath = "index";
    @SuppressWarnings("FieldCanBeLocal")
    private static String documentsPath = "Answers.csv";
    private static String queriesPath = "src/main/java/lab0/evaluation/queries.txt";

    private IndexWriter openIndex(Analyzer analyzer) {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter iw = null;
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index instead of appending to existing index
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            iw = new IndexWriter(dir, iwc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return iw;
    }

    private void indexDocuments(IndexWriter iw) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void indexDoc(IndexWriter iw, String documentStr) {
        // Each document is organized as:
        // Id,OwnerUserId,CreationDate,ParentId,Score,Body
        Document doc = new Document();
        String[] parts = documentStr.split(",", 6);

        // Parse Id
        int id = Integer.parseInt(parts[0]);
        doc.add(new IntPoint("Id",id));
        doc.add(new StoredField("Id", id));

        // Parse OwnerUserId
        try {
            int ownerUserId = Integer.parseInt(parts[1]);
            doc.add(new IntPoint("OwnerUserId", ownerUserId));
            doc.add(new StoredField("OwnerUserId", ownerUserId));
        } catch (NumberFormatException e) {
            System.out.println("Error parsing OwnerUserId of document with id " + id);
        }

        // Parse CreationDate
        try {
            Date creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(parts[2]);
            doc.add(new LongPoint("CreationDate", creationDate.getTime()));
            doc.add(new StoredField("CreationDate", creationDate.getTime()));
        } catch (ParseException e) {
            System.out.println("Error parsing date of document with id " + id);
        }

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

        try {
            iw.addDocument(doc);
            //System.out.println("Successfully added document " + id);
        } catch (IOException e) {
            System.out.println("Error adding to index document id " + id);
        }

    }

    private void searchIndex(Analyzer analyzer) {
        IndexReader reader = null;

        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        } catch (IOException e) {
            System.out.println("Error opening index");
        }

        if (reader == null)
            throw new NullPointerException("Reader is null");

        IndexSearcher searcher = new IndexSearcher(reader);
        Scanner in = new Scanner(System.in);

        QueryParser parser = new QueryParser("Body", analyzer);

        while (true) {
            System.out.println("Enter query:");
            System.out.print("> ");

            String inputLine = in.nextLine().trim();

            if (inputLine.isEmpty())
                break;

            Query query = null;
            try {
                query = parser.parse(inputLine);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                System.out.println("Error parsing input query");
            }

            if (query == null)
                throw new NullPointerException("Query is null");

            TopDocs results = null;

            try {
                results = searcher.search(query, 5);
            } catch (IOException e) {
                System.out.println("Error searching for results");
            }

            if (results == null)
                throw new NullPointerException("Results is null");

            ScoreDoc[] hits = results.scoreDocs;

            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            for (ScoreDoc hit : hits) {
                Document doc = null;
                try {
                    doc = searcher.doc(hit.doc);
                } catch (IOException e) {
                    System.out.println("Error getting document");
                }

                if (doc == null)
                    throw new NullPointerException("doc is null");

                String answer = doc.get("Body");
                Integer Id = doc.getField("Id").numericValue().intValue();
                System.out.println("DocId: " + Id);
                System.out.println("DocAnswer: " + answer);
                System.out.println();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error closing index reader");
        }
    }

    private void parseQueries(Analyzer analyzer) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("output.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(queriesPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                searchQuery(writer, analyzer, parts[0], parts[1]);
            }

        } catch (IOException e) {
            System.out.println("Error parsing queries file");
        }
        writer.close();
    }

    private void searchQuery(PrintWriter writer, Analyzer analyzer, String queryID, String queryStr) {
        IndexReader reader = null;

        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        } catch (IOException e) {
            System.out.println("Error opening index");
        }

        if (reader == null)
            throw new NullPointerException("Reader is null");

        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("Body", analyzer);

        try {
            Query query = null;
            try {
                query = parser.parse(queryStr);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                System.out.println("Error parsing query string.");
            }

            TopDocs results = searcher.search(query, 10);
            ScoreDoc[] hits = results.scoreDocs;


            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            int rank = 1;

            for (ScoreDoc hit : hits) {

                Document doc = searcher.doc(hit.doc);

                writer.println(queryID + "\t" + "Q0" + "\t" + doc.getField("Id").numericValue().intValue()
                        + "\t" + rank++ + "\t" + hit.score + "\t" + "run-1");

//                String answer = doc.get("Body");
//                Integer Id = doc.getField("Id").numericValue().intValue();
//                System.out.println("DocId: " + Id);
//                System.out.println("DocAnswer: " + answer);
//                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main (String args[]) {

        Analyzer analyzer = new StandardAnalyzer();

        Indexer indexer = new Indexer();
        IndexWriter iw = indexer.openIndex(analyzer);

        if (iw == null)
            throw new NullPointerException("Error opening index path");

        indexer.indexDocuments(iw);

        try {
            iw.close();
        } catch (IOException e) {
            System.out.println("Error closing index");
        }

        indexer.parseQueries(analyzer);

        //indexer.searchIndex(analyzer);
    }



}
