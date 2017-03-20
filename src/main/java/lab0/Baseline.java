package lab0;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Baseline {

    public static String indexPath = "index";
    public static String documentsPath = "Answers.csv";

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
            String line = br.readLine();

            while (line != null) {
                int i = line.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String args[]) {

        Analyzer analyzer = new StandardAnalyzer();

        Baseline baseline = new Baseline();
        IndexWriter iw = baseline.openIndex(analyzer);
        baseline.indexDocuments(iw);


    }


}
