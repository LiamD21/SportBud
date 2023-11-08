package View;

import Controller.GroupGamesHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GroupGames {
    private final GroupGamesHandler handler;

   private final VBox root;
   private final String[] groups;

    /**
     * Constructor for the group games menu
     * @param stage The main stage that this scene will be displayed in
     */
    public GroupGames(Stage stage) {
        // create the root
        stage.setTitle("Group Events");
        handler = new GroupGamesHandler();
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(30);

        // Create elements
        Button backButton = new Button("Back");
        Text titleText = new Text("Groups: Choose a group");
        ChoiceBox<String> groupChoice = new ChoiceBox<>();
        Button newGroupButton = new Button("Create a New Group");


        // Modify Title text
        titleText.setFont(new Font(22));

        // add all groups as choices in the choice box
        groups = this.getGroups();
        for(String group: groups){
            groupChoice.getItems().add(group);
        }


        // Anchor Buttons
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(backButton, 15.0);
        AnchorPane.setLeftAnchor(backButton, 25.0);

        anchorPane.getChildren().addAll(backButton);

        // Add to root
        root.getChildren().addAll(anchorPane, titleText, groupChoice, newGroupButton);

        // Event listener for return to main menu
        backButton.setOnAction(event -> {
            MainMenu menu = new MainMenu();
            menu.start(stage);
            stage.setScene(menu.getScene());
            stage.setTitle("Menu");
        });

        //Event listener for newGroupButton (creating a new group); creates new creategroup class with all its
        // buttons and what not, on button click.
        newGroupButton.setOnAction(event ->{
            CreateGroup creategroup = new CreateGroup(stage);
            stage.setScene(new Scene(creategroup.getRoot(),600,600));
        });


        //event listener to listen for group chosen.
        groupChoice.setOnAction(event ->{
            //TODO Implement the group event viewer when chosen selection
            GroupSelectEvent groupSelectEvent = new GroupSelectEvent(stage,
                    groupChoice.getSelectionModel().getSelectedItem());
            stage.setScene(new Scene(groupSelectEvent.getRoot(), 500,500));
        });
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot(){
        return root;
    }

    public String[] getGroups() {
        return handler.getGroupList();
    }
}
