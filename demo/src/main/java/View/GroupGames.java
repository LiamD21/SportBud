package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GroupGames {

   private final VBox root;

    /**
     * Constructor for the group games menu
     * @param stage The main stage that this scene will be displayed in
     */
    public GroupGames(Stage stage) {
        // create the root
        stage.setTitle("Group Events");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(30);

        // Create elements
        Button backButton = new Button("Back");
        Text titleText = new Text("Group Events");
        ChoiceBox<String> groupChoice = new ChoiceBox<>();
        Button newGroupButton = new Button("Create a New Group");

        // Modify Title text
        titleText.setFont(new Font(22));

        // add all groups as choices in the choice box
        // TODO once we have get groups method

        // Anchor Buttons
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(backButton, 15.0);
        AnchorPane.setLeftAnchor(backButton, 25.0);

        anchorPane.getChildren().addAll(backButton);

        // Add to root
        root.getChildren().addAll(anchorPane, titleText, groupChoice, newGroupButton);

        // Event listener for return to main menu
        backButton.setOnAction(event -> {
            MainMenu menu = new MainMenu();
            menu.start(stage);
            stage.setScene(menu.getScene());
            stage.setTitle("Menu");
        });

        //Event listener for newGroupButton (creating a new group); creates new creategroup class with all its
        // buttons and what not, on button click.
        newGroupButton.setOnAction(event ->{
            CreateGroup creategroup = new CreateGroup(stage);
            stage.setScene(new Scene(creategroup.getRoot(),600,600));
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
