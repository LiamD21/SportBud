package View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
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
//        Text menuTitle = new Text(180,150,"Sport Bud");
//        menuTitle.setFont(new Font(30));
        ImageView imageView =  new ImageView();
        imageView.setImage(new Image(System.getProperty("user.dir") + "/demo/visuals/blacklogo.png"));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setFitWidth(400);
        imageView.setVisible(true);

        Text chooseyourmenu = new Text("Choose Your Menu!");
        chooseyourmenu.setFont(new Font(16));


        //holds the buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(12);

        //the buttons
        Button soloButton = new Button("SOLO");
        Button groupButton = new Button("GROUP");

        // add buttons into the box
        buttons.getChildren().addAll(soloButton,groupButton);

        // Add spacing
        menuRoot.setSpacing(30);



        // Event Listening for going to group games screen or solo games screen
        soloButton.setOnAction(event -> {
                SoloGames nextMenu = new SoloGames(stage);
                stage.setScene(new Scene(nextMenu.getRoot(), 500, 500));
        });

        groupButton.setOnAction(event -> {
            GroupGames nextMenu = new GroupGames(stage);
            stage.setScene(new Scene(nextMenu.getRoot(), 500, 500));

        });

        // Add to root
        menuRoot.getChildren().addAll(imageView, chooseyourmenu, buttons);

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