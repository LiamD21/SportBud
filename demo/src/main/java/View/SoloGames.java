package View;

import Controller.SoloGamesHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SoloGames {

    private final VBox root;
    private final SoloGamesHandler handler;

    /**
     * Constructor for the group games menu
     * @param stage The main stage that this scene will be displayed in
     */
    public SoloGames(Stage stage) {
        // create the root
        stage.setTitle("Solo Events");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(30);
        handler = new SoloGamesHandler();

        // Create elements
        Button backButton = new Button("Back");
        Text titleText = new Text("Solo Events");
        ChoiceBox<String> personChoice = new ChoiceBox<>();
        HBox newPerson = new HBox();
        newPerson.setAlignment(Pos.CENTER);

        // Modify Title text
        titleText.setFont(new Font(22));

        // create these for new person and add them to the Hbox
        TextField newName = new TextField();
        Button addPersonButton = new Button("Add Person");
        newPerson.getChildren().addAll(newName, addPersonButton);

        // Add choices to choice box based on list of solo people
        // TODO once we have get people method

        // Anchor Back Button
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(backButton, 15.0);
        AnchorPane.setLeftAnchor(backButton, 25.0);

        anchorPane.getChildren().addAll(backButton);

        // Add elements to root
        root.getChildren().addAll(anchorPane, titleText, newPerson);

        // Event listener for return to main menu
        backButton.setOnAction(event -> {
            MainMenu menu = new MainMenu();
            menu.start(stage);
            stage.setScene(menu.getScene());
            stage.setTitle("Menu");
        });

        // If button clicked, pass to the controller
        addPersonButton.setOnAction(event -> {
            if (newName.getCharacters() != null){
                handler.handleNewPerson(newName.getCharacters().toString());
            }
        });
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot(){
        return root;
    }
}