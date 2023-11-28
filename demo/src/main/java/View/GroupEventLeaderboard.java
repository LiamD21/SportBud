package View;

import Controller.GroupEventLbHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupEventLeaderboard {
    private final VBox root;
    private final GroupEventLbHandler handler;
    private int scoreView = 0;
    private boolean sortByHighest;

    public GroupEventLeaderboard(Stage stage, String eventID, String groupname) throws ParseException, IOException {
        handler = new GroupEventLbHandler(eventID, groupname);
        sortByHighest = handler.getSortByHighest();

        // VBox root creation
        stage.setTitle(String.format("%s Leaderboard", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getStylesheets().add("style.css");

        // Backbutton
        Button backButton = new Button(" ◄ ");

        // Anchorpane to hold the Boxes
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
        eachMembersScores.setPadding(new Insets(10, 10, 10, 10));
        eachMembersScores.setPrefWidth(stage.getWidth());

        //HBox for the bottom buttons
        HBox bottomButtonsBox = new HBox();

        //Buttons/ widgets for sorting, adding scores, and going to the stats page
        ChoiceBox<String> leaderboardSorter = new ChoiceBox<>();
        Text sorterText = new Text("Leaderboard sorted by:");
        Button sort = new Button("Sort");
        Button addScoreButton = new Button("Add Score");
        //HBox for splitting the view between leaderboard and comments
        Button toStatsPage = new Button("View More Stats");

        // Set event type text and the sorting parameters
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
        else if (Objects.equals(type, "Points-Highest")){
            eventType.setText("This is a high score event");
        }
        else if (Objects.equals(type, "Points-Lowest")){
            eventType.setText("This is a low score event");
        }
        else if (Objects.equals(type, "Time-Highest")){
            eventType.setText("This is a high time event");
        }
        else if (Objects.equals(type, "Time-Lowest")){
            eventType.setText("This is a low time event");
        }

        //add in the sorted leaderboard to the listview
        // sort the leaderboard depending on if higher is better or not
        ArrayList<String> listOfScores = handler.getScores(scoreView);
        if (!sortByHighest) {
            for (String score : listOfScores) {
                eachMembersScores.getItems().add(score);
            }
        }
        else {
            for (int i = listOfScores.size() - 1; i >= 0; i--) {
                eachMembersScores.getItems().add(listOfScores.get(i));
            }
        }

        eachMembersScores.setPrefHeight(150);
        eachMembersScores.setPrefWidth(400);

        //add the elements to the borderPane, specified to their location
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardPane.setTop(leaderboardTitle);
        leaderboardPane.getTop().setTranslateX(leaderboardPane.getWidth()/2 - leaderboardPane.getWidth()/2);
        leaderboardPane.setCenter(eachMembersScores);
        leaderboardPane.setPadding(new Insets(10, 10, 10, 10));

        // Modify Title text
        nameTitle.setFont(new Font(22));
        eventType.setFont(new Font(15));

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

        //comment things
        BorderPane commentPane = new BorderPane();
        commentPane.setPadding(new Insets(10, 10, 10, 10));
        Text commentsTitle = new Text("Comments on " + handler.getEventName());
        commentsTitle.setTextAlignment(TextAlignment.CENTER);
        ListView<String> commentView = new ListView<>();
        for (String s: handler.getComments()) {
            commentView.getItems().add(s);
        }
        commentView.setStyle("-fx-font-size : 10");
        commentView.setMinHeight(50);
        commentView.setPrefHeight(commentView.getItems().size() * 10 + 10);
        commentView.setMinWidth(200);
        Button addCommentButton = new Button("add");
        addCommentButton.setAlignment(Pos.CENTER);
        // TODO - adding scores on button
        commentPane.setTop(commentsTitle);
        commentPane.setCenter(commentView);
        commentPane.setBottom(addCommentButton);
        commentPane.setPrefHeight(50);
        commentPane.setPrefWidth(75);

        // AnchorPane for putting comments at the bottom right
        AnchorPane bottomAnchor = new AnchorPane();
        AnchorPane.setRightAnchor(commentPane, 5.0);
        AnchorPane.setBottomAnchor(commentPane, 5.0);
        bottomAnchor.getChildren().add(commentPane);

        root.getChildren().add(bottomAnchor);

        // SETONACTIONS (eventhandlers) =====================================================

        // event listener for the back button
        backButton.setOnAction(event -> {
            GroupSelectEvent goBack = new GroupSelectEvent(stage,groupname);
            stage.setScene(new Scene(goBack.getRoot(), 800, 600));
        });

        // event listener for the add score button
        addScoreButton.setOnAction(event -> {
            GeneralAddScore menu = new GeneralAddScore(stage, "GroupEventStats", null, groupname, eventID);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        // event listener for the stats page button
        toStatsPage.setOnAction(event -> {
            GroupStats menu = new GroupStats(stage, eventID, groupname);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
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
                        } catch (ParseException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            EventLeaderBoard = handler.getScores(scoreView - 9);
                        } catch (ParseException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    eachMembersScores.getItems().remove(0, EventLeaderBoard.size());

                    // add all scores again, order depending on if we want higher on top or not
                    if (!sortByHighest) {
                        for (int i = 0; i < EventLeaderBoard.size(); i++) {
                            eachMembersScores.getItems().add(i, EventLeaderBoard.get(i));
                        }
                    }
                    else {
                        for (int i = EventLeaderBoard.size() - 1; i >= 0; i--) {
                            eachMembersScores.getItems().add(i, EventLeaderBoard.get(i));
                        }
                    }


                    // error message if nothing was selected to sort by
                    if (scoreView == -1) {
                        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                        invalidAlert.setContentText("Error: Select something to sort by before clicking sort");
                        invalidAlert.show();
                    }
                }
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    public VBox getRoot(){return root;}


}
