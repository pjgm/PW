package independent_project;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class Analyzer extends StopwordAnalyzerBase {

    private static List<String> stopWords = Arrays.asList("you", "us", "i", "can", "have", "which", "on");
    private static CharArraySet stopSet = new CharArraySet(stopWords, false);

    private int maxTokenLength = 25;

    public Analyzer() {
        super(stopSet);
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        final StandardTokenizer src = new StandardTokenizer();
        TokenStream tok = new StandardFilter(src);

        tok = new LowerCaseFilter(tok);
        tok = new StopFilter(tok, stopSet);
        tok = new PorterStemFilter(tok);

        return new TokenStreamComponents(src, tok) {
            protected void setReader(Reader reader) {
                src.setMaxTokenLength(maxTokenLength);
                super.setReader(reader);
            }
        };
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        TokenStream result = new StandardFilter(in);
        return new LowerCaseFilter(result);
    }

    public static void main(String args[]) {
        String text = "<p>This is a demonstration, of the TokenStream Lucene-API,</p>";
        Analyzer analyser = new Analyzer();
        TokenStream ts = analyser.tokenStream("field", new StringReader(text));
        CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);

        try {
            ts.reset();
            while (ts.incrementToken()) {
                System.out.println(cta.toString());
            }
            ts.end();
            ts.close();
        } catch (IOException e) {
            System.out.println("Error reading from token stream");
        }
    }

}
