package View;

import Controller.SoloStatsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class SoloStats {

    private final VBox root;
    private final SoloStatsHandler handler;
    private int averageScoreView = 0;
    private Text avgInfo;

    public SoloStats(Stage stage, String eventID, String personID){
        handler = new SoloStatsHandler(eventID, personID);
        String type = handler.getEventType();

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("back");
        AnchorPane anchorPane = new AnchorPane();
        HBox averageStat = new HBox();

        // create a list of choices to sort by
        int start = 1;
        int choices = 0;
        if (Objects.equals(type, "Front 9") || Objects.equals(type, "Back 9")){
            choices = 9;
            if (Objects.equals(type, "Back 9")){
                start = 10;
                choices = 18;
            }
        }
        else if (Objects.equals(type, "18")){
            choices = 18;
        }

        // find average
        int avg = handler.getAverage(averageScoreView);

        // adding elements to the average display box
        ChoiceBox<String> averageChoice = new ChoiceBox<>();
        avgInfo = new Text(String.format("Your Average Score is: %d", avg));
        Button checkAvgButton = new Button("Check Average");
        averageStat.getChildren().addAll(avgInfo, averageChoice, checkAvgButton);
        averageStat.setSpacing(20);
        averageStat.setAlignment(Pos.CENTER);

        // adding choices to the average sorting choice box
        averageChoice.getItems().add("Total");
        for (int i = start; i <= choices; i++){
            averageChoice.getItems().add(String.valueOf(i));
        }

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to the root
        root.getChildren().addAll(anchorPane, averageStat);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event listener for the average choice box button
        checkAvgButton.setOnAction(event -> {
            averageScoreView = handler.convertScoreView(averageChoice.getValue());
            int newAverage;

            // find the new average
            if (!Objects.equals(type, "Back 9") || averageScoreView == 0 || averageScoreView == -1) {
                newAverage = handler.getAverage(averageScoreView);
            }
            else {
                newAverage = handler.getAverage(averageScoreView - 9);
            }
            // set new average
            avgInfo.setText(String.format("Your Average Score is: %d", newAverage));

            // error message if nothing was selected to sort by
            if (averageScoreView == -1){
                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                invalidAlert.setContentText("Error: Select something to get average by before trying to get a new average");
                invalidAlert.show();
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