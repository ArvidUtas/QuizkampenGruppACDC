package ServerSide;

import java.util.List;

public class Question {
    private String text;
    private List<String> options;
    private String correctAnswer;

    public Question(String text, List<String> options, String correctAnswer) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }


    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }
}

class QuestionCategory {
    private List<Question> questions;

    public QuestionCategory(List<Question> questions) {
        this.questions = questions;
    }
}
