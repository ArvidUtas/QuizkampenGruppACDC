package ClientSide;

import AccessFromBothSides.Response;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Protocol {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private PrintWriter stringOut;

    public Protocol(String serverAddress, int port) {
        try {
            // Anslut till servern
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            stringOut = new PrintWriter(socket.getOutputStream(), true);
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunde inte ansluta till servern!");
        }
    }

    // Skicka data till servern
    public void sendToServer(Response response) {
        try {
            out.writeObject(response);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendStringToServer(String message) {
        try {
            stringOut.println(message);
            stringOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hämta data från servern
    public Response receiveFromServer() {
        try {
            return (Response) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Stäng anslutningen
    public void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
