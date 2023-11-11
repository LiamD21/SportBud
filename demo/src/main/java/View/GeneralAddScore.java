package View;

import Controller.GeneralAddScoreHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class GeneralAddScore {

    private final VBox root;

    private final String eventID;

    private final String genericID;

    private final GeneralAddScoreHandler handler;

    private final String lastPage;

    public GeneralAddScore(Stage stage, String lastPage, String ID, String eventName){
        handler = new GeneralAddScoreHandler(eventName, ID, lastPage);
        eventID = eventName;
        genericID = ID;
        this.lastPage = lastPage;

        // create the root
        stage.setTitle(String.format("Add Your %s Score", eventID));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("Back");
        AnchorPane anchorPane = new AnchorPane();
        VBox scoreEntryBox = new VBox();

        // create score entering elements
        Label label;
        if (handler.isGolfEvent()){
            label = new Label("Enter your scores, \neach hole separated by a comma");
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
                SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, genericID);
                stage.setScene(new Scene(menu.getRoot(), 500, 500));
            }
            else if (Objects.equals(this.lastPage, "GroupEventStats")){
                GroupEventLeaderboard menu = null;
                try {
                    menu = new GroupEventLeaderboard(stage, eventID, genericID);
                } catch (ParseException | IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(new Scene(menu.getRoot(), 500, 500));
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
                            handler.setScore(intScores);

                            // go back to the leaderboard after entering a new score
                            if (Objects.equals(this.lastPage, "SoloEventStats")) {
                                SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, genericID);
                                stage.setScene(new Scene(menu.getRoot(), 500, 500));
                            }

                            //else go back to the group leaderboard if its from there.
                            else if (Objects.equals(this.lastPage, "GroupEventStats")){
                                GroupEventLeaderboard menu = null;
                                try {
                                    menu = new GroupEventLeaderboard(stage, eventID, genericID);
                                } catch (ParseException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                                stage.setScene(new Scene(menu.getRoot(), 500, 500));

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
                else {

                    // check for valid score input
                    try {
                        int score = Integer.parseInt(scoreIn.getCharacters().toString());
                        int[] scores = {score};
                        handler.setScore(scores);

                        // go back to the leaderboard after entering a new score
                        if (Objects.equals(lastPage, "SoloEventStats")) {
                            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, genericID);
                            stage.setScene(new Scene(menu.getRoot(), 500, 500));
                        }

                        else if (Objects.equals(lastPage, "GroupEventStats")){
                            GroupEventLeaderboard menu = null;
                            try {
                                menu = new GroupEventLeaderboard(stage, eventID, genericID);
                            } catch (ParseException | IOException e) {
                                throw new RuntimeException(e);
                            }
                            stage.setScene(new Scene(menu.getRoot(), 500, 500));

                        }
                    } catch (NumberFormatException | NullPointerException e){
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
