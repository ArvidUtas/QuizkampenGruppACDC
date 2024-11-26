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
import static java.lang.Thread.sleep;

public class QuizPanel {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton clickedButton;
    private final String backgroundImagePath = "src/ClientSide/graphics/gradient.png";
    private final String buttonImagePath = "src/ClientSide/graphics/button.png";
    private final String buttonGreenImagePath = "src/ClientSide/graphics/buttonGreen.png";
    private ImageIcon buttonIcon = new ImageIcon(buttonImagePath);
    private ImageIcon buttonGreenIcon = new ImageIcon(buttonGreenImagePath);
    private ImageIcon backgroundIcon = new ImageIcon(backgroundImagePath);
    private Image background = backgroundIcon.getImage();
    private Socket socket;
    private ObjectInputStream in;
    private PrintWriter out;
    private JScrollPane scrollPane;

    // Konstruktor för nätverk + gui
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
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) { // Anpassad bakgrundsbild
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Scroll
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // visar meddelanden typ välkommen
    public void messageFrame(String message) {
        mainPanel.removeAll();
        JLabel label = new JLabel("<html><div style='width:400px;'>" + message + "</div></html>", JLabel.CENTER);
        label.setFont(new Font("Montserrat", Font.PLAIN, 24));
        label.setForeground(Color.GRAY);
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showCategorySelection() {
        mainPanel.removeAll();

        JLabel label = new JLabel("Choose a category", JLabel.CENTER);
        label.setFont(new Font("Montserrat", Font.PLAIN, 24));
        label.setForeground(Color.DARK_GRAY);
        mainPanel.add(label, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3,2));
        buttonPanel.setOpaque(false);

        ArrayList<EnumCategories> listOfCategories = new ArrayList<>();
        Collections.addAll(listOfCategories, EnumCategories.values());

        for (EnumCategories enumCategories : listOfCategories) {
            JButton button = new JButton(enumCategories.getText(), buttonIcon);
            button.setBorderPainted(false); // Kanterna syns inte
            button.setContentAreaFilled(false); // Transparent
            button.setHorizontalTextPosition(SwingConstants.CENTER); // Får plats i rutan
            button.setVerticalTextPosition(SwingConstants.CENTER); // Får plats i rutan
            button.addActionListener(e -> sendStringToServer(enumCategories.getValue()));
            buttonPanel.add(button);
            //buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Mellanrum
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
        answerPanel.setLayout(new GridLayout(2,2));
        answerPanel.setOpaque(false);

        Font buttonFont = new Font("Arial", Font.PLAIN, 16);

        for (int i = 2; i < questionData.size(); i++) {
            JButton answerButton = new JButton(questionData.get(i), buttonIcon);
            answerButton.setBorderPainted(false);
            answerButton.setContentAreaFilled(false);
            answerButton.setHorizontalTextPosition(SwingConstants.CENTER);
            answerButton.setVerticalTextPosition(SwingConstants.CENTER);
            answerButton.setFont(buttonFont);
            answerButton.setFocusPainted(true);

            answerButton.addActionListener(e -> {
                sendStringToServer(answerButton.getText()); // Skicka svaret till servern
                clickedButton = answerButton;

                for (Component component : answerPanel.getComponents()) { //hindrar att man kan klicka på flera svar
                    if (component instanceof JButton && component != clickedButton) {
                        component.setEnabled(false);
                    }
                }
            });
            answerPanel.add(answerButton);

        }

        mainPanel.add(answerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Visa feedback på svaret
    public void showFeedback(String feedback) {
        if (feedback.equals("Wrong!")) { //TODO: change to boolean
            clickedButton.setForeground(Color.red);
        } else
            clickedButton.setIcon(buttonGreenIcon);
        try {
            sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Visa slutresultatet
    public void showFinalScore(Response response) {
        mainPanel.removeAll();
        JPanel southPanel = new JPanel(new GridLayout(1,2));

        JLabel finalScoreLabel = new JLabel("<html>" + response.getMessage() + "<br>Player 1: "
                + response.getPlayer1score() + " - Player 2: "
                + response.getPlayer2score() + "</html>", JLabel.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(finalScoreLabel, BorderLayout.CENTER);

        JButton playAgainButton = new JButton("Play again");
        JButton exitButton = new JButton("Exit");
        playAgainButton.addActionListener(e -> Client.replayable = true);
        exitButton.addActionListener(e -> System.exit(0));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        southPanel.setOpaque(false);
        southPanel.add(playAgainButton);
        southPanel.add(exitButton);

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
