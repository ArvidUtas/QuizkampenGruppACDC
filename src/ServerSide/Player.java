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
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String message){
        out.println(message);
    }
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    public Player getOpponent() {
        return opponent;
    }


    public String receieveFromClient(){
        String input = "";
        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }


}
