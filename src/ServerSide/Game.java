package ServerSide;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Game extends Thread {
    private Player player1;
    private Player player2;
    private int numQuestion;
    private int numRounds;
    private Properties p = new Properties();

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
        Protocol protocol = new Protocol(numQuestion, numRounds, player1, player2);
    }
}


