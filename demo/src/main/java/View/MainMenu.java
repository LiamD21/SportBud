package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {

    private VBox menuRoot;
    @Override
    public void start(Stage stage) {
        // Create the menu pane and scene
        menuRoot = new VBox();
        menuRoot.setAlignment(Pos.CENTER);
        Scene menuScene = new Scene(menuRoot, 500,500);

        // Create elements
        Text menuTitle = new Text(180,150,"Sport Bud");
        menuTitle.setFont(new Font(30));

        Button startButton = new Button("GO!");

        ChoiceBox<String> soloGroup = new ChoiceBox<>();
        soloGroup.getItems().addAll("Solo", "Group");

        // Add spacing
        menuRoot.setSpacing(30);

        // Button Event Listening
        startButton.setOnAction(event -> {
            TestWindow change = new TestWindow(stage);
            stage.setScene(new Scene(change.getRoot(), 500, 500));
        });

        // Add to root
        menuRoot.getChildren().addAll(menuTitle, soloGroup, startButton);

        // Setup of the stage and then show it
        stage.setTitle("Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    public VBox getRoot(){
        return menuRoot;
    }

    public static void main(String[] args) {
        launch();
    }
}