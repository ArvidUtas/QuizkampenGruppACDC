package ServerSide;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import com.google.gson.*;

public class Category {

    public ArrayList<String> getQuestionsList(String category) {
        //String url = "https://opentdb.com/api.php?amount=6&category=" + category + "&difficulty=easy&type=multiple";
        String url = "https://opentdb.com/api.php?amount=6&category=11&difficulty=easy&type=multiple";
        ArrayList<String> questAndAns = new ArrayList<>();

        // Create HttpClient and HttpRequest
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            // send request and receive reply
            HttpResponse<String> apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (apiResponse.statusCode() == 200) { //Status code 200 = request is successful
                // read JSON as String
                String json = apiResponse.body();
                // Parse JSON w Gson to array with all Q n A
                JsonArray resultsArray = JsonParser.parseString(json)
                        .getAsJsonObject().getAsJsonArray("results");
                //Create question and answers list
                for (JsonElement element : resultsArray) {

                    JsonObject questionObject = element.getAsJsonObject();
                    //Get incorrect answers from array and add to list
                    JsonArray incorrectAnswersArray = questionObject.getAsJsonArray("incorrect_answers");
                    for (int i = 0; i < incorrectAnswersArray.size(); i++) {
                        String incorrectAnswer = incorrectAnswersArray.get(i).getAsString().
                                replaceAll("&#039;", "'").
                                replaceAll("&quot;", "\"");
                        questAndAns.add((incorrectAnswer));
                    }
                    String correct_answer = questionObject.get("correct_answer").getAsString()
                            .replaceAll("&#039;", "'").replaceAll("&quot;", "\"");
                    questAndAns.add(correct_answer); //Add correct answer as one of answer options
                    Collections.shuffle(questAndAns); //Shuffle answer options

                    questAndAns.addFirst(correct_answer); //Added at [1], position of correct answer

                    String question = questionObject.get("question").getAsString().
                            replaceAll("&#039;", "'").replaceAll("&quot;", "\"");
                    questAndAns.addFirst(question); //Added at [0], position of question

                    System.out.println(questAndAns.toString());
                }

            } else {
                System.out.println("Fel: " + apiResponse.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    return questAndAns;
    }

}
