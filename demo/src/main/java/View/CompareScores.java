package View;

import Controller.CompareScoresHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CompareScores {

    private final VBox root;
    private final CompareScoresHandler handler;
    private String comparePersonID = null;
    private final Text theirBest;

    public CompareScores(Stage stage, String personID, String eventID){
        handler = new CompareScoresHandler(eventID, personID);

        // create the root
        stage.setTitle("Compare Your Golf Scores With Friends");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getStylesheets().add("style.css");

        // create elements
        Button backButton = new Button(" ◄ ");
        AnchorPane anchorPane = new AnchorPane();
        ChoiceBox<String> comparePersonBox = new ChoiceBox<>();
        Label compareLabel = new Label("Pick a player to compare against");
        HBox chooseBox = new HBox();
        Text myBest = new Text(String.format("Your best score is %d", handler.getBestScore(personID)));
        theirBest = new Text();

        // filling the choice box
        ArrayList<String> peopleToCompare = handler.getOtherGolfPlayers();
        for (String person:peopleToCompare){
            comparePersonBox.getItems().add(person);
        }

        // adding the person to compare against choices to the HBox
        chooseBox.setSpacing(15);
        chooseBox.setAlignment(Pos.CENTER);
        chooseBox.getChildren().addAll(compareLabel, comparePersonBox);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add to the root
        root.getChildren().addAll(anchorPane, chooseBox, myBest, theirBest);

        // event handler for the back button
        backButton.setOnAction(event -> {
            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        // event handler for the choice box
        comparePersonBox.setOnAction(event -> {
            comparePersonID = comparePersonBox.getValue();
            theirBest.setText(String.format("%s's best score is %d", handler.getPersonName(comparePersonID), handler.getBestScore(comparePersonID)));
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