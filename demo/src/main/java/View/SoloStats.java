package View;

import Controller.SoloStatsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private int chartScoreView = 0;
    private final Text avgInfo;

    public SoloStats(Stage stage, String eventID, String personID){
        handler = new SoloStatsHandler(eventID, personID);
        String type = handler.getEventType();

        // TODO add improvement stats
        // TODO add high and low scores

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // create elements
        Button backButton = new Button(" ◄ ");
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
        averageStat.getChildren().add(avgInfo);
        averageStat.setSpacing(20);
        averageStat.setAlignment(Pos.CENTER);

        // if this event is a golf event, add display choices
        if (handler.isGolfEvent()){
            averageStat.getChildren().addAll(averageChoice, checkAvgButton);
        }

        // adding choices to the average sorting choice box only if this is a golf event
        if (handler.isGolfEvent()) {
            averageChoice.getItems().add("Total");
            for (int i = start; i <= choices; i++) {
                averageChoice.getItems().add(String.valueOf(i));
            }
        }

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // Create a chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Attempt");
        yAxis.setLabel("Score");
        xAxis.setMinorTickVisible(false);
        final LineChart<Number, Number> progressChart = new LineChart<>(xAxis, yAxis);
        progressChart.setTitle("Scores Over Time");
        XYChart.Series series = new XYChart.Series();
        series.setName("Scores");
        ArrayList<Integer> data = handler.getChartData(chartScoreView);

        // adding elements to the chart
        for (int i = 0; i < data.size(); i++){
            series.getData().add(new XYChart.Data<>(i, data.get(i)));
        }
        progressChart.getData().add(series);

        // add elements to the root
        root.getChildren().addAll(anchorPane, averageStat, progressChart);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
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