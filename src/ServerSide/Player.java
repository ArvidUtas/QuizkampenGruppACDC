package ServerSide;

import java.net.Socket;

public class Player {
    private Socket socket;
    private char playerNum;
    private Player opponent;

    public Player(Socket socket, char playerNum) {
        this.socket = socket;
        this.playerNum = playerNum;
    }
}
