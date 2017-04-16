package guided_project.search;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
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

	@SuppressWarnings("resource")
	@Override
	protected TokenStreamComponents createComponents(String s) {
		final StandardTokenizer src = new StandardTokenizer();
		TokenStream tok = new StandardFilter(src);

		if (Properties.WHITESPACETOKENIZER)
			tok = new WhitespaceTokenizer();
		
		if (Properties.LOWERCASE)
			tok = new LowerCaseFilter(tok);

		if (Properties.STOPFILTER)
			tok = new StopFilter(tok, stopSet);

		if (Properties.PORTERSTEMFILTER)
			tok = new PorterStemFilter(tok);

		if (Properties.NGRAMTOKENFILTER)
			tok = new NGramTokenFilter(tok, 2,5);
		
		if (Properties.EDGENGRAMCOMMONFILTER)
			tok = new EdgeNGramTokenFilter(tok, 2,5);
		
		if (Properties.SHINGLEFILTER)
			tok = new ShingleFilter(tok, 2, 3);
		
		if (Properties.SNOWBALLFILTER)
			tok = new SnowballFilter(tok, "English");
			
		if(Properties.ENGLISHPOSSESSIVEFILTER)
			tok = new EnglishPossessiveFilter(tok);
		
		if(Properties.KSTEMFILTER)
			 tok = new KStemFilter(tok);

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
		analyser.close();

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
