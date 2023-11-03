package View;

import Controller.GeneralAddScoreHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class GeneralAddScore {
    // TODO - IMPORTANT!!! If you are working on group stuff and this menu is missing options for event creation, let Liam know,
    // TODO - I will pass in any parameters needed to setup this page in a way that all relevant information is displayed and available

    private final VBox root;

    private final String eventID;

    private final String personID;

    private final GeneralAddScoreHandler handler;

    private final String lastPage;

    public GeneralAddScore(Stage stage, String lastPage, String person, String eventName){
        handler = new GeneralAddScoreHandler(eventName, person);
        eventID = eventName;
        personID = person;
        this.lastPage = lastPage;

        // create the root
        stage.setTitle(String.format("Add Your %s Score", eventID));
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        // create elements
        Button backButton = new Button("Back");
        AnchorPane anchorPane = new AnchorPane();
        VBox scoreEntryBox = new VBox();

        // create score entering elements
        Label label;
        if (Objects.equals(handler.getEventType(), "Front 9") || Objects.equals(handler.getEventType(), "Back 9") || Objects.equals(handler.getEventType(), "18")){
            label = new Label("Enter your scores, \neach hole separated by a comma");
        }
        else{
            label = new Label("Enter your new score");
        }
        TextField scoreIn = new TextField("Enter Scores Here");
        Button submit = new Button("Submit");
        scoreEntryBox.getChildren().addAll(label, scoreIn, submit);

        // Anchor back button to the top corner of the screen
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 5.0);
        anchorPane.getChildren().addAll(backButton);

        // add children to the root
        root.getChildren().addAll(anchorPane, scoreEntryBox);

        // event listener for the back button
        backButton.setOnAction(event -> {
            if (Objects.equals(lastPage, "SoloEventStats")) {
                SoloEventStats menu = new SoloEventStats(stage, eventID, personID);
                stage.setScene(new Scene(menu.getRoot(), 500, 500));
            }
        });

        // TODO Liam - Add input filtering based on if it has the correct number of inputs for the event type
        // event listener for the add scores button
        submit.setOnAction(event -> {
            if (!scoreIn.getCharacters().toString().equals("Enter Scores Here")){
                if (Objects.equals(handler.getEventType(), "Front 9") || Objects.equals(handler.getEventType(), "Back 9") || Objects.equals(handler.getEventType(), "18")) {
                    String[] scores = scoreIn.getCharacters().toString().split(",");
                    int[] intScores = new int[scores.length];
                    for (int i = 0; i < scores.length; i++){
                        if (scores[i].charAt(0) == ' '){
                            scores[i] = scores[i].substring(1);
                        }
                        intScores[i] = Integer.parseInt(scores[i]);
                    }
                    handler.setScore(intScores);
                }
            }
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