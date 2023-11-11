package View;

import Controller.GroupEventLbHandler;
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
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GroupEventLeaderboard {
    private final VBox root;
    private final GroupEventLbHandler handler;
    private int scoreView = 0;

    public GroupEventLeaderboard(Stage stage, String eventID, String groupname) throws ParseException, IOException {
        handler = new GroupEventLbHandler(eventID, groupname);

        //VBox root creation
        stage.setTitle(String.format("%s Leaderboard", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        //Backbutton
        Button backButton = new Button("Back");

        //Anchorpane to hold the Boxes
        AnchorPane anchorPane = new AnchorPane();

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // Title shown with events name
        Text nameTitle = new Text(handler.getEventName());
        Text eventType = new Text();

        //borderpane to hold things?
        BorderPane leaderboardPane = new BorderPane();

        //Viewing list for each member of the groups score for the event
        ListView<String> eachMembersScores = new ListView<>();

        //HBox for the bottom buttons
        HBox bottomButtonsBox = new HBox();

        //Buttons/ widgets for sorting, adding scores, and going to the stats page
        ChoiceBox<String> leaderboardSorter = new ChoiceBox<>();
        Text sorterText = new Text("Leaderboard sorted by:");
        Button sort = new Button("Sort");
        Button addScoreButton = new Button("Add Score");
        Button toStatsPage = new Button("View More Stats");

        //add in the sorted leaderboard to the listview?
        ArrayList<String> listOfScores = handler.getScores(scoreView);
        for (String score : listOfScores){
            eachMembersScores.getItems().add(score);
        }

        eachMembersScores.setPrefHeight(150);
        eachMembersScores.setPrefWidth(300);



        //add the elements to the borderPane, specified to their location
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardPane.setLeft(leaderboardTitle);
        leaderboardPane.setBottom(eachMembersScores);


        // Modify Title text
        nameTitle.setFont(new Font(22));
        eventType.setFont(new Font(15));


        // set the event type text, to specify which sort of event it is
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


        // add all children to root
        root.getChildren().addAll(anchorPane, nameTitle, eventType, leaderboardPane);

        // only add sorting buttons if this event is a golf event and there are options to sort between
        if (handler.isGolfEvent()) {
            root.getChildren().addAll(bottomButtonsBox, toStatsPage);
        }

        else {
            root.getChildren().addAll(addScoreButton, toStatsPage);
        }



        // SETONACTIONS (eventhandlers) =====================================================

        // event listener for the back button
        backButton.setOnAction(event -> {
            GroupSelectEvent goBack = new GroupSelectEvent(stage,groupname);
            stage.setScene(new Scene(goBack.getRoot(), 500, 500));
        });

        // event listener for the add score button
        addScoreButton.setOnAction(event -> {
            GeneralAddScore menu = new GeneralAddScore(stage, "GroupEventStats", groupname, eventID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });


        //event listener for the sort button (must take into account all people in the group

        sort.setOnAction(event -> {
            //if there even is any scores to sort
            try {
                if (handler.hasScores()){
                    //
                    scoreView = handler.convertScoreView(leaderboardSorter.getValue());
                    ArrayList<String> EventLeaderBoard;

                    //
                    if (!Objects.equals(type, "Back 9") || scoreView == 0 || scoreView == -1) {
                        try {
                            EventLeaderBoard = handler.getScores(scoreView);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            EventLeaderBoard = handler.getScores(scoreView - 9);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    eachMembersScores.getItems().remove(0, EventLeaderBoard.size());
                    for (int i = 0; i < EventLeaderBoard.size(); i++) {
                        eachMembersScores.getItems().add(i, EventLeaderBoard.get(i));
                    }


                    // error message if nothing was selected to sort by
                    if (scoreView == -1) {
                        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                        invalidAlert.setContentText("Error: Select something to sort by before clicking sort");
                        invalidAlert.show();
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    public VBox getRoot(){return root;}


}
