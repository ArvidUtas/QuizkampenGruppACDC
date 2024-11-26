package AccessFromBothSides;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    public final static int CATEGORY = 0;
    public final static int QUESTION = 1;
    public final static int ANSWER_CHECK = 2;
    public final static int FINAL_SCORE = 3;
    public final static int MESSAGE = 4;
    public static final int ROUND_SCORE = 5;
    public static final int PLAY_AGAIN = 6;

    private int type;

    private int currentRound = 1;
    private int currentQ = 1;
    private int player1score = 0;
    private int player2score = 0;
    private int p1RoundScore = 0;
    private int p2RoundScore = 0;
    private boolean correctAnswer;
    private ArrayList<String> question;
    private String message;

    public Response(int type, int currentRound, int currentQ, int player1score, int player2score,
                    ArrayList<String> question, String optionalMessage) {
        this.type = type;
        this.currentRound = currentRound;
        this.currentQ = currentQ;
        this.player1score = player1score;
        this.player2score = player2score;
        this.question = question;
        this.message = optionalMessage;
    }
    public Response(int type, int currentRound, int player1score, int player2score,
                    int p1RoundScore, int p2RoundScore, String optionalMessage) {
        this.type = type;
        this.currentRound = currentRound;
        this.player1score = player1score;
        this.player2score = player2score;
        this.p1RoundScore = p1RoundScore;
        this.p2RoundScore = p2RoundScore;
        this.message = optionalMessage;
    }

    public Response(int type, int currentRound, int p1RoundScore, int p2RoundScore) {
        this.type = type;
        this.currentRound = currentRound;
        this.p1RoundScore = p1RoundScore;
        this.p2RoundScore = p2RoundScore;
    }

    public Response(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public Response(int type, boolean correctAnswer) {
        this.type = type;
        this.correctAnswer = correctAnswer;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getP1RoundScore() {
        return p1RoundScore;
    }

    public int getP2RoundScore() {
        return p2RoundScore;
    }

    public int getPlayer1score() {
        return player1score;
    }

    public int getPlayer2score() {
        return player2score;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    //En toString metod bara föra att kunna se i consolen att t ex poängen mm. uppdateras.
    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", currentRound=" + currentRound +
                ", currentQ=" + currentQ +
                ", player1score=" + player1score +
                ", player2score=" + player2score +
                ", question=" + question +
                ", message='" + message +
                ", roundScore=" + ROUND_SCORE + '\'' +
                '}';
    }

    public ArrayList<String> getQuestionData() {
        return question;
    }
}
