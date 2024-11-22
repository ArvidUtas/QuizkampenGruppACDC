package ClientSide;

import AccessFromBothSides.EnumCategories;
import AccessFromBothSides.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class QuizPanel {
    private JFrame frame;
    private JPanel mainPanel;
    private Protocol protocol;

    public QuizPanel() {
        protocol = new Protocol("localhost", 23456); // Anslut till servern
        MainFrame();
        showCategorySelection();
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
    private void showCategorySelection() {
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
            JButton button = new JButton(enumCategories.toString());
            button.setPreferredSize(new Dimension(200, 50));
            button.addActionListener(e -> {
                handleCategorySelection(enumCategories.getText());
                Response questionResponse = protocol.receiveFromServer();
                if (questionResponse != null && questionResponse.getType() == Response.QUESTION) {
                    showQuestionStage(questionResponse.getQuestionData());
                }
            });
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Hantera kategorivalet
    private void handleCategorySelection(String category) {
        Response categoryResponse = new Response(Response.CATEGORY, 0, 0, 0, 0, null, category);
        protocol.sendToServer(categoryResponse); // Skicka kategorin till servern
    }

    // Visa frågestadiet
    private void showQuestionStage(ArrayList<String> questionData) {
        mainPanel.removeAll();

        JLabel questionLabel = new JLabel(questionData.get(0), JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i < questionData.size(); i++) {
            JButton answerButton = new JButton(questionData.get(i));
            answerButton.setPreferredSize(new Dimension(200, 50));
            answerButton.addActionListener(e -> {
                handleAnswerSelection(answerButton.getText()); // Skicka svaret till servern
                Response response = protocol.receiveFromServer(); // Vänta på feedback
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizPanel::new);
    }
}
