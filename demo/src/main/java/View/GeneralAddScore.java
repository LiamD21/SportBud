package View;

import Controller.GeneralAddScoreHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GeneralAddScore {

    private final VBox root;

    private final GeneralAddScoreHandler handler;

    public GeneralAddScore(Stage stage){
        handler = new GeneralAddScoreHandler();

        // create the root
        stage.setTitle("Add Your Event Score");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot() {
        return root;
    }
}
