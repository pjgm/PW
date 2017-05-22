package guided_project.search;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class SearchEngine {

    public static void main(String args[]) {

        Parser parser = new Parser();
        Analyzer analyzer = new Analyzer();
        Indexer indexer = new Indexer();
        try {
            IndexWriter iw = indexer.openIndex(analyzer);
            //indexer.indexDocuments(iw, parser.getAnswers());
            iw.close();
            indexer.parseQueries(analyzer);
            PageRank pr = new PageRank();
        } catch (IOException e) {
            System.err.println("Error reading file");
        } catch (ParseException e) {
            System.err.println("Error parsing query");
        }
    }
}
