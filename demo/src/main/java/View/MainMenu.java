package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MainMenu extends Application {

    private Scene menuScene;

    @Override
    public void start(Stage stage) {
        // setting icon
        stage.getIcons().add(new Image("icon.png"));

        //help button
        AnchorPane ap = new AnchorPane();
        Button helpButton = new Button(" ? ");
        helpButton.setId("boldButton");
        ap.getChildren().add(helpButton);
        AnchorPane.setTopAnchor(helpButton, 10.0);
        AnchorPane.setRightAnchor(helpButton, 15.0);

        helpButton.setOnAction(event -> {
            Popup p = new Popup();
            p.show(stage);
            // TODO add popup help window ISAAC DO THIS NEXT
        });

        // Create the menu pane and scene
        VBox menuRoot = new VBox();
        menuRoot.setAlignment(Pos.CENTER);
        menuScene = new Scene(menuRoot, 800,600);
        menuScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Create elements
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("logorecolour.png"));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setFitWidth(400);
        imageView.setVisible(true);

        Text chooseYourMenu = new Text("Choose Your Menu!");

        //holds the buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
//        buttons.setSpacing(12);

        //the buttons
        Button soloButton = new Button("SOLO");

        Button groupButton = new Button("GROUP");

        // add buttons into the box
        buttons.getChildren().addAll(soloButton,groupButton);

        // Add spacing
//        menuRoot.setSpacing(30);

        // Event Listening for going to group games screen or solo games screen
        soloButton.setOnAction(event -> {
                SoloGames nextMenu = new SoloGames(stage);
                stage.setScene(new Scene(nextMenu.getRoot(), 800, 600));
        });

        groupButton.setOnAction(event -> {
            GroupGames nextMenu = new GroupGames(stage);
            stage.setScene(new Scene(nextMenu.getRoot(), 800, 600));

        });

        // Add to root
        menuRoot.getChildren().addAll(ap, imageView, chooseYourMenu, buttons);

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