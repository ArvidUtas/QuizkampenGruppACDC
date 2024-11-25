package ServerSide;

import AccessFromBothSides.Response;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {
    public ServerListener() {
        try (ServerSocket listener = new ServerSocket(23456)){
            System.out.println("Servern är igång!");
            while (true) {
                Player player1 = new Player(listener.accept(), '1');
                player1.sendToClient(new Response(Response.MESSAGE, 0, 0,
                        0, 0,null, "Welcome Player 1. Please wait for Player 2 to connect."));
                Player player2 = new Player(listener.accept(), '2');
                Game game = new Game(player1, player2);
                game.start();
            }
        } catch (IOException e) {
            System.err.println("Problem med in-ut strömmen.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerListener sl = new ServerListener();
    }
}