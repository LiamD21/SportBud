package View;

import Model.Person;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PersonSelectEvent {
    private final VBox root;
    private final String username;

    public PersonSelectEvent(Stage stage, String username){
        this.username = username;

        // create the root
        stage.setTitle(String.format("%s's Information", username));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot(){
        return this.root;
    }
}
