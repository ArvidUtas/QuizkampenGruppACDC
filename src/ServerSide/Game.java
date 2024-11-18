package ServerSide;

import java.util.ArrayList;

public class Game extends Thread {
    Player player1;
    Player player2;
    Category category = new Category();
    private int numQuestion; //TODO: dessa ska styras av properties filen sen
    private int numRounds; //TODO: dessa ska styras av properties filen sen
    private int p1Score = 0;
    private int p2Score = 0;



    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);
    }

    public void sendToBothClients(String message) {
        player1.sendToClient(message);
        player2.sendToClient(message);
    }

    public void run() {
        player1.sendToClient("Välkommen Spelare 1");
        player2.sendToClient("Välkommen Spelare 2");
        ArrayList<ArrayList<String>> questions = category.getQuestionsList("11");
        sendToBothClients(questions.getFirst().getFirst());
        sendToBothClients(questions.getFirst().get(2));
        sendToBothClients(questions.getFirst().get(3));
        sendToBothClients(questions.getFirst().get(4));
        sendToBothClients(questions.getFirst().get(5));



        if (questions.isEmpty()) {
            throw new IllegalStateException("Inga frågor tillgängliga för spelet.");
        }



        for (int round = 1; round <= numRounds; round++) {
            player1.sendToClient("Runda " + round);
            player2.sendToClient("Runda " + round);
            Question currentQuestion = null;


            for (int q = 0; q < numRounds; q++) {
                sendToBothClients(currentQuestion.toString());

                String player1Answer = player1.receieveFromClient();
                String player2Answer = player2.receieveFromClient();

                if (currentQuestion.isCorrect(player1Answer)) p1Score++;
                if (currentQuestion.isCorrect(player2Answer)) p2Score++;
            }


            player1.sendToClient("Slutpoäng: " + p1Score);
            player2.sendToClient("Slutpoäng: " + p2Score);

            if (p1Score > p2Score) {
                player1.sendToClient("Du vann");
                player2.sendToClient("Du förlorade");
            } else if (p1Score < p2Score) {
                player1.sendToClient("Du förlorade");
                player2.sendToClient("Du vann");
            } else {
                player1.sendToClient("Oavgjort");
                player2.sendToClient("Oavgjort");
            }


        }
    }
}

