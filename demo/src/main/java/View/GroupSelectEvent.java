package View;

import Controller.GroupSelectEventHandler;
import Model.Event;
import Model.Person;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GroupSelectEvent {
    private final VBox root;
    private final String groupName;
    private final GroupSelectEventHandler handler;

    public GroupSelectEvent(Stage stage, String groupNameArg){
        groupName = groupNameArg;
        handler = new GroupSelectEventHandler(groupName);

//        stage.setTitle(String.format("Group %s's Information", handler.getName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));


        // panes in which the nodes will be laid out
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
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

//        Button selectEvent = new Button();



        //fill the borderpane theGroupsEvents
//        theGroupsEvents.setLeft(rosterTitle);
//        theGroupsEvents.setBottom(roster);
//        theGroupsEvents.setLeft(eventTitle);
//        theGroupsEvents.setBottom(eventList);
        theGroupsEvents.getChildren().addAll(rosterTitle,roster, eventTitle, eventList);



        //Add groups events to the list view
        for (Event event : handler.getEvents()){
            if (event.getIsGroup()){
                eventList.getItems().add(event.getEventName());
            }
        }

        // create buttons for stats and create event pages in the bottom HBox
        Button statsButton = new Button(String.format("%s's Stats", groupName));
        Button newEventButton = new Button("New Event");
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





    }

    public VBox getRoot(){
        return root;
    }
}
