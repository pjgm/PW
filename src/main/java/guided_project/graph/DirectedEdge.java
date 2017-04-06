package guided_project.graph;

import guided_project.model.Answer;
import guided_project.model.Question;

public class DirectedEdge {

    private Question question;
    private Answer answer;

    public DirectedEdge(Question question, Answer answer) {
        this.question = question;
        this.answer = answer;
    }

    public int getQuestionScore() {
        return question.getScore();
    }

    public int getAnswerScore() {
        return answer.getScore();
    }

    public int getFrom() {
        return question.getOwnerUserId();
    }

    public int getTo() {
        return answer.getOwnerUserId();
    }

}
