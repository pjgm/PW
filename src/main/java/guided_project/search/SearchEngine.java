package guided_project.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;

public class SearchEngine {

    public static void main(String args[]) {

        PageRank pr = new PageRank();
        Analyzer analyzer = new Analyzer();
        Indexer indexer = new Indexer();
        try {
            IndexWriter iw = indexer.openIndex(analyzer);
            indexer.indexDocuments(iw, pr.getParser().getAnswers());
            iw.close();
            indexer.parseQueries(analyzer, pr);
        } catch (IOException e) {
            System.err.println("Error reading file");
        } catch (ParseException e) {
            System.err.println("Error parsing query");
        }
    }
}
