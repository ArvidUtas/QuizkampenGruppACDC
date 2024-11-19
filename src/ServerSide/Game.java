package ServerSide;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends Thread {
    Player player1;
    Player player2;
    Category category = new Category();
    private int p1Score = 0;
    private int p2Score = 0;
    private int numQuestion;
    private int numRonds;

    Properties p = new Properties();
    public Game(Player player1, Player player2) {
        try {
            p.load(new FileInputStream("src/ServerSide/Quizkampen.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);
        numQuestion = Integer.parseInt(p.getProperty("numberOfQuestions"));
        numRonds = Integer.parseInt(p.getProperty("numberOfRounds"));

    }

    public void sendToBothClients(String message) {
        player1.sendToClient(message);
        player2.sendToClient(message);
    }

    public void run() {
        int currentQ = 0;
        player1.sendToClient("Welcome Player 1");
        player2.sendToClient("Welcome Player 2");

        ArrayList<ArrayList<String>> questions = category.getQuestionsList("11");
        for (int i = 0; i < numQuestion; i++) {
            sendQnAs(questions, currentQ);
            currentQ++;

            if (questions.isEmpty()) {
                throw new IllegalStateException("No questions found");
            }

            for (int round = 1; round <= numRounds; round++) {
                player1.sendToClient("Round " + round);
                player2.sendToClient("Round " + round);
                Question currentQuestion = null;


                for (int q = 0; q < numRounds; q++) {
                    sendToBothClients(currentQuestion.toString());

                    String player1Answer = player1.receieveFromClient();
                    String player2Answer = player2.receieveFromClient();

                    if (currentQuestion.isCorrect(player1Answer)) p1Score++;
                    if (currentQuestion.isCorrect(player2Answer)) p2Score++;

                }
            }

            player1.sendToClient("Final Score: " + p1Score);
            player2.sendToClient("Final Score: " + p2Score);

            if (p1Score > p2Score) {
                player1.sendToClient("Victory");
                player2.sendToClient("Defeat");
            } else if (p1Score < p2Score) {
                player1.sendToClient("Victory");
                player2.sendToClient("Defeat");
            } else {
                player1.sendToClient("Draw");
                player2.sendToClient("Draw");
            }
        }
    }

    public boolean checkAnswer(Player answer, ArrayList<ArrayList<String>> questions, int currentQ){
        if(answer.equals(questions.get(currentQ).get(1))){
            return true;
        }
        else return false;
    }

    public void sendQnAs(ArrayList<ArrayList<String>> questions, int currentQ){
        sendToBothClients("Question" + questions.get(currentQ).getFirst());
        sendToBothClients(questions.get(currentQ).get(2));
        sendToBothClients(questions.get(currentQ).get(3));
        sendToBothClients(questions.get(currentQ).get(4));
        sendToBothClients(questions.get(currentQ).get(5));
        }
    }


