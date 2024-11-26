package ServerSide;

import AccessFromBothSides.Response;
import java.util.ArrayList;

public class Protocol {
    private final int CATEGORY = 0;
    private final int QUESTION = 1;
    private final int ANSWER = 2;
    private final int ROUND_SCORE = 3;
    private final int FINAL_SCORE = 4;
    private final int PLAY_AGAIN = 5;
    private final int EXIT = 6;

    private int numQuestion;
    private int numRounds;
    private int state = CATEGORY;
    private int currentRound = 1;
    private int currentQ = 1;
    private int p1Score = 0;
    private int p2Score = 0;
    private int p1RoundScore = 0;
    private int p2RoundScore = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private ArrayList<ArrayList<String>> questions;
    private Category category = new Category();

    public Protocol(int numQuestion, int numRounds, Player player1, Player player2) {
        this.numQuestion = numQuestion;
        this.numRounds = numRounds;
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;

        while (true) {
            if (state == CATEGORY) {
                currentPlayer.getOpponent().sendToClient(new Response(Response.MESSAGE,
                        "Please wait for Player " + currentPlayer.getPlayerNum() + " to choose category."));
                currentPlayer.sendToClient(new Response(Response.CATEGORY, currentRound, currentQ, p1Score, p2Score,
                        null, null));
                String chosenCategory = currentPlayer.receieveFromClient();
                if (chosenCategory.equals("DISCONNECT"))
                    break;
                questions = category.getQuestionsList(chosenCategory);
                if (questions.isEmpty()) {
                    throw new IllegalStateException("No questions found");
                }
                state = QUESTION;
            }
            else if (state == QUESTION) {
                Response qNAs = new Response(Response.QUESTION, currentRound, currentQ,
                        p1Score, p2Score, questions.get(currentQ - 1), null);
                sendToBothClients(qNAs);
                state = ANSWER;
            }
            else if (state == ANSWER) {
                /** Skapar trådar för spelarna.
                 * Nu kan t ex player2 skicka in sitt svarsalternativ och få svar på om det var rätt/fel samt få sin poäng
                 * uppdaterat. Innan gick det inte att göra eftersom programmet stod och väntade på player1.
                 */
                Thread player1Thread = new Thread(() -> {
                    try {
                        String player1Answer = player1.receieveFromClient();
                        if (player1Answer.equals("DISCONNECT"))
                            state = EXIT;
                        boolean corrAns;
                        synchronized (this) {
                            if (questions.get(currentQ - 1).get(1).equals(player1Answer)) {
                                p1RoundScore++; //
                                corrAns = true;
                            } else
                                corrAns = false;
                        }
                        Response answerCheck = new Response(Response.ANSWER_CHECK, corrAns);
                        player1.sendToClient(answerCheck);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Thread player2Thread = new Thread(() -> {
                    try {
                        String player2Answer = player2.receieveFromClient();
                        if (player2Answer.equals("DISCONNECT"))
                            state = EXIT;
                        boolean corrAns;
                        synchronized (this) {
                            if (questions.get(currentQ - 1).get(1).equals(player2Answer)) {
                                p2RoundScore++; //
                                corrAns = true;
                            } else
                                corrAns = false;
                        }
                        Response answerCheck = new Response(Response.ANSWER_CHECK, corrAns);
                        player2.sendToClient(answerCheck);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (state == EXIT)
                    break;
                player1Thread.start();
                player2Thread.start();
                try {
                    player1Thread.join();
                    player2Thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                currentQ++;
                if (currentQ > numQuestion && currentRound < numRounds) {
                    state = ROUND_SCORE;
                } else if (currentQ <= numQuestion) {
                    state = QUESTION;
                } else {
                    state = FINAL_SCORE;
                    p1Score += p1RoundScore;
                    p2Score += p2RoundScore;
                }
            }
            else if (state == ROUND_SCORE) {
                Response roundScoreUpdate = new Response(Response.ROUND_SCORE, currentRound,
                        p1RoundScore, p2RoundScore);
                sendToBothClients(roundScoreUpdate);

                String p1cont = player1.receieveFromClient();
                String p2cont = player2.receieveFromClient();

                p1Score += p1RoundScore;
                p2Score += p2RoundScore;

                p1RoundScore = 0;
                p2RoundScore = 0;

                currentRound++;
                if (currentRound <= numRounds) {
                    state = CATEGORY;
                    currentQ = 1;
                    currentPlayer = currentPlayer.getOpponent();
                }
            }
            else if (state == FINAL_SCORE) {
                if (p1Score > p2Score) {
                    player1.sendToClient(new Response(Response.FINAL_SCORE, currentRound,
                            p1Score, p2Score, p1RoundScore, p2RoundScore, "Victory!"));
                    player2.sendToClient(new Response(Response.FINAL_SCORE, currentRound,
                            p1Score, p2Score, p1RoundScore, p2RoundScore,  "Defeat."));
                } else if (p1Score < p2Score) {
                    player1.sendToClient(new Response(Response.FINAL_SCORE, currentRound,
                            p1Score, p2Score, p1RoundScore, p2RoundScore, "Defeat."));
                    player2.sendToClient(new Response(Response.FINAL_SCORE, currentRound,
                            p1Score, p2Score, p1RoundScore, p2RoundScore, "Victory!"));
                } else {
                    sendToBothClients(new Response(Response.FINAL_SCORE, currentRound,
                            p1Score, p2Score, p1RoundScore, p2RoundScore, "Draw."));
                }
                state = PLAY_AGAIN;
            }
            else if (state == PLAY_AGAIN) {
                Thread player1Thread = new Thread(() -> {
                    try {
                        String player1Answer = player1.receieveFromClient();
                        synchronized (this) {
                            if (player1Answer.equals("Again")){
                                Response playAgain = new Response(Response.PLAY_AGAIN, null);
                                player1.sendToClient(playAgain);
                            } else
                                state = EXIT;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Thread player2Thread = new Thread(() -> {
                    try {
                        String player2Answer = player2.receieveFromClient();
                        synchronized (this) {
                            if (player2Answer.equals("Again")) {
                                Response playAgain = new Response(Response.PLAY_AGAIN, null);
                                player2.sendToClient(playAgain);
                            } else
                                state = EXIT;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (state == EXIT)
                    break;

                player1Thread.start();
                player2Thread.start();

                try {
                    player1Thread.join();
                    player2Thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (state == EXIT) {
                break;
            }
        }
        player1.closeConnection();
        player2.closeConnection();
    }

    public void sendToBothClients(Response response) {
        player1.sendToClient(response);
        player2.sendToClient(response);
    }
}
