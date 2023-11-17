package View;

import Controller.CreateSoloEventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class CreateSoloEvent {
    private final VBox root;
    private final String username;
    private final CreateSoloEventHandler handler;
    private String basicType;
    private String golfType;
    private String sortType;

    public CreateSoloEvent(Stage stage, String username){
        this.username = username;
        handler = new CreateSoloEventHandler(username);

        // create root
        stage.setTitle("Create New Solo Event");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // create elements
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
        TextField eventName = new TextField("Enter Your Event's Name");
        Label basicChoiceLabel = new Label("Select your event's type");
        ChoiceBox<String> basicEventTypes = new ChoiceBox<>();
        Label golfChoiceLabel = new Label("Which holes is this golf event on?");
        ChoiceBox<String> golfEventTypes = new ChoiceBox<>();
        Label sortingChoiceLabel = new Label("Is a high or low score the best?");
        ChoiceBox<String> sortingEventTypes = new ChoiceBox<>();
        Button confirm = new Button("Create Event");
        VBox vbox = new VBox(basicChoiceLabel, basicEventTypes);

        // add event types to the choice boxes
        basicEventTypes.getItems().addAll("Golf", "Timed", "Points");
        golfEventTypes.getItems().addAll("18", "Front 9", "Back 9");
        sortingEventTypes.getItems().addAll("Highest", "Lowest");

        // set up the vbox with the elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(20));

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 15.0);
        anchorPane.getChildren().addAll(backButton);

        // add elements to the root
        root.getChildren().addAll(anchorPane, vbox);

        // event handling for the back button
        backButton.setOnAction(event -> {
            SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, username);
            stage.setScene(new Scene(menu.getRoot(), 800, 600));
        });

        // event handling for the basic event type selector
        basicEventTypes.setOnAction(event -> {
            basicType = basicEventTypes.getSelectionModel().getSelectedItem();
            vbox.getChildren().removeAll(vbox.getChildren());
            if (Objects.equals(basicType, "Golf")){
                vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, golfChoiceLabel, golfEventTypes);
            }
            else {
                vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, sortingChoiceLabel, sortingEventTypes);
            }
        });

        // event handling for the golf event type selector
        golfEventTypes.setOnAction(event -> {
            golfType = golfEventTypes.getSelectionModel().getSelectedItem();
            vbox.getChildren().removeAll(vbox.getChildren());
            vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, golfChoiceLabel, golfEventTypes, eventName, confirm);
        });

        // event handling for the sorting event type selector
        sortingEventTypes.setOnAction(event -> {
            sortType = sortingEventTypes.getSelectionModel().getSelectedItem();
            vbox.getChildren().removeAll(vbox.getChildren());
            vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, sortingChoiceLabel, sortingEventTypes, eventName, confirm);
        });

        // event handling for the create event button
        // text field must have a name and a type must be selected
        confirm.setOnAction(event -> {
            if (!eventName.getText().equals("Enter Your Event's Name")){

                // get the event type name to use
                String eventType = "";
                if (Objects.equals(basicType, "Golf")){
                    eventType = golfType;
                }
                else if (Objects.equals(basicType, "Timed")){
                    if (Objects.equals(sortType, "Highest")){
                        eventType = "Time-Highest";
                    }
                    else if (Objects.equals(sortType, "Lowest")){
                        eventType = "Time-Lowest";
                    }
                }
                else if (Objects.equals(basicType, "Points")){
                    if (Objects.equals(sortType, "Highest")){
                        eventType = "Points-Highest";
                    }
                    else if (Objects.equals(sortType, "Lowest")){
                        eventType = "Points-Lowest";
                    }
                }

                handler.createEvent(eventName.getText(), eventType);

                // returns to the previous menu after creating the new event successfully
                SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, username);
                stage.setScene(new Scene(menu.getRoot(), 800, 600));

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setContentText(String.format("%s event named %s created!", eventType, eventName.getText()));
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