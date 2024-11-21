package ClientSide;

import java.util.ArrayList;

public class Protocol {
    private int score;

    public Protocol() {
        score = 0;
    }

    public void handleCategorySelection(String category) {
        System.out.println("Category selected: " + category);
    }

    public ArrayList<String> getCurrentQuestion() {
        ArrayList<String> questionData = new ArrayList<>();
        questionData.add("gör ett val");
        questionData.add("1");
        questionData.add("2");
        questionData.add("3");
        questionData.add("4");
        return questionData;
    }

    public void submitAnswer(String answer) {
        if ("1".equals(answer)) {
            score += 1;
        }
    }

    public int getCurrentScore() {
        return score;
    }
}
