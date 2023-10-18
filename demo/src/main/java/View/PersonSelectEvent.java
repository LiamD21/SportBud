package View;

import Controller.PersonSelectEventHandler;
import Model.Person;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        // create elements
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("Back");
        Text nameTitle = new Text(handler.getName());
        HBox myEvents = new HBox();
        Button newEvent = new Button("New Solo Event");

        // Modify Title text
        nameTitle.setFont(new Font(22));

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 15.0);
        AnchorPane.setLeftAnchor(backButton, 25.0);

        anchorPane.getChildren().addAll(backButton);

        // TODO Liam - Add to HBox a selector menu with information about all current events for this person
        // TODO Liam - Add button to bring the user to a create new event page

        // add elements to root
        root.getChildren().addAll(anchorPane, nameTitle);

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
