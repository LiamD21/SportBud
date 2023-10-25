package View;

import Controller.SoloEventStatsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import java.util.concurrent.atomic.AtomicReference;

public class SoloEventStats {

    private final VBox root;
    private final String eventID;
    private final SoloEventStatsHandler handler;
    private int scoreView = 0;

    public SoloEventStats(Stage stage, String eventID, String personID){
        this.eventID = eventID;
        handler = new SoloEventStatsHandler(eventID, personID);

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("back");
        AnchorPane anchorPane = new AnchorPane();
        Text nameTitle = new Text(handler.getEventName());
        Text eventType = new Text();
        BorderPane leaderboardPane = new BorderPane();
        AtomicReference<ListView<Integer>> personalBests = new AtomicReference<>(new ListView<>());
        ChoiceBox<String> leaderboardSorter = new ChoiceBox<>();
        Text sorterText = new Text("Leaderboard sorted by:");
        HBox sorterBox = new HBox();
        Button sort = new Button("Sort");

        // TODO Liam add number of times played to leaderboard - Change int displayed to string to be able to display more info

        // add sorted score leaderboard to listview
        ArrayList<Integer> leaderboard = handler.getScores(scoreView);
        for (Integer integer : leaderboard) {
            personalBests.get().getItems().add(integer);
        }
        personalBests.get().setPrefHeight(100);
        personalBests.get().setPrefWidth(300);

        // add elements to the leaderboard borderPane
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardPane.setLeft(leaderboardTitle);
        leaderboardPane.setBottom(personalBests.get());

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

        // add sorting choice elements to their HBox
        sorterBox.getChildren().addAll(sorterText, leaderboardSorter);
        sorterBox.setSpacing(10);
        sorterBox.setAlignment(Pos.CENTER);

        // Add choices to the sorting choice box
        int choices = 1;
        if (Objects.equals(type, "Front 9") || Objects.equals(type, "Back 9")){
            choices = 10;
        }
        else if (Objects.equals(type, "18")){
            choices = 19;
        }
        leaderboardSorter.getItems().add("Total");
        for (int i = 1; i < choices; i++){
            leaderboardSorter.getItems().add(String.valueOf(i));
        }

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add all children to root
        root.getChildren().addAll(anchorPane, nameTitle, eventType, leaderboardPane, sorterBox, sort);

        // event listener for the back button
        backButton.setOnAction(event -> {
            PersonSelectEvent menu = new PersonSelectEvent(stage, personID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event listener for the sort button
        sort.setOnAction(event -> {
            scoreView = handler.convertScoreView(leaderboardSorter.getValue());
            ArrayList<Integer> lb = handler.getScores(scoreView);
            for (int i = 0; i < lb.size(); i++) {
                personalBests.get().getItems().remove(i);
                personalBests.get().getItems().add(i, lb.get(i));
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
