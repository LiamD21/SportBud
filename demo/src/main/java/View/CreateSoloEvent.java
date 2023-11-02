package View;

import Controller.CreateSoloEventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 15.0);
        anchorPane.getChildren().addAll(backButton);

        // TODO Liam add create event functionality

        // add elements to the root
        root.getChildren().addAll(anchorPane);

        // event handling for the back button
        backButton.setOnAction(event -> {
            SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, username);
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
