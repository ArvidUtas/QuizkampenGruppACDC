package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader br;
    private PrintWriter pw;

    public Client() {

        String hostName = "127.0.0.1";
        int portNumber = 23456;

        try(Socket socket = new Socket(hostName, portNumber)) {
            System.out.println("Connected to " + hostName + "\nPort: " + portNumber);

            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String temp = br.readLine();
                System.out.println(temp);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
