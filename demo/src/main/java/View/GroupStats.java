package View;

import Controller.GroupStatsHandler;
import Model.Group;
import Model.Person;
import Model.Score;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GroupStats {
    private final VBox root;
    private final GroupStatsHandler handler;
    private final String groupName;
    private final String eventID;
    private int statScoreView = 0;
    private final Text avgInfo;
    private final Text minInfo;
    private final Text maxInfo;
    private final BarChart<String, Number> ScoreChart;
    ArrayList<XYChart.Series> series;


    public GroupStats(Stage stage, String eventname, String groupname){
        eventID = eventname;
        groupName = groupname;
        handler = new GroupStatsHandler(groupName, eventname);
        String type = handler.getEventType();
        series = new ArrayList<>();


        // VBox root creation
        stage.setTitle(String.format("%s Leaderboard", handler.getGroupName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(10));

        //Buttons and windows
        Button backButton  =  new Button( "Back");

        // creating the stat filter choices and selection button
        ChoiceBox<String> specificChoice = new ChoiceBox<>();
        Button refreshAllStatsButton = new Button("Refresh All Stats");


        // panes in which the nodes will be laid out
        AnchorPane anchorPane = new AnchorPane();
        VBox numberStats = new VBox();
        HBox statFilter = new HBox();

        Text nameTitle = new Text(String.format("%s's StatsPage", groupName));
        nameTitle.setFont(new Font(20));


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

        // if this event is a golf event, add display choices to the stat filter container
        if (handler.isGolfEvent()){
            statFilter.getChildren().addAll(specificChoice, refreshAllStatsButton);
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

        //fill the stats above with data and information
        int avg = handler.getAverage(statScoreView);
        String high  = handler.getHigh(statScoreView);
        String low = handler.getLow(statScoreView);

        avgInfo = new Text(String.format("Overall average at this hole is: %d", avg));
        minInfo = new Text("The loser of the hole is: " + low);
        maxInfo = new Text("The winner of the hole is: " + high);

        //setup the barChart

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Player");
        yAxis.setLabel("Score");

//        //set the series, each player in the group should have one (their color and bar)
//        // send to handler
//        for (int i = 0 ; i < handler.getGroupPeople().size(); i++){
//            //creates a series for the person (color of bars)
//            XYChart.Series personSeries = new XYChart.Series<>();
//            personSeries.setName(handler.getGroupPeople().get(i));
//
//            //take the series, and populate it with the scores of the event based on the hole chosen
//            handler.populatePersonsSeries(personSeries, statScoreView);
//
//            //now add it to the list of series? Each series in list should be added to chart
//            series.add(personSeries);
//        }

        //fill up the series list with each players series (scores)
        fillSeriesList(statScoreView);


        //chart creation with the x and y axis
        ScoreChart = new BarChart<>(xAxis, yAxis);
        ScoreChart.setTitle(eventname + " Scores for group: " + groupname);

        // for every series in the series list, add in the series to the barChart
        for (int i = 0; i < series.size(); i++){
            ScoreChart.getData().add(series.get(i));
        }


        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);
        //set up the stats
        numberStats.getChildren().addAll(avgInfo, maxInfo, minInfo);
        numberStats.setSpacing(8);
        numberStats.setAlignment(Pos.CENTER);


        //populate the root node
        root.getChildren().addAll(anchorPane, numberStats, ScoreChart, statFilter);




        // EVENT HANDLERS ___________________________________________________________________________


        // event listener for the back button
        backButton.setOnAction(event -> {
            GroupEventLeaderboard menu = null;
            try {
                menu = new GroupEventLeaderboard(stage, eventID, groupName);
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        refreshAllStatsButton.setOnAction(event -> {
            statScoreView = handler.convertScoreView(specificChoice.getValue());



            //remove all the series so that the barchart can be refilled
            while (!ScoreChart.getData().isEmpty()){
                ScoreChart.getData().remove(0);
                System.out.println(ScoreChart.getData());
            }

            //clean the serieslist
            emptySeriesList();

            int newAverage;
            String newHigh;
            String newLow;

            //get the new average scoring, the new Leader, and the new loser and recalculate the date
            // for the barCharts bars. (showing based on hole, not total)

            //if the type is back 9, or the option hole is total or nothing
            if (!Objects.equals(type, "Back 9") || statScoreView == 0 || statScoreView == -1) {
                fillSeriesList(statScoreView);  //refill out the barchart's data (not yet plugged in)
                newAverage = handler.getAverage(statScoreView);
                newHigh = handler.getHigh(statScoreView);
                newLow = handler.getLow(statScoreView);
            }

            //else ... uhh.. if its not the back nine or total?
            else {
                fillSeriesList(statScoreView-9);
                newAverage = handler.getAverage(statScoreView - 9);
                newHigh = handler.getHigh(statScoreView - 9);
                newLow = handler.getLow(statScoreView - 9);
            }

            //repopulate the details.
            avgInfo.setText(String.format("Overall average at this hole is: %d", newAverage));
            minInfo.setText("The loser of the hole is: " + newLow);
            maxInfo.setText("The winner of the hole is: " + newHigh);



            // for every series in the series list, add in the series to the barChart
            for (int i = 0; i < series.size(); i++){
                ScoreChart.getData().add(series.get(i));
            }

            // error message if nothing was selected to sort by
            if (statScoreView == -1){
                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                invalidAlert.setContentText("Error: Select something to get average by before trying to get a new average");
                invalidAlert.show();
            }


        });


    }

    public void fillSeriesList(int holeNumber){
        //set the series, each player in the group should have one (their color and bar)
        // send to handler
        for (int i = 0 ; i < this.handler.getGroupPeople().size(); i++){
            //creates a series for the person (color of bars)
            XYChart.Series personSeries = new XYChart.Series<>();
            personSeries.setName(this.handler.getGroupPeople().get(i));

            //take the series, and populate it with the scores of the event based on the hole chosen
            this.handler.populatePersonsSeries(personSeries, holeNumber);

            //now add it to the list of series? Each series in list should be added to chart
            this.series.add(personSeries);
        }
    }

    public void emptySeriesList(){
        //while there are series in the series list, remove it. (because we need to repopulate the names
        // with new scores for specified hole.

        while (!this.series.isEmpty()){
            this.series.remove(0);
            System.out.println(series);
        }
    }




    public VBox getRoot(){
        return root;
    }
}
