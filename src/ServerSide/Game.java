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
    private int numRounds;

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
        numRounds = Integer.parseInt(p.getProperty("numberOfRounds"));

    }

    public void run() {
        int currentQ = 0;
        player1.sendToClient("Welcome Player 1");
        player2.sendToClient("Welcome Player 2");

        ArrayList<ArrayList<String>> questions = category.getQuestionsList("11");

            if (questions.isEmpty()) {
                throw new IllegalStateException("No questions found");
            }

            for (int round = 1; round <= numRounds; round++) {
                player1.sendToClient("Round " + round);
                player2.sendToClient("Round " + round);

                for (int i = 0; i < numQuestion; i++) {
                    sendQnAs(questions, currentQ);

                    String player1Answer = player1.receieveFromClient();
                    String player2Answer = player2.receieveFromClient();

                    if (questions.get(currentQ).get(1).equals(player1Answer)) {
                        player1.sendToClient("Correct!");
                        p1Score++;
                    } else
                        player1.sendToClient("Wrong!");
                    if (questions.get(currentQ).get(1).equals(player2Answer)) {
                        player2.sendToClient("Correct!");
                        p2Score++;
                    } else
                        player2.sendToClient("Wrong!");
                    currentQ++;
            }

            player1.sendToClient("Final Score: " + p1Score);
            player2.sendToClient("Final Score: " + p2Score);

            if (p1Score > p2Score) {
                player1.sendToClient("Victory");
                player2.sendToClient("Defeat");
            } else if (p1Score < p2Score) {
                player1.sendToClient("Defeat");
                player2.sendToClient("Victory");
            } else {
                player1.sendToClient("Draw");
                player2.sendToClient("Draw");
            }
        }
    }

    public void sendToBothClients(String message) {
        player1.sendToClient(message);
        player2.sendToClient(message);
    }

//    public boolean checkAnswer(Player answer, ArrayList<ArrayList<String>> questions, int currentQ){
//        if(answer.equals(questions.get(currentQ).get(1))){
//            return true;
//        }
//        else return false;
//    }

    public void sendQnAs(ArrayList<ArrayList<String>> questions, int currentQ){
        sendToBothClients("Question: " + questions.get(currentQ).getFirst());
        sendToBothClients(questions.get(currentQ).get(2));
        sendToBothClients(questions.get(currentQ).get(3));
        sendToBothClients(questions.get(currentQ).get(4));
        sendToBothClients(questions.get(currentQ).get(5));
        }
    }


