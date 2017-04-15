package guided_project.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.Answer;
import guided_project.model.User;

class Indexer {

	private static double alfa = 0.5;

	IndexWriter openIndex(Analyzer analyzer) throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index
															// instead of
															// appending to
															// existing index
		Directory dir = FSDirectory.open(Paths.get(SearchEngine.INDEXPATH));
		return new IndexWriter(dir, iwc);
	}

	void indexDocuments(IndexWriter iw, List<Answer> answers) throws IOException {

		for (Answer a : answers) {
			Document doc = new Document();

			doc.add(new StoredField("Id", a.getId()));

			doc.add(new IntPoint("OwnerUserId", a.getOwnerUserId()));
			doc.add(new StoredField("OwnerUserId", a.getOwnerUserId()));

			doc.add(new LongPoint("CreationDate", a.getCreationDate().getTime()));
			doc.add(new StoredField("CreationDate", a.getCreationDate().getTime()));

			doc.add(new IntPoint("ParentId", a.getParentId()));
			doc.add(new StoredField("ParentId", a.getParentId()));

			doc.add(new IntPoint("Score", a.getScore()));
			doc.add(new StoredField("Score", a.getScore()));

			doc.add(new TextField("Body", a.getBody(), Field.Store.YES));

			iw.addDocument(doc);
		}
	}

	void parseQueries(Analyzer analyzer, PageRank pr) throws IOException, ParseException {
		PrintWriter pw;
		BufferedReader br;

		if (SearchEngine.KAGGLEMODE) {
			br = new BufferedReader(new FileReader(SearchEngine.KAGGLEQUERIES));
			pw = new PrintWriter(SearchEngine.KAGGLERESULTS, SearchEngine.CHARSET);
			pw.write(SearchEngine.KAGGLEHEADER);
		} else {
			br = new BufferedReader(new FileReader(SearchEngine.OFFLINEQUERIES));
			pw = new PrintWriter(SearchEngine.OFFLINERESULTS, SearchEngine.CHARSET);
			pw.write(SearchEngine.OFFLINEHEADER);
		}

		String line;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(":", 2);
			searchQuery(pw, analyzer, pr, parts[0], parts[1]);
		}

		br.close();
		pw.flush();
		pw.close();
	}

	private void searchQuery(PrintWriter writer, Analyzer analyzer, PageRank pr, String queryID, String queryStr)
			throws IOException, ParseException {

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(SearchEngine.INDEXPATH)));

		IndexSearcher searcher = new IndexSearcher(reader);
		// searcher.setSimilarity(new ClassicSimilarity());
		QueryParser parser = new QueryParser("Body", analyzer);

		Query query = parser.parse(QueryParser.escape(queryStr));

		TopDocs results = searcher.search(query, 100);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;

		System.out.println("Query number " + queryID);
		System.out.println("Query body " + queryStr);
		System.out.println(numTotalHits + " total matching documents");

		if (SearchEngine.DEBUGMODE)
			System.out.println("DEBUG: Max score from max score: " + results.getMaxScore());
		if (SearchEngine.DEBUGMODE)
			System.out.println("DEBUG: Min score from score doc array: " + hits[99].score);

		computeCombinedScore(hits, pr, searcher, results.getMaxScore(), hits[99].score, pr.getMaxValue(), pr.getMinValue());

		if (SearchEngine.KAGGLEMODE)
			writer.write("\"" + queryID + "\",\"");

		int rank = 1;
		for (ScoreDoc hit : hits) {
			Document doc = searcher.doc(hit.doc);
			String runId = analyzer.getClass().getSimpleName() + "-" + new Date().toString();
			if (SearchEngine.KAGGLEMODE) {
				writer.write(doc.getField("Id").numericValue().intValue() + " ");
				rank++;
			} else {
				writer.write(queryID + "\t" + "Q0" + "\t" + doc.getField("Id").numericValue().intValue() + "\t" + rank++
						+ "\t" + hit.score + "\t" + runId + "\n");
			}

			if (rank > 10)
				break;
		}

		if (SearchEngine.KAGGLEMODE)
			writer.write("\"\n");
	}

	private void computeCombinedScore(ScoreDoc[] hits, PageRank pr, IndexSearcher searcher, float docMaxScore,
			float docMinScore, double prMaxScore, double prMinScore) throws IOException {
		EdgeWeightedDigraph graph = pr.getGraph();
		for (int i = 0; i < hits.length; i++) {
			// Normalization
			hits[i].score = (hits[i].score - docMinScore) / (docMaxScore - docMinScore);
			if (SearchEngine.DEBUGMODE)
				System.out.println("DEBUG: Normalized Doc score: " + hits[i].score);
			int userId = (int) searcher.doc(hits[i].doc).getField("OwnerUserId").numericValue();
			User user = graph.getVertex(userId);
			double userRank = 0;
			if (user != null) {
				if (SearchEngine.DEBUGMODE)
					System.out.println("DEBUG: Normalized user rank: " + user.getRank());
				userRank = user.getRank();
			}
			hits[i].score = (float) ((alfa * hits[i].score) + ((1 - alfa) * userRank));
			if (SearchEngine.DEBUGMODE)
				System.out.println("DEBUG: Final Score: " + hits[i].score);
			if (SearchEngine.DEBUGMODE)
				System.out.println("--/--");
			// Calculate normalized final score
		}

		// Sort array at the end
		Arrays.sort(hits, new Comparator<ScoreDoc>() {
			@Override
			public int compare(ScoreDoc o1, ScoreDoc o2) {
				return Float.compare(o2.score, o1.score);
			}
		});
	}
}
