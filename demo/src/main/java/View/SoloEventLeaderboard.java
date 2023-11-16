package View;

import Controller.SoloEventLbHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class SoloEventLeaderboard {

    private final VBox root;
    private final SoloEventLbHandler handler;
    private int scoreView = 0;

    public SoloEventLeaderboard(Stage stage, String eventID, String personID){
        handler = new SoloEventLbHandler(eventID, personID);

        // TODO add selector to decide if this event is sorted by fastest or slowest
        // TODO time and score event types

        // create the root
        stage.setTitle(String.format("%s Leaderboard", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // create elements
        Button backButton = new Button("🠔");
        AnchorPane anchorPane = new AnchorPane();
        Text nameTitle = new Text(handler.getEventName());
        Text eventType = new Text();
        BorderPane leaderboardPane = new BorderPane();
        ListView<String> personalBests = new ListView<>();
        ChoiceBox<String> leaderboardSorter = new ChoiceBox<>();
        Text sorterText = new Text("Leaderboard sorted by:");
        HBox bottomButtonsBox = new HBox();
        Button sort = new Button("Sort");
        Button addScoreButton = new Button("Add Score");
        Button toStatsPage = new Button("View More Stats");

        // add sorted score leaderboard to listview
        ArrayList<String> leaderboard = handler.getScores(scoreView);
        for (String score : leaderboard) {
            personalBests.getItems().add(score);
        }
        personalBests.setPrefHeight(100);
        personalBests.setPrefWidth(300);

        // add elements to the leaderboard borderPane
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardPane.setLeft(leaderboardTitle);
        leaderboardPane.setBottom(personalBests);

        // Modify Title text
        nameTitle.setFont(new Font(22));
        eventType.setFont(new Font(15));

        // Set event type text
        String type = handler.getEventType();
        if (Objects.equals(type, "18")){
            eventType.setText("This is an 18 hole golf event");
        }
        else if (Objects.equals(type, "Back 9")){
            eventType.setText("This is a golf event on the back 9");
        }
        else if (Objects.equals(type, "Front 9")){
            eventType.setText("This is a golf event on the front 9");
        }
        else {
            eventType.setText(String.format("This is a %s event", type));
        }

        // add sorting choice elements to their HBox
        bottomButtonsBox.getChildren().addAll(sorterText, leaderboardSorter, sort, addScoreButton);
        bottomButtonsBox.setSpacing(10);
        bottomButtonsBox.setAlignment(Pos.CENTER);

        // Add choices to the sorting choice box if this event is a golf event
        if (handler.isGolfEvent()) {
            int start = 1;
            int choices = 0;
            if (Objects.equals(type, "Front 9") || Objects.equals(type, "Back 9")) {
                choices = 9;
                if (Objects.equals(type, "Back 9")) {
                    start = 10;
                    choices = 18;
                }
            } else if (Objects.equals(type, "18")) {
                choices = 18;
            }
            leaderboardSorter.getItems().add("Total");
            for (int i = start; i <= choices; i++) {
                leaderboardSorter.getItems().add(String.valueOf(i));
            }
        }

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add all children to root
        root.getChildren().addAll(anchorPane, nameTitle, eventType, leaderboardPane);

        // only add sorting buttons if this event is a golf event and there are options to sort between
        if (handler.isGolfEvent()) {
            root.getChildren().addAll(bottomButtonsBox, toStatsPage);
        }

        else {
            root.getChildren().addAll(addScoreButton, toStatsPage);
        }

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, personID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        // event listener for the sort button
        // popup if the button was pressed but no specific hole was selected
        // popup if the button was pressed but there are no scores to sort
        sort.setOnAction(event -> {
            if (handler.hasScores()) {
                scoreView = handler.convertScoreView(leaderboardSorter.getValue());
                ArrayList<String> lb;

                // sort and add scores
                if (!Objects.equals(type, "Back 9") || scoreView == 0 || scoreView == -1) {
                    lb = handler.getScores(scoreView);
                } else {
                    lb = handler.getScores(scoreView - 9);
                }
                personalBests.getItems().remove(0, lb.size());
                for (int i = 0; i < lb.size(); i++) {
                    personalBests.getItems().add(i, lb.get(i));
                }

                // error message if nothing was selected to sort by
                if (scoreView == -1) {
                    Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                    invalidAlert.setContentText("Error: Select something to sort by before clicking sort");
                    invalidAlert.show();
                }
            }

            // error message if the event does not have any scores yet
            else {
                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                invalidAlert.setContentText("Error: There are no scores to sort");
                invalidAlert.show();
            }
        });

        // event listener for the add score button
        addScoreButton.setOnAction(event -> {
            GeneralAddScore menu = new GeneralAddScore(stage, "SoloEventStats", personID, eventID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        // event listener for the stats page button
        toStatsPage.setOnAction(event -> {
            SoloStats menu = new SoloStats(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
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
