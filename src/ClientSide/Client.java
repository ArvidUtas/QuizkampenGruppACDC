package ClientSide;

import AccessFromBothSides.Response;
import java.io.*;
import java.net.Socket;

public class Client {
        public static boolean replayable = true;

        public Client() {

            String hostName = "localhost";
            int portNumber = 23456;

            while (replayable) {
                try {
                    Socket socket = new Socket(hostName, portNumber);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    QuizPanel quizPanel = new QuizPanel(socket, out, in);
                    Object obj;

                    while (true) {
                        obj = in.readObject();
                        System.out.println(obj);
                        if (obj instanceof Response response) {
                            if (response.getType() == Response.MESSAGE) {
                                quizPanel.messageFrame(response.getMessage());
                            } else if (response.getType() == Response.CATEGORY) {
                                quizPanel.showCategorySelection();
                            } else if (response.getType() == Response.QUESTION) {
                                quizPanel.showQuestionStage(response.getQuestionData());
                            } else if (response.getType() == Response.ANSWER_CHECK) {
                                quizPanel.showFeedback(response.getMessage());
                            } else if (response.getType() == Response.ROUND_SCORE) {
                                quizPanel.showRoundScore(response);
                            } else if (response.getType() == Response.FINAL_SCORE) {
                                quizPanel.showFinalScore(response);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    public static void main(String[] args) {
        Client client = new Client();
    }
}