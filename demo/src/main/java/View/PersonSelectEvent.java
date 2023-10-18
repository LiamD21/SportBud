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
        stage.setTitle("Personal Events");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
    }
}
