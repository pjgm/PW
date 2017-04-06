package lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Lab3NovaSocialGraph extends DatasetParser {

	public class User {
		public Integer Id;
		public double userRank;
		public List<User> inLinks;
		public List<User> outLinks;

		public User(Integer id) {
			Id = id;
			userRank = 0;
			inLinks = new ArrayList<>();
			outLinks = new ArrayList<>();
		}
	};

	public class Link {
		public User srcUser;
		public User dstUser;
		public QA question;
		public QA answer;
	};

	// <QuestionId, Question>
	protected Map<Integer, QA> questionsMap;

	// <QuestionId, <ScoreId, Answer >>
	protected Map<Integer, TreeMap<Integer, QA>> answersMap;

	public Lab3NovaSocialGraph() {
		socialGraph = new HashMap<Integer, User>();

		questionsMap = new HashMap<Integer, QA>();
		answersMap = new HashMap<Integer, TreeMap<Integer, QA>>();
	}

	@Override
	protected void handleAnswer(QA tmp) {

		// Store the answer in the pool of
		// question-answers
		TreeMap<Integer, QA> answers = answersMap.get(tmp.parentId);
		if (answers == null)
			answers = new TreeMap<Integer, QA>();
		answers.put(tmp.Id, tmp);
		answersMap.put(tmp.parentId, answers);

	}

	@Override
	protected void handleQuestion(QA tmp) {
		questionsMap.put(tmp.Id, tmp);
	}

	protected HashMap<Integer, User> socialGraph;
	protected double damping;

	public void loadSocialGraph(String answersPath, String questionsPath, double d) {

		damping = d;

		loadData(questionsPath, answersPath);

		for (Map.Entry<Integer, TreeMap<Integer, QA>> question : answersMap.entrySet()) {
			Integer QuestionId = question.getKey();
			TreeMap<Integer, QA> answers = question.getValue();
			QA q = questionsMap.get(QuestionId);

			for (Map.Entry<Integer, QA> ans : answers.entrySet()) {
				QA a = ans.getValue();
				addLinkToGraph(q, a);
			}
		}
	}

	public void addLinkToGraph(QA question, QA answer) {

		// TODO: complete the InLinks and the OutLinks

	}

	public void computePageRank(Integer iter) {

		// TODO: compute the PageRank of the social-graph

	}

	public void outUserRank() {
		for (Entry<Integer, User> usr : socialGraph.entrySet()) {
			System.out.println(usr.getKey() + "\t" + usr.getValue().userRank + "\t" + usr.getValue().inLinks.size()
					+ "\t" + usr.getValue().outLinks.size());
		}
	}

	public static void main(String[] args) {

		Lab3NovaSocialGraph temp = new Lab3NovaSocialGraph();

		String answersPath = "/home/jmag/classes/courses/2016-2017/WMS/labs/crossvalidatedquestions/Answers.csv";
		String questionsPath = "/home/jmag/classes/courses/2016-2017/WMS/labs/crossvalidatedquestions/Questions.csv";

		temp.loadSocialGraph(answersPath, questionsPath, 0.1);

		temp.computePageRank(10);

		temp.outUserRank();

		return;

	}

}
