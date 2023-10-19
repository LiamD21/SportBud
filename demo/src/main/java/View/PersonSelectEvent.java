package View;

import Controller.PersonSelectEventHandler;
import Model.Event;
import Model.Person;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PersonSelectEvent {
    private final VBox root;
    private final String username;
    private final PersonSelectEventHandler handler;

    public PersonSelectEvent(Stage stage, String username){
        this.username = username;
        handler = new PersonSelectEventHandler(username);

        // create the root
        stage.setTitle(String.format("%s's Information", handler.getName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        // create elements
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
        Text nameTitle = new Text(handler.getName());
        BorderPane myEvents = new BorderPane();
        HBox bottomButtons = new HBox();

        // Modify Title text
        nameTitle.setFont(new Font(22));

        // Create elements in the events BorderPane
        ListView<String> eventList = new ListView<String>();
        eventList.setPrefHeight(100);
        Text eventTitle = new Text(String.format("%s's Events", handler.getName()));
        myEvents.setLeft(eventTitle);
        myEvents.setBottom(eventList);

        // Add person's events to the list view
        for (Event event: handler.getEvents()){
            if (!event.getIsGroup()){
                eventList.getItems().add(event.getEventName());
            }
        }

        // create buttons for stats and create event pages in the bottom HBox
        Button statsButton = new Button(String.format("%s's Stats", handler.getName()));
        Button newEventButton = new Button("New Event");
        bottomButtons.getChildren().addAll(statsButton, newEventButton);
        bottomButtons.setSpacing(15);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 15.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to root
        root.getChildren().addAll(anchorPane, nameTitle, myEvents, bottomButtons);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloGames menu = new SoloGames(stage);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot(){
        return this.root;
    }
}
