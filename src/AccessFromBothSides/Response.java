package AccessFromBothSides;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private final int CATEGORY = 1;
    private final int QUESTION = 2;
    private final int MESSAGE = 3;
    private int type;

    private int currentRound = 1;
    private int currentQ = 1;
    private int player1score = 0;
    private int player2score = 0;
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

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getCurrentQ() {
        return currentQ;
    }

    public void setCurrentQ(int currentQ) {
        this.currentQ = currentQ;
    }

    public int getPlayer1score() {
        return player1score;
    }

    public void setPlayer1score(int player1score) {
        this.player1score = player1score;
    }

    public int getPlayer2score() {
        return player2score;
    }

    public void setPlayer2score(int player2score) {
        this.player2score = player2score;
    }

    public ArrayList<String> getQuestion() {
        return question;
    }

    public void setQuestion(ArrayList<String> question) {
        this.question = question;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String optionalMessage) {
        this.message = optionalMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
