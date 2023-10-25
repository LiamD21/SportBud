package View;

import Controller.SoloEventStatsHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class SoloEventStats {

    private final VBox root;
    private final String eventID;
    private final SoloEventStatsHandler handler;

    public SoloEventStats(Stage stage, String eventID, String personID){
        this.eventID = eventID;
        handler = new SoloEventStatsHandler(eventID, personID);

        // create the root
        stage.setTitle(String.format("%s Stats", handler.getEventName()));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("back");
        AnchorPane anchorPane = new AnchorPane();
        Text nameTitle = new Text(handler.getEventName());
        HBox simpleStats = new HBox();
        Text eventType = new Text();
        Text timesPlayed = new Text(String.format("This was played %d times!", handler.getTimesPlayed()));
        ListView<Integer> personalBests = new ListView<>();

        // add to listview

        // Modify Title text
        nameTitle.setFont(new Font(22));

        // Add elements to the HBox simple stats
        String type = handler.getEventType();
        if (Objects.equals(type, "18")){
            eventType.setText("This is an 18 hole golf event");
        }
        else if (Objects.equals(type, "Back 9")){
            eventType.setText("This is a golf event on the back 9");
        }
        else if (Objects.equals(type, "Front 9")){
            eventType.setText("This is a golf event on the front 9");
        }
        simpleStats.getChildren().addAll(eventType, timesPlayed);
        eventType.setFont(new Font(15));
        timesPlayed.setFont(new Font(15));
        simpleStats.setSpacing(30);
        simpleStats.setAlignment(Pos.CENTER);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add all children to root
        root.getChildren().addAll(anchorPane, nameTitle, simpleStats);

        // event listener for the back button
        backButton.setOnAction(event -> {
            PersonSelectEvent menu = new PersonSelectEvent(stage, personID);
            stage.setScene(new Scene(menu.getRoot(), 500, 500));
        });
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot() {
        return root;
    }
}
