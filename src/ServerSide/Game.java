package ServerSide;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends Thread {
    Player player1;
    Player player2;
    Category category = new Category();
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
        numQuestion = Integer.parseInt(p.getProperty("numberOfQuestions"));
        numRonds = Integer.parseInt(p.getProperty("numberOfRounds"));

    }

    public void run() {
        int currentQ = 0;
        player1.sendToClient("Välkommen Spelare 1");
        player2.sendToClient("Välkommen Spelare 2");

        ArrayList<ArrayList<String>> questions = category.getQuestionsList("11");
        for (int i = 0; i < numQuestion; i++) {
            sendQnAs(questions, currentQ);
            currentQ++;
        }
    }

    public void sendQnAs(ArrayList<ArrayList<String>> questions, int currentQ){
        sendToBothClients("Question" + questions.get(currentQ).getFirst());
        sendToBothClients(questions.get(currentQ).get(2));
        sendToBothClients(questions.get(currentQ).get(3));
        sendToBothClients(questions.get(currentQ).get(4));
        sendToBothClients(questions.get(currentQ).get(5));
    }

    public void sendToBothClients (String message){
        player1.sendToClient(message);
        player2.sendToClient(message);
    }
}
