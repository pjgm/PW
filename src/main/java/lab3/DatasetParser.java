package lab3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

public abstract class DatasetParser {

	public class QA {
		public Integer Id;
		public Integer ownerUserId;
		public long creationDate;
		public Integer parentId;
		public Integer score;
		public String body;
		public String title;
	}

	protected abstract void handleAnswer(QA tmp);

	protected abstract void handleQuestion(QA tmp);

	private QA parseAnswers(String rawDocument) {

		QA doc = new QA();
		String aux = "";
		// ====================================================
		// Each document is organized as:
		// Id,OwnerUserId,CreationDate,ParentId,Score,Body
		Integer Id = 0;
		try {

			// Extract field Id
			Integer start = 0;
			Integer end = rawDocument.indexOf(',');
			aux = rawDocument.substring(start, end);
			Id = Integer.decode(aux);
			doc.Id = Id;

			// Extract field OwnerUserId
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			if (aux.equals("NA")) {
				System.out.println("Unknown OwnerID for answer " + Id);
				return null;
			} else {
				Integer OwnerUserId = Integer.decode(aux);
				doc.ownerUserId = OwnerUserId;
			}

			// Extract field CreationDate
			try {
				start = end + 1;
				end = rawDocument.indexOf(',', start);
				aux = rawDocument.substring(start, end);
				Date creationDate;
				creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(aux);
				doc.creationDate = creationDate.getTime();
			} catch (ParseException e1) {
				System.out.println("Error parsing date for document " + Id);
			}

			// Extract field ParentId
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer ParentId = Integer.decode(aux);
			doc.parentId = ParentId;

			// Extract field Score
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer Score = Integer.decode(aux);
			doc.score = Score;

			// Extract field Body
			String body = rawDocument.substring(end + 1);
			doc.body = body;

		} catch (Exception e) {
			System.out.println("Error parsing answer " + Id);
			e.printStackTrace();
			return null;
		}
		return doc;
	}

	private QA parseQuestions(String rawDocument) {

		QA doc = new QA();
		// ====================================================
		// Each document is organized as:
		// Id,OwnerUserId,CreationDate,Score,Title,Body
		Integer Id = 0;
		try {

			// Extract field Id
			Integer start = 0;
			Integer end = rawDocument.indexOf(',');
			String aux = rawDocument.substring(start, end);
			Id = Integer.decode(aux);
			doc.Id = Id;

			// Extract field OwnerUserId
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			if (aux.equals("NA")) {
				System.out.println("Unknown OwnerID for question " + Id);
				doc.ownerUserId = -1;
				return null;
			} else {
				Integer OwnerUserId = Integer.decode(aux);
				doc.ownerUserId = OwnerUserId;
			}

			// Extract field CreationDate
			try {
				start = end + 1;
				end = rawDocument.indexOf(',', start);
				aux = rawDocument.substring(start, end);
				Date creationDate;
				creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(aux);
				doc.creationDate = creationDate.getTime();
			} catch (ParseException e1) {
				System.out.println("Error parsing date for document " + Id);
			}

			// Extract field Score
			start = end + 1;
			end = rawDocument.indexOf(',', start);
			aux = rawDocument.substring(start, end);
			Integer Score = Integer.decode(aux);
			doc.score = Score;

			// Extract field Title
			start = end + 1;
			end = rawDocument.indexOf('"', start);
			String title;
			if (end == start + 1) {
				start++;
				end = rawDocument.indexOf('"', start) - 1;
				title = rawDocument.substring(start, end);
				end++;
			} else {
				end = rawDocument.indexOf(',', start);
				title = rawDocument.substring(start, end);
			}
			doc.title = title;

			// Extract field Body
			String body = rawDocument.substring(end + 1);
			doc.body = body;

		} catch (Exception e) {
			System.out.println("Error parsing question " + Id);
			e.printStackTrace();
			return null;
		}
		return doc;
	}

	public void loadData(String questionsPath, String answersPath) {

		// ====================================================
		// Parse the Questions data
		if (questionsPath != null)
			try (BufferedReader br = new BufferedReader(new FileReader(questionsPath))) {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine(); // The first line is dummy
				line = br.readLine();

				// ====================================================
				// Read documents
				while (line != null) {
					int i = line.length();

					// Search for the end of document delimiter
					if (i != 0)
						sb.append(line);
					sb.append(System.lineSeparator());
					if (((i >= 2) && (line.charAt(i - 1) == '"') && (line.charAt(i - 2) != '"'))
							|| ((i == 1) && (line.charAt(i - 1) == '"'))) {
						// Index the document
						QA tmp = parseQuestions(sb.toString());
						if (tmp != null) {
							handleQuestion(tmp);
						}

						// Start a new document
						sb = new StringBuilder();
					}
					line = br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// ====================================================
		// Parse the Answers data
		try (BufferedReader br = new BufferedReader(new FileReader(answersPath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine(); // The first line is dummy
			line = br.readLine();

			// ====================================================
			// Read documents
			Integer num_lines = 0;
			while (line != null) {
				int i = line.length();

				// Search for the end of document delimiter
				if (i != 0) {
					sb.append(line);
					num_lines++;
				}
				sb.append(System.lineSeparator());
				if (num_lines > 1)
					if (((i >= 2) && (line.charAt(i - 1) == '"') && (line.charAt(i - 2) != '"'))
							|| ((i == 1) && (line.charAt(i - 1) == '"'))) {
						// Index the document
						QA tmp = parseAnswers(sb.toString());

						if (tmp != null) {
							handleAnswer(tmp);
						}

						// Start a new document
						sb = new StringBuilder();
						num_lines = 0;
					}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
