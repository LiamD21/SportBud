package View;

import Controller.SoloStatsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SoloStats {

    private final VBox root;
    private final SoloStatsHandler handler;

    public SoloStats(Stage stage, String eventID, String personID){
        handler = new SoloStatsHandler(eventID, personID);

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("back");
        AnchorPane anchorPane = new AnchorPane();

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // event listener for the back button
        backButton.setOnAction(event -> {
            SoloEventLeaderboard menu = new SoloEventLeaderboard(stage, eventID, personID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });

        // add elements to the root
        root.getChildren().addAll(anchorPane);
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot() {
        return root;
    }
}