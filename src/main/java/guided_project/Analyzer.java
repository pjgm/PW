package guided_project;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class Analyzer extends StopwordAnalyzerBase {

    private static List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if",
            "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there",
            "these", "they", "this", "to", "was", "will", "with");

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
        // tok = new StopFilter(tok, stopSet);
        // tok = new EnglishPossessiveFilter(tok); // 5.59%
        // tok = new KStemFilter(tok); // 5.65%
        tok = new PorterStemFilter(tok); // 5.66%
        // tok = new SnowballFilter(tok, "English");

        return new TokenStreamComponents(src, tok) {
            protected void setReader(Reader reader) {
                src.setMaxTokenLength(maxTokenLength);
                super.setReader(reader);
            }
        };
    }

//    @Override
//    protected Reader initReader(String fieldName, Reader reader) {
//        return new HTMLStripCharFilter(reader);
//    }

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
