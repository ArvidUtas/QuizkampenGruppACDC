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
            BufferedReader bufTemp = new BufferedReader(new InputStreamReader(System.in));

            String messsage;
            messsage = br.readLine();
            System.out.println(messsage);
            messsage = br.readLine();
            System.out.println(messsage);
            while (true) {
                for (int i = 0; i < 5; i++) {
                    messsage = br.readLine();
                    System.out.println(messsage);
                }

                System.out.print("Your answer: ");
                String answer = bufTemp.readLine();
                pw.println(answer);

                messsage = br.readLine();
                System.out.println(messsage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
