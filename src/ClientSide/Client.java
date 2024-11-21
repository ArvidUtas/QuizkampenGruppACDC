package ClientSide;

import AccessFromBothSides.EnumCategories;
import AccessFromBothSides.Response;

import java.io.*;
import java.net.Socket;

public class Client {
    public Client() {

        String hostName = "localhost";
        int portNumber = 23456;

        try(Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            BufferedReader bufTemp = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to " + hostName + "\nPort: " + portNumber);
            System.out.println(in.readObject());
            Object obj;

            while (true) {
                obj = in.readObject();
                if (obj instanceof Response response) {
                    if (response.getType() == Response.CATEGORY) {
                        System.out.println(response.getMessage());
                        String chosenCat = bufTemp.readLine();
                        String catToSend = "";
//                        if (chosenCat.equalsIgnoreCase(EnumCategories.FILM.getText())) catToSend = EnumCategories.FILM.getValue();
//                        else if (chosenCat.equalsIgnoreCase(EnumCategories.SPORTS.getText())) catToSend = EnumCategories.SPORTS.getValue();
//                        else if (chosenCat.equalsIgnoreCase(EnumCategories.GEOGRAPHY.getText())) catToSend = EnumCategories.GEOGRAPHY.getValue();
//                        else if (chosenCat.equalsIgnoreCase(EnumCategories.POLITICS.getText())) catToSend = EnumCategories.POLITICS.getValue();
//                        else if (chosenCat.equalsIgnoreCase(EnumCategories.VEHICLES.getText())) catToSend = EnumCategories.VEHICLES.getValue();
                        for (EnumCategories category : EnumCategories.values()) {
                            if (chosenCat.equalsIgnoreCase(category.getText())) {
                                catToSend = category.getValue();
                                break;
                            }
                        }
                        out.println(catToSend);

                    } else if (response.getType() == Response.QUESTION) {
                        System.out.println("Question: " + response.getQuestion().getFirst() + "\n" +
                                response.getQuestion().get(2) + "\n" +
                                response.getQuestion().get(3) + "\n" +
                                response.getQuestion().get(4) + "\n" +
                                response.getQuestion().get(5));
                        String answer = bufTemp.readLine();
                        out.println(answer);
                    }
                }
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
