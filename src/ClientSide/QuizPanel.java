package ClientSide;

import AccessFromBothSides.EnumCategories;
import AccessFromBothSides.Response;
import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class QuizPanel {
    private JFrame frame;
    private JPanel mainPanel;
    private Protocol protocol;
    private String chosenCat = "";
    private ArrayList<String> roundScoreList = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream in;
    private PrintWriter out;

    public QuizPanel(Socket socket, PrintWriter out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        MainFrame();
    }

    private void MainFrame() {
        frame = new JFrame("Quizkampen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // visar meddelanden typ välkommen
    public void messageFrame(String message) {
        mainPanel.removeAll();
        JLabel label = new JLabel("<html><div style='width:400px;'>" + message + "</div></html>", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showCategorySelection() {
        mainPanel.removeAll();

        JLabel label = new JLabel("Välj en kategori", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        ArrayList<EnumCategories> listOfCategories = new ArrayList<>();
        Collections.addAll(listOfCategories, EnumCategories.values());

        for (EnumCategories enumCategories : listOfCategories) {
            JButton button = new JButton(enumCategories.getText());
            button.setPreferredSize(new Dimension(200, 50));
            button.addActionListener(e -> sendStringToServer(enumCategories.getValue()));
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showQuestionStage(ArrayList<String> questionData) {
        mainPanel.removeAll();

        JLabel questionLabel = new JLabel("<html><div style='width:400px;'>" +
                questionData.getFirst() + "</div></html>", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));

        for (int i = 2; i < questionData.size(); i++) {
            JButton answerButton = new JButton(questionData.get(i));
            answerButton.setPreferredSize(new Dimension(200, 50));
            answerButton.addActionListener(e -> {
                sendStringToServer(answerButton.getText()); // Skicka svaret till servern
//                handleAnswerSelection(answerButton.getText()); // Skicka svaret till servern

                for (Component component : answerPanel.getComponents()) { //hindrar att man kan klicka på flera svar
                    if (component instanceof JButton) {                   //eventuelt onödig
                        component.setEnabled(false);
                    }
                }

//                Response response = receiveFromServer(); // Vänta på feedback
//                if (response != null && response.getType() == Response.ANSWER_CHECK) {
//                    showFeedback(response.getMessage()); // Visa feedback
//                }
            });
            answerPanel.add(answerButton);
            answerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
        }

        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

//    // Hantera svaret
//    private void handleAnswerSelection(String answer) {
//        Response answerResponse = new Response(Response.ANSWER, 0, 0, 0, 0, null, answer);
//        protocol.sendToServer(answerResponse); // Skicka svaret till servern
//    }

    // Visa feedback på svaret
    public void showFeedback(String feedback) {
        JOptionPane.showMessageDialog(frame, feedback);
    }

    public void showRoundScore(Response response) {
        final String cont = "Continue";
        mainPanel.removeAll();
        JPanel centrePanel = new JPanel(new GridLayout(10, 1));
        mainPanel.add(centrePanel,BorderLayout.CENTER);
        JLabel label = new JLabel("Round " + response.getCurrentRound() + " score", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        JButton contButton = new JButton(cont);
        contButton.addActionListener(e -> {
            sendStringToServer(cont);
        });
        roundScoreList.add("Player 1: " + response.getP1RoundScore() + "\t\t\t\t\t\t" + response.getCurrentRound()
                + "\t\t\t\t\t\tPlayer 2: " + response.getP2RoundScore());
        for (String s : roundScoreList) {
            JLabel rScoreLabel = new JLabel(s, JLabel.CENTER);
            centrePanel.add(rScoreLabel, BorderLayout.CENTER);
        }
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(contButton, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Visa slutresultatet
    public void showFinalScore(Response response) {
        mainPanel.removeAll();
        JPanel centrePanel = new JPanel(new GridLayout(10, 1));
        mainPanel.add(centrePanel,BorderLayout.CENTER);
        JLabel finalScoreLabel1 = new JLabel(response.getMessage(), JLabel.CENTER);
        JLabel finalScoreLabel2 = new JLabel("Player 1: "
                + response.getPlayer1score() + "\t\t\t - \t\t\tPlayer 2: "
                + response.getPlayer2score(), JLabel.CENTER);
        finalScoreLabel1.setFont(new Font("Arial", Font.BOLD, 20));
        finalScoreLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        centrePanel.add(finalScoreLabel1);
        centrePanel.add(finalScoreLabel2);

        roundScoreList.add("Player 1: " + response.getP1RoundScore() + "\t\t\t\t\t\t" + response.getCurrentRound()
                + "\t\t\t\t\t\tPlayer 2: " + response.getP2RoundScore());
        for (String s : roundScoreList) {
            JLabel rScoreLabel = new JLabel(s, JLabel.CENTER);
            centrePanel.add(rScoreLabel);
        }

        JButton playAgainButton = new JButton("Play again");
        playAgainButton.addActionListener(e -> sendStringToServer("PLAY_AGAIN"));


        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            sendStringToServer("EXIT GAME");
        });
//        } {
//            sendStringToServer("PLAY_AGAIN");
//            resetGame();
//            showCategorySelection();
//        });

        mainPanel.add(playAgainButton, BorderLayout.SOUTH);
        mainPanel.add(quitButton, BorderLayout.EAST);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public  void resetGame(){
        roundScoreList.clear();



    }

    public void sendStringToServer(String message) {
        try {
            out.println(message);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    // Skicka data till servern
//    public void sendToServer(Response response) {
//        try {
//            out.writeObject(response);
//            out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Hämta data från servern
//    public Response receiveFromServer() {
//        try {
//            return (Response) in.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // Stäng anslutningen
//    public void closeConnection() {
//        try {
//            in.close();
//            out.close();
//            socket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
