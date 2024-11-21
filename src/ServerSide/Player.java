package ServerSide;

import AccessFromBothSides.Response;

import java.io.*;
import java.net.Socket;

public class Player {
    private Socket socket;
    private char playerNum;
    private Player opponent;
    private BufferedReader in;
    private ObjectOutputStream out;

    public Player(Socket socket, char playerNum) {
        this.socket = socket;
        this.playerNum = playerNum;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendToClient(Response response){
        try {
            System.out.println("Sending response to client: " + response);
            out.writeObject(response);
            out.flush();
            System.out.println("Response sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendStringToClient(String message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
