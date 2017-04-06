//package lab2;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import lab0.Lab0NovaBaseline;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.core.KeywordAnalyzer;
//import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.search.similarities.ClassicSimilarity;
//import org.apache.lucene.search.similarities.Similarity;
//
//public class Lab2NovaPerField extends Lab0NovaBaseline {
//
//	@Override
//	void indexDoc(String rawDocument) {
//		// Implement your parser to extract the body and the first sentence
//	}
//
//	public static void main(String[] args) {
//
//		// ===================================
//		// Default analyzer and ranking function
//		Analyzer analyzer = new StandardAnalyzer();
//		Similarity similarity = new ClassicSimilarity();
//
//
//		// ===================================
//		// Select the ranking function
//
//		// Similarity similarity = new BM25Similarity();
//		// Similarity similarity = new LMDirichletSimilarity();
//		// Similarity similarity = new TFIDFSimilarity();
//
//		// ===================================
//		// Select or implement a new Analyzer
//
//		// Map<String, Analyzer> analyzerPerField = new HashMap<>();
//		// analyzerPerField.put("Body", new KeywordAnalyzer());
//		// analyzerPerField.put("FirstSentence", new KeywordAnalyzer());
//		// analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);
//
//		// ===================================
//		// The indexing process will use the provided analyzer and ranking function
//		Lab0NovaBaseline baseline = new Lab0NovaBaseline();
//		baseline.openIndex(analyzer, similarity);
//		baseline.indexDocuments();
//		baseline.close();
//
//		// ===================================
//		// The search process will use the provided analyzer and ranking function
//		baseline.indexSearch(analyzer, similarity);
//	}
//
//}
