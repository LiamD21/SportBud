package View;

import Controller.SoloEventStatsHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    }
}
