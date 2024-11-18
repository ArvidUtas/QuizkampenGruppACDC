package ServerSide;

import java.util.ArrayList;

public class Game extends Thread {
    Player player1;
    Player player2;
    Category category = new Category();
    private int numQuestion; //TODO: dessa ska styras av properties filen sen
    private int numRonds; //TODO: dessa ska styras av properties filen sen

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        player1.sendToClient("Välkommen Spelare 1");
        player2.sendToClient("Välkommen Spelare 2");
        ArrayList<String> questions = category.getQuestionsList("11");
        sendToBothClients(questions.getFirst());

        while (true){
            System.out.println(player1.receieveFromClient() + "Player one");
            System.out.println(player2.receieveFromClient() + "Player two");

        }
    }

    public void sendToBothClients (String message){
        player1.sendToClient(message);
        player2.sendToClient(message);
    }
}
