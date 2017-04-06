package guided_project.search;

import guided_project.graph.DirectedEdge;
import guided_project.graph.EdgeWeightedDigraph;
import guided_project.model.Answer;
import guided_project.model.Question;
import guided_project.model.User;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Parser {

    private static final String QUESTIONS_PATH = "Questions.csv";
    private static final String ANSWERS_PATH = "Answers.csv";
    private static final int QUESTION = 0;
    private static final int ANSWER = 1;

    private EdgeWeightedDigraph graph;
    private Set<Integer> users;
    private Map<Integer, Question> questions;
    private List<Answer> answers;

    Parser() {

        users = new HashSet<>();
        questions = new HashMap<>();
        answers = new ArrayList<>();

        try {
            parseFile(QUESTIONS_PATH, QUESTION);
            parseFile(ANSWERS_PATH, ANSWER);
            createGraph();
        } catch (IOException | ParseException e) {
            System.err.println("Error parsing file.");
        }
    }

    private void createGraph() {
        this.graph = new EdgeWeightedDigraph();
        for(Answer a : answers) {
            Question q = questions.get(a.getParentId());
            if(q == null) {
                continue;
            }
            graph.addVertex(q.getOwnerUserId());
            graph.addVertex(a.getOwnerUserId());
            DirectedEdge e = new DirectedEdge(q, a);
            graph.addEdge(e);
        }
    }

    public EdgeWeightedDigraph getGraph() {
        return graph;
    }

    private void parseFile(String filePath, int type) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        br.readLine();
        String line;

        while ((line = br.readLine()) != null) {

            if (line.length() == 0)
                continue;

            sb.append(line);

            if (line.equals("\"")) {
                parseFields(sb.toString(), type);
                sb.setLength(0);
            }
        }
    }
    /** Each question is organized as
     *  Id,OwnerUserId,CreationDate,Score,Title,Body
     *  ===============================================
     *  Each answer is organized as:
     *  Id,OwnerUserId,CreationDate,ParentId,Score,Body
    */
    private void parseFields(String documentSrt, int type) throws ParseException {

        String[] parts = documentSrt.split(",", 6);

        int id = Integer.parseInt(parts[0]);

        int ownerUserId;
        try {
            ownerUserId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing OwnerUserID of document " + id + ". " + "Skipping...");
            return;
        }

        Date creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(parts[2]);

        String body = new HtmlToPlainText().getPlainText(Jsoup.parse(parts[5]));

        if (type == QUESTION) {
            int score = Integer.parseInt(parts[3]);
            String title = parts[4];
            questions.put(id, new Question(id, ownerUserId, creationDate, score, title, body));
        }
        else if (type == ANSWER) {
            int parentId = Integer.parseInt(parts[3]);
            int score = Integer.parseInt(parts[4]);
            answers.add(new Answer(id, ownerUserId, creationDate, parentId, score, body));
        }
        else
            System.err.println("type not defined");
    }

}
