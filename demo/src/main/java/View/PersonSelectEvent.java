package View;

import Controller.PersonSelectEventHandler;
import Model.Event;
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
        root.setPadding(new Insets(10));

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
        Button statsButton = new Button("View Selected Event Stats");
        Button newEventButton = new Button("New Event");
        bottomButtons.getChildren().addAll(statsButton, newEventButton);
        bottomButtons.setSpacing(15);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to root
        root.getChildren().addAll(anchorPane, nameTitle, myEvents, bottomButtons);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloGames menu = new SoloGames(stage);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event listener for the create new solo event button
        newEventButton.setOnAction(event -> {
            CreateSoloEvent menu = new CreateSoloEvent(stage, username);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event listener for the view selected event button
        statsButton.setOnAction(event -> {
            if (eventList.getSelectionModel().getSelectedItem() != null) {
                SoloEventStats menu = new SoloEventStats(stage, eventList.getSelectionModel().getSelectedItem(), username);
                stage.setScene(new Scene(menu.getRoot(), 500, 500));
            }
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
