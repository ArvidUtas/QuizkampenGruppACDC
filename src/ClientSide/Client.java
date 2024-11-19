package ClientSide;

import java.io.*;
import java.net.Socket;

public class Client {

    private BufferedReader br;
    private PrintWriter pw;

    public Client() {

        String hostName = "localhost";
        int portNumber = 23456;

        try(Socket socket = new Socket(hostName, portNumber)) {
            System.out.println("Connected to " + hostName + "\nPort: " + portNumber);

            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String messsage;
            while ((messsage = br.readLine()) != null) {
                System.out.println(messsage);
                if (messsage.startsWith("Question")) {
                    System.out.print("Your answer: ");
                    String answer = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    pw.println(answer);
                }
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
