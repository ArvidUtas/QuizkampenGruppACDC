package ServerSide;

import AccessFromBothSides.Response;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        currentPlayer = player1;

        while (true) {
            if (state == CATEGORY) {
                Response catResponse = new Response(Response.CATEGORY, currentRound, currentQ, p1Score, p2Score,
                        null, "Choose your category");
                currentPlayer.sendToClient(catResponse);
                String chosenCategory = currentPlayer.receieveFromClient();
                questions = category.getQuestionsList(chosenCategory);
                if (questions.isEmpty()) {
                    throw new IllegalStateException("No questions found");
                }
                state = QUESTION;
            } else if (state == QUESTION) {
                Response qNAs = new Response(Response.QUESTION, currentRound, currentQ,
                        p1Score, p2Score, questions.get(currentQ - 1), null);
                sendToBothClients(qNAs);
                state = ANSWER;
            } else if (state == ANSWER) {
                ExecutorService executor = Executors.newFixedThreadPool(2); // Create a thread pool for two players

// Thread for Player 1
                executor.submit(() -> {
                    try {
                        String player1Answer = player1.receieveFromClient();
                        String corrOrWro;

                            if (questions.get(currentQ - 1).get(1).equals(player1Answer)) {
                                p1Score++;
                                corrOrWro = "Correct!";
                            } else {
                                corrOrWro = "Wrong!";
                            }
                            Response answerCheck = new Response(Response.ANSWER_CHECK, currentRound, currentQ,
                                    p1Score, p2Score, questions.get(currentQ - 1), corrOrWro);
                            player1.sendToClient(answerCheck);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

// Thread for Player 2
                executor.submit(() -> {
                    try {
                        String player2Answer = player2.receieveFromClient();
                        String corrOrWro;

                            if (questions.get(currentQ - 1).get(1).equals(player2Answer)) {
                                p2Score++;
                                corrOrWro = "Correct!";
                            } else {
                                corrOrWro = "Wrong!";
                            }
                            Response answerCheck = new Response(Response.ANSWER_CHECK, currentRound, currentQ,
                                    p1Score, p2Score, questions.get(currentQ - 1), corrOrWro);
                            player2.sendToClient(answerCheck);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                executor.shutdown();


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
                    else if (currentPlayer == player2)
                        currentPlayer = player1;

                } else
                    state = FINAL_SCORE;
            } else if (state == FINAL_SCORE) {
                if (p1Score > p2Score) {
                    player1.sendToClient(new Response(Response.FINAL_SCORE, currentRound, currentQ,
                            p1Score, p2Score, questions.get(currentQ - 1), "Victory!"));
                    player2.sendToClient(new Response(Response.FINAL_SCORE, currentRound, currentQ,
                            p1Score, p2Score, questions.get(currentQ - 1), "Defeat"));
                } else if (p1Score < p2Score) {
                    player1.sendToClient(new Response(Response.FINAL_SCORE, currentRound, currentQ,
                            p1Score, p2Score, questions.get(currentQ - 1), "Defeat"));
                    player2.sendToClient(new Response(Response.FINAL_SCORE, currentRound, currentQ,
                            p1Score, p2Score, questions.get(currentQ - 1), "Victory!"));
                } else {
                    sendToBothClients(new Response(Response.FINAL_SCORE, currentRound, currentQ,
                            p1Score, p2Score, questions.get(currentQ - 1), "Draw"));
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
