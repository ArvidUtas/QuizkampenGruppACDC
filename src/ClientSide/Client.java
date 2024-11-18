package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private BufferedReader br;
    private PrintWriter pw;

    private BufferedReader br2;

    public Client() {

        String hostName = " "; //ändra till en IP-adress
        int portNumber = 23456;

        // Använda try-with resources?
        try(Socket socket = new Socket(hostName, portNumber)) {
            //ska vi läsa från och skriva till enbart som strängar?
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //utifall vi tillfälligt vill testa kommunikationen genom consolen
            br2 = new BufferedReader(new InputStreamReader(System.in));

            String fromServer;
            String fromUser;

            //test för att se att kommunikationen funkar
            while((fromServer = br.readLine()) != null) {
                    fromUser = br2.readLine();

                    if (fromUser != null){
                        pw.println(fromUser);
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
