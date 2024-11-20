package ClientSide;

import java.io.*;
import java.net.Socket;

public class Client {

    ;

    public Client() {

        String hostName = "localhost";
        int portNumber = 23456;

        try(Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            BufferedReader bufTemp = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to " + hostName + "\nPort: " + portNumber);

            Object messsage = in.readObject();
            if (messsage instanceof String)
                System.out.println(messsage);
            messsage = in.readLine();
            System.out.println(messsage);
            while (true) {
                for (int i = 0; i < 5; i++) {
                    messsage = in.readLine();
                    System.out.println(messsage);
                }

                System.out.print("Your answer: ");
                String answer = bufTemp.readLine();
                out.println(answer);

                messsage = in.readLine();
                System.out.println(messsage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
