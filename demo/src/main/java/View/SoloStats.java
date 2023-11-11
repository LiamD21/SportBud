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
    private int statScoreView = 0;
    private final Text avgInfo;
    private final Text minInfo;
    private final Text maxInfo;
    private final LineChart<Number, Number> progressChart;
    private XYChart.Series series;

    public SoloStats(Stage stage, String eventID, String personID){
        handler = new SoloStatsHandler(eventID, personID);
        String type = handler.getEventType();

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15));

        // create elements
        Button backButton = new Button("back");
        AnchorPane anchorPane = new AnchorPane();
        VBox numberStats = new VBox();
        HBox statFilter = new HBox();

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

        // creating the stat filter choices and selection button
        ChoiceBox<String> specificChoice = new ChoiceBox<>();
        Button filterButton = new Button("Refresh All Stats");

        // if this event is a golf event, add display choices to the stat filter container
        if (handler.isGolfEvent()){
            statFilter.getChildren().addAll(specificChoice, filterButton);
            statFilter.setSpacing(22);
            statFilter.setAlignment(Pos.CENTER);
        }

        // adding choices to the average sorting choice box only if this is a golf event
        if (handler.isGolfEvent()) {
            specificChoice.getItems().add("Total");
            for (int i = start; i <= choices; i++) {
                specificChoice.getItems().add(String.valueOf(i));
            }
        }

        // TODO change labels depending on if we sort by lowest or highest
        // find stats and add them to the display box
        int avg = handler.getAverage(statScoreView);
        int[] maxMin = handler.getHighLowScores(statScoreView);
        avgInfo = new Text(String.format("Your Average Score is: %d", avg));
        minInfo = new Text(String.format("Your Lowest Score is: %d", maxMin[1]));
        maxInfo = new Text(String.format("Your Highest Score is: %d", maxMin[0]));
        numberStats.getChildren().addAll(avgInfo, maxInfo, minInfo);
        numberStats.setSpacing(8);
        numberStats.setAlignment(Pos.CENTER);

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
        progressChart = new LineChart<>(xAxis, yAxis);
        progressChart.setTitle("Scores Over Time");
        series = new XYChart.Series();
        series.setName("Scores");
        ArrayList<Integer> data = handler.getChartData(statScoreView);

        // adding elements to the chart
        for (int i = 0; i < data.size(); i++){
            series.getData().add(new XYChart.Data<>(i, data.get(i)));
        }
        progressChart.getData().add(series);

        // add elements to the root
        root.getChildren().addAll(anchorPane, numberStats, progressChart, statFilter);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event listener for the average choice box button
        filterButton.setOnAction(event -> {
            statScoreView = handler.convertScoreView(specificChoice.getValue());
            int newAverage;
            int[] newHighLow;
            ArrayList<Integer> newData;

            // find the new average, high, low, and overall data
            if (!Objects.equals(type, "Back 9") || statScoreView == 0 || statScoreView == -1) {
                newAverage = handler.getAverage(statScoreView);
                newHighLow = handler.getHighLowScores(statScoreView);
                newData = handler.getChartData(statScoreView);
            }
            else {
                newAverage = handler.getAverage(statScoreView - 9);
                newHighLow = handler.getHighLowScores(statScoreView - 9);
                newData = handler.getChartData(statScoreView - 9);
            }
            // set new average, high, low, and overall data
            avgInfo.setText(String.format("Your Average Score is: %d", newAverage));
            maxInfo.setText(String.format("Your Highest Score is: %d", newHighLow[0]));
            minInfo.setText(String.format("Your Lowest Score is: %d", newHighLow[1]));

            // changing the data series on the chart
            progressChart.getData().remove(series);
            series = new XYChart.Series();
            for (int i = 0; i < newData.size(); i++){
                series.getData().add(new XYChart.Data<>(i, newData.get(i)));
            }
            series.setName("Scores");
            progressChart.getData().add(series);

            // error message if nothing was selected to sort by
            if (statScoreView == -1){
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