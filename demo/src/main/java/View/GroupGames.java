package View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GroupGames {

   private final VBox root;

    /**
     * Constructor for the group games menu
     * @param stage The main stage that this scene will be displayed in
     */
    public GroupGames(Stage stage) {
        // create the root
        root = new VBox();
        root.setAlignment(Pos.CENTER);

        // Create elements
        Button test = new Button("Test");

        // Add to root
        root.getChildren().addAll(test);

        // Event listener for return to main menu
        test.setOnAction(event -> {
            MainMenu change = new MainMenu();
            change.start(stage);
            stage.setScene(new Scene(change.getRoot(), 500, 500));
            stage.setTitle("Group Games");
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
