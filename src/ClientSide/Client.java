package ClientSide;

import AccessFromBothSides.EnumCategories;
import AccessFromBothSides.Response;

import java.io.*;
import java.net.Socket;

public class Client {
    private PrintWriter out;

    public Client() {

        String hostName = "localhost";
        int portNumber = 23456;

        try {
            Socket socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            BufferedReader bufTemp = new BufferedReader(new InputStreamReader(System.in));

            QuizPanel quizPanel = new QuizPanel(socket, out, in);

//            System.out.println("Connected to " + hostName + "\nPort: " + portNumber);
           // System.out.println(in.readObject());
            Object obj;

            while (true) {
                obj = in.readObject();
                System.out.println(obj); //skriver ut toString-metoden
                if (obj instanceof Response response) {
                    if (response.getType() == Response.CATEGORY) {
                        //System.out.println(response.getMessage());
                        quizPanel.showCategorySelection();
//                        String chosenCat = quizPanel.showCategorySelection();
//                        String catToSend = "";
//                        System.out.println(chosenCat);
//                        for (EnumCategories category : EnumCategories.values()) {
//                            if (chosenCat.equalsIgnoreCase(category.getText())) {
//                                catToSend = category.getValue();
//                                break;
//                            }
//                        }
//                        out.println(catToSend);

                    } else if (response.getType() == Response.OTHERPCATEGORY) {
                        quizPanel.emptyFrame(response.getMessage());
                    } else if (response.getType() == Response.QUESTION) {
                        quizPanel.showQuestionStage(response.getQuestionData());
                        System.out.println("Question: " + response.getQuestion().getFirst() + "\n" +
                                response.getQuestion().get(2) + "\n" +
                                response.getQuestion().get(3) + "\n" +
                                response.getQuestion().get(4) + "\n" +
                                response.getQuestion().get(5));
                        //String answer = bufTemp.readLine();
                        //out.println(answer);
                    } else if (response.getType() == Response.ANSWER_CHECK) {
                        System.out.println(response.getMessage());
                    } else if (response.getType() == Response.FINAL_SCORE) {
                        System.out.println(response.getMessage());
                        System.out.println("Player 1: " + response.getPlayer1score() + " Player 2: " + response.getPlayer2score());
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
