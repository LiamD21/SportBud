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

import java.util.Objects;

public class MainMenu extends Application {

    private Scene menuScene;

    @Override
    public void start(Stage stage) {
        // Create the menu pane and scene
        VBox menuRoot = new VBox();
        menuRoot.setAlignment(Pos.CENTER);
        menuScene = new Scene(menuRoot, 500,500);

        // Create elements
        Text menuTitle = new Text(180,150,"Sport Bud");
        menuTitle.setFont(new Font(30));
        Button startButton = new Button("GO!");
        ChoiceBox<String> soloGroup = new ChoiceBox<>();
        soloGroup.getItems().addAll("Solo", "Group");

        // Add spacing
        menuRoot.setSpacing(30);

        // Event Listening for going to group games screen or solo games screen
        startButton.setOnAction(event -> {
            if (Objects.equals(soloGroup.getValue(), "Group")) {
                GroupGames nextMenu = new GroupGames(stage);
                stage.setScene(new Scene(nextMenu.getRoot(), 500, 500));
            }
            else if (Objects.equals(soloGroup.getValue(), "Solo")) {
                SoloGames nextMenu = new SoloGames(stage);
                stage.setScene(new Scene(nextMenu.getRoot(), 500, 500));
            }
        });

        // Add to root
        menuRoot.getChildren().addAll(menuTitle, soloGroup, startButton);

        // Setup of the stage and then show it
        stage.setTitle("Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public Scene getScene(){
        return menuScene;
    }

    public static void main(String[] args) {
        launch();
    }
}