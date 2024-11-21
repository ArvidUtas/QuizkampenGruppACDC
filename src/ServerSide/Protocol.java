package ServerSide;

import AccessFromBothSides.Response;
import java.util.ArrayList;

public class Protocol {
    private final int CATEGORY = 0;
    private final int QUESTION = 1;
    private final int ANSWER = 2;
    private final int FINAL_SCORE = 3;
    private final int PLAY_AGAIN = 4;

    private int numQuestion;
    private int numRounds;
    private int state = CATEGORY;
    private int currentRound = 1;
    private int currentQ = 1;
    private int p1Score = 0;
    private int p2Score = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    Response response;
    ArrayList<ArrayList<String>> questions;
    private Category category = new Category();

    public Protocol(int numQuestion, int numRounds, Player player1, Player player2) {
        this.numQuestion = numQuestion;
        this.numRounds = numRounds;
        this.player1 = player1;
        this.player2 = player2;

        while (true) {
            if (state == CATEGORY) {
                currentPlayer = player1;
                Response catResponse = new Response(1, currentRound, currentQ, p1Score, p2Score,
                        null, "Choose your category");
                currentPlayer.sendToClient(catResponse);
                String chosenCategory = currentPlayer.receieveFromClient();
                questions = category.getQuestionsList(chosenCategory);
                if (questions.isEmpty()) {
                    throw new IllegalStateException("No questions found");
                }
                state = QUESTION;
            } else if (state == QUESTION) {
                Response qNAs = new Response(2, currentRound, currentQ,
                        p1Score, p2Score, questions.get(currentQ - 1), null);
                sendToBothClients(qNAs);
                state = ANSWER;
            } else if (state == ANSWER) {
                String player1Answer = player1.receieveFromClient();
                String player2Answer = player2.receieveFromClient();
                if (questions.get(currentQ).get(1).equals(player1Answer)) {
                    player1.sendStringToClient("Correct!");
                    p1Score++;
                } else
                    player1.sendStringToClient("Wrong!");
                if (questions.get(currentQ).get(1).equals(player2Answer)) {
                    player2.sendStringToClient("Correct!");
                    p2Score++;
                } else
                    player2.sendStringToClient("Wrong!");

                currentQ++;
                if (currentQ > numQuestion)
                    currentRound++;

                if (currentQ <= numQuestion)
                    state = QUESTION;
                else if (currentQ > numQuestion && currentRound <= numRounds) {
                    state = CATEGORY;
                    currentQ = 1;
                    if (currentPlayer == player1)
                        currentPlayer = player2;
                } else
                    state = FINAL_SCORE;
            } else if (state == FINAL_SCORE) {
                if (p1Score > p2Score) {
                    player1.sendStringToClient("Victory");
                    player2.sendStringToClient("Defeat");
                } else if (p1Score < p2Score) {
                    player1.sendStringToClient("Defeat");
                    player2.sendStringToClient("Victory");
                } else {
                    player1.sendStringToClient("Draw");
                    player2.sendStringToClient("Draw");
                }
                break;
            }
        }
    }

    public void sendToBothClients(Response response) {
        player1.sendToClient(response);
        player2.sendToClient(response);
    }
}
