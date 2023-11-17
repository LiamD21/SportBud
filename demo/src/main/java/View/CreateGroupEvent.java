package View;

import Controller.CreateGroupEventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class CreateGroupEvent {

    final String groupName;

    final VBox root;

    final CreateGroupEventHandler handler;
    private String basicType;
    private String golfType;
    private String sortType;

    public CreateGroupEvent(Stage stage, String gName) {
        stage.setTitle("Create New Group Event");
        groupName = gName;
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        handler = new CreateGroupEventHandler(gName);

        AnchorPane ap = new AnchorPane();
        Button backB = new Button("Back");
        TextField eName = new TextField("Enter your event's name");
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

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(20));

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backB, 10.0);
        AnchorPane.setLeftAnchor(backB, 15.0);
        ap.getChildren().addAll(backB);

        // add elements to the root
        root.getChildren().addAll(ap, vbox);

        // event handling for the back button
        backB.setOnAction(event -> {
            GroupSelectEvent menu = new GroupSelectEvent(stage, groupName);
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
            vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, golfChoiceLabel, golfEventTypes, eName, confirm);
        });

        // event handling for the sorting event type selector
        sortingEventTypes.setOnAction(event -> {
            sortType = sortingEventTypes.getSelectionModel().getSelectedItem();
            vbox.getChildren().removeAll(vbox.getChildren());
            vbox.getChildren().addAll(basicChoiceLabel, basicEventTypes, sortingChoiceLabel, sortingEventTypes, eName, confirm);
        });

        // event handling for the create event button
        // text field must have a name and a type must be selected
        confirm.setOnAction(event -> {
            if (!eName.getText().equals("Enter Your Event's Name")){

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

                handler.createEvent(groupName, eName.getText(), eventType);

                // returns to the previous menu after creating the new event successfully
                GroupSelectEvent menu = new GroupSelectEvent(stage, groupName);
                stage.setScene(new Scene(menu.getRoot(), 800, 600));

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setContentText(String.format("%s event named %s created!", eventType, eName.getText()));
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
