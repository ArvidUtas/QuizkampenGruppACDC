package ClientSide;

import AccessFromBothSides.EnumCategories;
import AccessFromBothSides.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class QuizPanel {
    Client client;
    private JFrame frame;
    private JPanel mainPanel;
    private Protocol protocol;
    private String chosenCat = "";
    private Socket socket;
    private ObjectInputStream in;
    private PrintWriter out;

    public QuizPanel(Socket socket, PrintWriter out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        // protocol = new Protocol("localhost", 23456); // Anslut till servern
        MainFrame();
        //showCategorySelection();
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

    // Visa kategorivalet
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
            button.addActionListener(e -> {
                sendStringToServer(enumCategories.getValue());
//                handleCategorySelection(enumCategories.getValue());
//                Response questionResponse = protocol.receiveFromServer();
//                if (questionResponse != null && questionResponse.getType() == Response.QUESTION) {
//                    showQuestionStage(questionResponse.getQuestionData());
//                }
            });
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    void emptyFrame(String message) {
        mainPanel.removeAll();

        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        mainPanel.add(label, BorderLayout.NORTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Hantera kategorivalet
    private void handleCategorySelection(String category) {
        //Response categoryResponse = new Response(Response.CATEGORY, 0, 0, 0, 0, null, category);
        //protocol.sendToServer(categoryResponse); // Skicka kategorin till servern
        System.out.println(category);
        sendStringToServer(category); // Skicka kategorin till servern
    }

    // Visa frågestadiet
    public void showQuestionStage(ArrayList<String> questionData) {
        mainPanel.removeAll();

        JLabel questionLabel = new JLabel(questionData.getFirst(), JLabel.CENTER);
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
                    if (component instanceof JButton) {
                        component.setEnabled(false);
                    }
                }

                Response response = receiveFromServer(); // Vänta på feedback
                if (response != null && response.getType() == Response.ANSWER_CHECK) {
                    showFeedback(response.getMessage()); // Visa feedback
                }
            });
            answerPanel.add(answerButton);
            answerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
        }

        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Hantera svaret
    private void handleAnswerSelection(String answer) {
        Response answerResponse = new Response(Response.ANSWER, 0, 0, 0, 0, null, answer);
        protocol.sendToServer(answerResponse); // Skicka svaret till servern
    }

    // Visa feedback på svaret
    private void showFeedback(String feedback) {
        JOptionPane.showMessageDialog(frame, feedback);
        Response nextQuestionResponse = protocol.receiveFromServer(); // Vänta på nästa fråga eller poäng
        if (nextQuestionResponse != null) {
            if (nextQuestionResponse.getType() == Response.QUESTION) {
                showQuestionStage(nextQuestionResponse.getQuestionData());
            } else if (nextQuestionResponse.getType() == Response.FINAL_SCORE) {
                showFinalScore(nextQuestionResponse.getMessage());
            }
        }
    }

    // Visa slutresultatet
    private void showFinalScore(String finalScore) {
        mainPanel.removeAll();

        JLabel finalScoreLabel = new JLabel("Slutresultat: " + finalScore, JLabel.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(finalScoreLabel, BorderLayout.CENTER);

        JButton playAgainButton = new JButton("Spela igen");
        playAgainButton.addActionListener(e -> showCategorySelection());
        mainPanel.add(playAgainButton, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
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
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(QuizPanel::new);
//    }
}
