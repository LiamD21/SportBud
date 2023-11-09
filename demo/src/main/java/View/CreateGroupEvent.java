package View;

import Controller.CreateGroupEventHandler;
import Controller.CreateSoloEventHandler;
import Model.Group;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateGroupEvent {

    final String groupName;

    final VBox root;

    final CreateGroupEventHandler handler;

    public CreateGroupEvent(Stage stage, String gName) {
        stage.setTitle("Create New Group Event");
        groupName = gName;
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        handler = new CreateGroupEventHandler(gName);

        AnchorPane ap = new AnchorPane();
        Button backB = new Button("Back");
        TextField eName = new TextField("Enter your event's name");
        Label cLabel = new Label("Select your event's type");
        ChoiceBox<String> eventTypes = new ChoiceBox<>();
        Button confirm = new Button("Create Event");
        VBox vbox = new VBox(cLabel, eventTypes, eName, confirm);

        // add event types to the choice box
        // Other event type just means not a golf event
        eventTypes.getItems().addAll("18", "Front 9", "Back 9", "Other");

        vbox.setAlignment(Pos.CENTER);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backB, 10.0);
        AnchorPane.setLeftAnchor(backB, 15.0);
        ap.getChildren().addAll(backB);

        // add elements to the root
        root.getChildren().addAll(ap, vbox);

        // event handling for the back button
        backB.setOnAction(event -> {
            GroupSelectEvent menu = new GroupSelectEvent(stage, groupName);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event handling for the create event button
        // text field must have a name and a type must be selected
        confirm.setOnAction(event -> {
            if (!eName.getCharacters().toString().equals("Enter Your Event's Name") && eventTypes.getValue() != null){
                handler.createEvent(groupName, eName.getCharacters().toString(), eventTypes.getValue());

                // goes back to event select after creating
                GroupSelectEvent menu = new GroupSelectEvent(stage, groupName);
                stage.setScene(new Scene(menu.getRoot(), 500, 500));

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setContentText(String.format("%s event named %s created!", eventTypes.getValue(), eName.getCharacters().toString()));
                confirmAlert.show();
            }
            else {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setContentText("Error: An event name must be entered and an event type must be selected");
                failAlert.show();
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
