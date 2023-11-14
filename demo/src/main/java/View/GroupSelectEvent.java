package View;

import Controller.GroupSelectEventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GroupSelectEvent {
    private final VBox root;
    private final String groupName;
    private final GroupSelectEventHandler handler;

    public GroupSelectEvent(Stage stage, String groupNameArg){
        groupName = groupNameArg;
        handler = new GroupSelectEventHandler(groupName);

        stage.setTitle(String.format("Group %s's Information", groupName));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));


        // panes in which the nodes will be laid out
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
        backButton.setPrefSize(60,25);


        Text nameTitle = new Text(String.format("%s's HomePage", groupName));
        nameTitle.setFont(new Font(22));
        VBox theGroupsEvents = new VBox();
        HBox bottomButtons = new HBox();


        //Listview to show the roster of the group
        ListView<String> roster = new ListView<String>();
        roster.setPrefHeight(100);
        roster.setMaxWidth(200);
        roster.setMaxHeight(200);

        Text rosterTitle = new Text(String.format("Group Roster"));
        rosterTitle.setFont(new Font(16));
        // add in the people within the group to see.
        for (String person: handler.getGroupMembers()){
            if (!handler.getGroupMembers().isEmpty())
                roster.getItems().add(person);
        }


        //Listview to show all the events for the group
        ListView<String> eventList = new ListView<String>();
        eventList.setPrefHeight(100);

        Text eventTitle = new Text(String.format("%s's Events", groupName));
        eventTitle.setFont(new Font(16));

        Button selectEvent = new Button("View Selected Event");
        selectEvent.setPadding(new Insets(5));
        selectEvent.setPrefSize(60, 80);
        selectEvent.setWrapText(true);


        //HBOX to hold the select Event parts
        HBox selectEventParts = new HBox();
        selectEventParts.setSpacing(5);
        selectEventParts.getChildren().addAll(eventList, selectEvent);


        theGroupsEvents.getChildren().addAll(rosterTitle,roster, eventTitle, selectEventParts);

        //Add groups events to the list view
        for (String event : handler.getEvents()){
            if (!event.isEmpty()){
                eventList.getItems().add(event);
            }
        }

        // create buttons for stats and create event pages in the bottom HBox
        Button statsButton = new Button(String.format("%s's Stats", groupName));
        statsButton.setPrefHeight(25);
        statsButton.setPrefWidth(statsButton.getText().length()*7);

        Button newEventButton = new Button("New Event");
        newEventButton.setPrefSize(125,25);

        bottomButtons.getChildren().addAll(statsButton, newEventButton);
        bottomButtons.setSpacing(15);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to root
        root.getChildren().addAll(anchorPane, nameTitle, theGroupsEvents, bottomButtons);



        // Event listener for return to GroupGames menu
        backButton.setOnAction(event -> {
            GroupGames groupgames = new GroupGames(stage);
            stage.setScene(new Scene(groupgames.getRoot(), 500, 500));
            stage.setTitle("Group Events");
        });

        //Event listener for selecting events
        selectEvent.setOnAction(event -> {
            if (eventList.getSelectionModel().getSelectedItem() != null) {
                GroupEventLeaderboard eventPage = null;
                try {
                    eventPage = new GroupEventLeaderboard(stage,
                            eventList.getSelectionModel().getSelectedItem(), groupName);
                } catch (ParseException | IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(new Scene(eventPage.getRoot(), 500, 500));
            }
            else {
                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                invalidAlert.setContentText("Error: Select an Event above to see the leaderboard");
                invalidAlert.show();
            }
        });

        //event listener for new Event
        newEventButton.setOnAction(event ->{
            //TODO
            //New event page stage set scene

            CreateGroupEvent cge = new CreateGroupEvent(stage, groupName);
            stage.setScene(new Scene(cge.getRoot(), 500, 500));
        });

        //event listener for stats Page

        statsButton.setOnAction(event ->{
            //TODO  *This may not be needed*
            //stats page stage set scene
//            GroupStats groupStats = new GroupStats(stage, groupName);
//            stage.setScene((new Scene(groupStats.getRoot(), 500, 500)));

        });
    }

    public VBox getRoot(){
        return root;
    }
}
