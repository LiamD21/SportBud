package View;

import Controller.CreateSoloEventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateSoloEvent {
    private final VBox root;
    private final String username;
    private final CreateSoloEventHandler handler;

    public CreateSoloEvent(Stage stage, String username){
        this.username = username;
        handler = new CreateSoloEventHandler(username);

        // create root
        stage.setTitle("Create New Solo Event");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);

        // create elements
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
        TextField eventName = new TextField("Enter Your Event's Name");
        Label choiceLabel = new Label("Select your event's type");
        ChoiceBox<String> eventTypes= new ChoiceBox<>();
        Button confirm = new Button("Create Event");
        VBox vbox = new VBox(choiceLabel, eventTypes, eventName, confirm);

        // add event types to the choice box
        eventTypes.getItems().addAll("18", "Front 9", "Back 9");

        // set up the vbox with the elements
        vbox.setAlignment(Pos.CENTER);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 15.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to the root
        root.getChildren().addAll(anchorPane, vbox);

        // event handling for the back button
        backButton.setOnAction(event -> {
            SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, username);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // event handling for the create event button
        // text field must have a name and a type must be selected
        confirm.setOnAction(event -> {
            if (!eventName.getCharacters().toString().equals("Enter Your Event's Name") && eventTypes.getValue() != null){
                handler.createEvent(eventName.getCharacters().toString(), eventTypes.getValue());

                // returns to the previous menu after creating the new event successfully
                SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, username);
                stage.setScene(new Scene(menu.getRoot(), 500, 500));

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setContentText(String.format("%s event named %s created!", eventTypes.getValue(), eventName.getCharacters().toString()));
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
