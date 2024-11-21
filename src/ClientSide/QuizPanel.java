package ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizPanel implements ActionListener {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton nextButton;
    private Protocol protocol;

    public QuizPanel() {
        protocol = new Protocol();
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


    private void showCategorySelection() {
        mainPanel.removeAll();
        JLabel label = new JLabel("Choose a category", JLabel.CENTER);
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        String[] categories = {"val 1", "val 2", "val 3"};
        for (String category : categories) {
            JButton button = new JButton(category);
            button.addActionListener(e -> {
                protocol.handleCategorySelection(category);
                showQuestionStage();
            });
            buttonPanel.add(button);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void showQuestionStage() {
        mainPanel.removeAll();
        ArrayList<String> questionData = protocol.getCurrentQuestion();
        if (questionData == null) {
            showFinalScore();
            return;
        }

        JLabel questionLabel = new JLabel(questionData.get(0), JLabel.CENTER);
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel(new GridLayout(4, 1));
        ButtonGroup group = new ButtonGroup();
        for (int i = 1; i < questionData.size(); i++) {
            JRadioButton answerButton = new JRadioButton(questionData.get(i));
            answerButton.setActionCommand(questionData.get(i));
            group.add(answerButton);
            answerPanel.add(answerButton);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (group.getSelection() != null) {
                protocol.submitAnswer(group.getSelection().getActionCommand());
                showScoreboard();
            } else {
                JOptionPane.showMessageDialog(frame, "väljs ditt svar");
            }
        });

        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.add(submitButton, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showScoreboard() {
        mainPanel.removeAll();
        JLabel scoreLabel = new JLabel("poäng" + protocol.getCurrentScore(), JLabel.CENTER);
        mainPanel.add(scoreLabel, BorderLayout.CENTER);

        nextButton = new JButton("Next  ");
        nextButton.addActionListener(e -> showCategorySelection());
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void showFinalScore() {
        mainPanel.removeAll();
        JLabel finalScoreLabel = new JLabel("Game Over! Final Score: " + protocol.getCurrentScore(), JLabel.CENTER);
        mainPanel.add(finalScoreLabel, BorderLayout.CENTER);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> showCategorySelection());
        mainPanel.add(playAgainButton, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizPanel::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
