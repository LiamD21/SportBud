package View;

import Controller.GeneralAddScoreHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GeneralAddScore {

    private final VBox root;
    private final String eventID;
    private final String personID;
    private final String groupID;
    private final GeneralAddScoreHandler handler;
    private final String lastPage;
    private final ChoiceBox<String> people;

    public GeneralAddScore(Stage stage, String lastPage, String personID, String groupID, String eventName){
        handler = new GeneralAddScoreHandler(eventName,personID, lastPage, groupID);
        eventID = eventName;
        this.personID = personID;
        this.groupID = groupID;
        this.lastPage = lastPage;
        people = new ChoiceBox<>();

        // create the root
        stage.setTitle(String.format("Add Your %s Score", eventID));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getStylesheets().add("style.css");

        // create elements
        Button backButton = new Button(" ◄ ");
        AnchorPane anchorPane = new AnchorPane();
        VBox scoreEntryBox = new VBox();

        // do VBox alignment
        scoreEntryBox.setAlignment(Pos.CENTER);
        scoreEntryBox.setSpacing(15);
        scoreEntryBox.setPadding(new Insets(20));

        // if the event is a group event, add a dropdown to select who we add the score for
        if (Objects.equals(lastPage, "GroupEventStats")){
            Label peopleBoxLabel = new Label("Select the group member to give a new score to");

            ArrayList<String> groupPeople = handler.getPeople();
            for (String person:groupPeople){
                people.getItems().add(person);
            }

            scoreEntryBox.getChildren().addAll(peopleBoxLabel, people);
        }

        // create score entering elements
        Label label;
        if (handler.isGolfEvent()){
            label = new Label("Enter your scores, \neach hole separated by a comma");
        }
        else if (handler.isTimedEvent()){
            label = new Label("Enter your new score in the form hh:mm:ss or mm:ss or just ss");
        }
        else{
            label = new Label("Enter your new score");
        }
        TextField scoreIn = new TextField("Enter Scores Here");
        Button submit = new Button("Submit");
        scoreEntryBox.getChildren().addAll(label, scoreIn, submit);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add children to the root
        root.getChildren().addAll(anchorPane, scoreEntryBox);

        // event listener for the back button
        backButton.setOnAction(event -> {
            if (Objects.equals(this.lastPage, "SoloEventStats")) {
                SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
                stage.setScene(new Scene(menu.getRoot(), 800, 600));
            }
            else if (Objects.equals(this.lastPage, "GroupEventStats")){
                GroupEventLeaderboard menu = null;
                try {
                    menu = new GroupEventLeaderboard(stage, eventID, groupID);
                } catch (ParseException | IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(new Scene(menu.getRoot(), 800, 600));
            }
        });

        // event listener for the add scores button
        submit.setOnAction(event -> {
            if (!scoreIn.getCharacters().toString().equals("Enter Scores Here")){
                if (handler.isGolfEvent()) {
                    String[] scores = scoreIn.getCharacters().toString().split(",");

                    // check for valid score input
                    if (((Objects.equals(handler.getEventType(), "Front 9") || Objects.equals(handler.getEventType(), "Back 9")) && scores.length == 9) ||
                            (Objects.equals(handler.getEventType(), "18") && scores.length == 18)) {
                        int[] intScores = new int[scores.length];

                        // check for valid score input of individual hole scores
                        try {
                            for (int i = 0; i < scores.length; i++) {
                                if (scores[i].charAt(0) == ' ') {
                                    scores[i] = scores[i].substring(1);
                                }
                                intScores[i] = Integer.parseInt(scores[i]);
                            }

                            // go back to the leaderboard after entering a new score
                            if (Objects.equals(this.lastPage, "SoloEventStats")) {
                                handler.setScore(intScores, personID);

                                SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
                                stage.setScene(new Scene(menu.getRoot(), 800, 600));

                                // show popup if it was a new high score
                                if (handler.isBestScore(intScores)){
                                    Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                    highScore.setContentText("NEW HIGH SCORE!");
                                    highScore.show();
                                }
                            }

                            //else go back to the group leaderboard if its from there.
                            // group scores must have a person selected to enter it
                            else if (Objects.equals(this.lastPage, "GroupEventStats")){
                                if (people.getValue() != null){
                                    handler.setScore(intScores, people.getValue());

                                    GroupEventLeaderboard menu = null;
                                    try {
                                        menu = new GroupEventLeaderboard(stage, eventID, groupID);
                                    } catch (ParseException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    stage.setScene(new Scene(menu.getRoot(), 800, 600));

                                    // show popup if it was a new high score
                                    if (handler.isBestScore(intScores)){
                                        Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                        highScore.setContentText("NEW HIGH SCORE!");
                                        highScore.show();
                                    }
                                }
                                else {
                                    Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                                    invalidAlert.setContentText("Error: To add a score in a group event, you must select a person.");
                                    invalidAlert.show();
                                }
                            }
                        } catch (NumberFormatException | NullPointerException e){
                            Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                            inputAlert.setContentText("Error: Input for each hole's score must be an integer");
                            inputAlert.show();
                        }
                    }

                    // if invalid input, display error popup
                    else {
                        Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                        inputAlert.setContentText("Error: Input for a Golf event must be either 9 or 18 hole scores separated by commas");
                        inputAlert.show();
                    }
                }
                else if (handler.isTimedEvent()){
                    // split hour, min, seconds
                    String[] splitTime = scoreIn.getCharacters().toString().split(":");

                    int[] intScores = new int[splitTime.length];
                    // check for valid score input of minute and second counts
                    try {
                        for (int i = splitTime.length - 1; i >= 0; i--) {
                            if (splitTime[i].charAt(0) == ' ') {
                                splitTime[i] = splitTime[i].substring(1);
                            }
                            intScores[i] = Integer.parseInt(splitTime[i]);
                        }

                        // change score to a count of seconds only
                        int[] finalTime = new int[]{0};
                        if (intScores.length == 3){
                            finalTime[0] += (intScores[0] * 60 * 60);
                            finalTime[0] += (intScores[1] * 60);
                            finalTime[0] += intScores[2];
                        }
                        else if (intScores.length == 2){
                            finalTime[0] += (intScores[0] * 60);
                            finalTime[0] += intScores[1];
                        }
                        else {
                            finalTime[0] += intScores[0];
                        }

                        // go back to the leaderboard after entering a new score
                        if (Objects.equals(this.lastPage, "SoloEventStats")) {
                            handler.setScore(finalTime, personID);

                            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
                            stage.setScene(new Scene(menu.getRoot(), 800, 600));

                            // show popup if it was a new high score
                            if (handler.isBestScore(finalTime)){
                                Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                highScore.setContentText("NEW HIGH SCORE!");
                                highScore.show();
                            }
                        }

                        //else go back to the group leaderboard if its from there.
                        // group scores must have a person selected to enter it
                        else if (Objects.equals(this.lastPage, "GroupEventStats")){
                            if (people.getValue() != null){
                                handler.setScore(finalTime, people.getValue());

                                GroupEventLeaderboard menu = null;
                                try {
                                    menu = new GroupEventLeaderboard(stage, eventID, groupID);
                                } catch (ParseException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                                stage.setScene(new Scene(menu.getRoot(), 800, 600));

                                // show popup if it was a new high score
                                if (handler.isBestScore(finalTime)){
                                    Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                    highScore.setContentText("NEW HIGH SCORE!");
                                    highScore.show();
                                }
                            }
                            else {
                                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                                invalidAlert.setContentText("Error: To add a score in a group event, you must select a person.");
                                invalidAlert.show();
                            }
                        }
                    } catch (NumberFormatException | NullPointerException e){
                        Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                        inputAlert.setContentText("Error: Input for each hole's score must be an integer");
                        inputAlert.show();
                    }
                }
                else {

                    // check for valid score input
                    try {
                        int score = Integer.parseInt(scoreIn.getCharacters().toString());
                        int[] scores = {score};

                        // go back to the leaderboard after entering a new score
                        if (Objects.equals(lastPage, "SoloEventStats")) {
                            handler.setScore(scores, personID);

                            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
                            stage.setScene(new Scene(menu.getRoot(), 800, 600));

                            // show popup if it was a new high score
                            if (handler.isBestScore(scores)){
                                Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                highScore.setContentText("NEW HIGH SCORE!");
                                highScore.show();
                            }
                        }

                        else if (Objects.equals(lastPage, "GroupEventStats")){
                            if (people.getValue() != null) {
                                handler.setScore(scores, people.getValue());

                                GroupEventLeaderboard menu = null;
                                try {
                                    menu = new GroupEventLeaderboard(stage, eventID, groupID);
                                } catch (ParseException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                                stage.setScene(new Scene(menu.getRoot(), 800, 600));

                                // show popup if it was a new high score
                                if (handler.isBestScore(scores)){
                                    Alert highScore = new Alert(Alert.AlertType.INFORMATION);
                                    highScore.setContentText("NEW HIGH SCORE!");
                                    highScore.show();
                                }
                            }
                            else {
                                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                                invalidAlert.setContentText("Error: To add a score in a group event, you must select a person.");
                                invalidAlert.show();
                            }

                        }
                    } catch (NumberFormatException | NullPointerException e){
                        e.printStackTrace();
                        Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                        inputAlert.setContentText("Error: Input for an event must be entered as a single integer");
                        inputAlert.show();
                    }
                }
            }
        });
    }
    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot() {
        return root;
    }
}
