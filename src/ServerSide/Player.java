package ServerSide;

import AccessFromBothSides.Response;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

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
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
                System.out.println("Sending response to client: " + response);
                out.writeObject(response);
                out.flush();
                System.out.println("Response sent successfully.");
            } else {
                System.err.println("Socket disconnected.1");
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }

    public char getPlayerNum() {
        return playerNum;
    }

    public String receieveFromClient(){
        String input = "";
        try {
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
                input = in.readLine();
                if (input != null && input.equals("DISCONNECT")) {
                    System.out.println("Client has disconnected.");
                    closeConnection();
                }
            } else {
                System.err.println("Socket disconnected.2");
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNullElse(input, "");
    }

    public void closeConnection() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket or in/out streams: " + e.getMessage());
        }
    }
}
