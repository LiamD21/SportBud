package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestWindow {

   private final VBox root;

    public TestWindow(Stage stage) {
        root = new VBox();
        Button test = new Button("Test");
        root.getChildren().addAll(test);

        test.setOnAction(event -> {
            MainMenu change = new MainMenu();
            change.start(stage);
            stage.setScene(new Scene(change.getRoot(), 500, 500));
        });
    }

    public VBox getRoot(){
        return root;
    }
}
