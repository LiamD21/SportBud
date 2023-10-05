package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create the menu pane and scene
        VBox menuRoot = new VBox();
        menuRoot.setAlignment(Pos.CENTER);
        Scene menuScene = new Scene(menuRoot, 500,500);

        // Create the title text
        Text menuTitle = new Text(180,150,"Main Menu");
        menuTitle.setFont(new Font(30));

        // Add title text to root
        menuRoot.getChildren().addAll(menuTitle);

        // Setup of the stage and then show it
        stage.setTitle("Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}