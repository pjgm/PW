package guided_project.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;

public class SearchEngine {
	
	public static final boolean DEBUGMODE = true;
	public static final boolean KAGGLEMODE = true;
	public static final String OFFLINEQUERIES = "src/main/java/guided_project/data/queries.offline.txt";
	public static final String OFFLINERESULTS = "src/main/java/guided_project/results/offline.txt";
	public static final String KAGGLEQUERIES = "src/main/java/guided_project/data/queries.kaggle.txt";
	public static final String KAGGLERESULTS = "src/main/java/guided_project/results/kaggle.csv";
	public static final String INDEXPATH = "index";
	public static final String CHARSET = "UTF-8";
	public static final String OFFLINEHEADER = "QueryID\tX\tDocID\tRank\tScore\t\tRunID\n";
	public static final String KAGGLEHEADER= "\"ID\",\"AnswerID\"\n";

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
