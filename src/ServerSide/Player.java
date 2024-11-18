package ServerSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private Socket socket;
    private char playerNum;
    private Player opponent;
    private BufferedReader in;
    private PrintWriter out;

    public Player(Socket socket, char playerNum) {
        this.socket = socket;
        this.playerNum = playerNum;
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String message){
        out.println(message);
    }


}
