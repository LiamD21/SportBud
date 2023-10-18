package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class CreateGroup {
    private final VBox root;

    public CreateGroup(Stage stage){
        stage.setTitle("Create a Group");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10,10,10,10));


        //modify title
        Text createGroupTitle = new Text("Create Your Group");
        createGroupTitle.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));

        //Buttons:
        Button backButton = new Button("Back to Groups");
        Button createGroup = new Button("Create!");
        Button addPerson = new Button("Add Person");
        Button removePerson = new Button("Remove Person");

        //button modifications:
        addPerson.setPadding(new Insets(5));
        removePerson.setPadding(new Insets(5));

        //ListView (members in the group to create)
        //takes ObservableList<String> variable: aka ArrayList of names?
        ListView<String> members = new ListView<String>();
        members.setPadding(new Insets(30));
        members.setPrefSize(300,200);



        //text inputs:
        TextField groupname = new TextField("Set group name here");
        groupname.setPrefColumnCount(20);
        groupname.setMaxWidth(300);
        groupname.setMinWidth(20);
        groupname.setPadding(new Insets(8,8,8,8));

        TextField enterGroupMember = new TextField("group members name to add");
        enterGroupMember.setPrefColumnCount(20);
        enterGroupMember.setMaxWidth(300);
        enterGroupMember.setMinWidth(20);
        enterGroupMember.setPadding(new Insets(8,8,8,8));


        //setting margins for the text boxes so they arent so close together
        root.setMargin(groupname, new Insets(5));
        root.setMargin(enterGroupMember, new Insets(5));

        // Anchoring buttons
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton,10.0);

        AnchorPane.setBottomAnchor(createGroup, 10.0);
        AnchorPane.setRightAnchor(createGroup, 10.0);
        anchorPane.getChildren().addAll(backButton, createGroup);


        root.getChildren().addAll(createGroupTitle, groupname, enterGroupMember,
                addPerson, removePerson,members, anchorPane);

        // Event listener for return to main menu
        backButton.setOnAction(event -> {
            GroupGames groupgames = new GroupGames(stage);
            stage.setScene(new Scene(groupgames.getRoot(), 500, 500));
            stage.setTitle("Group Events");
        });

        //Event listener for the Create Button

        //Event Listener for the add/ remove button (if we have a remove button)

        //Need to somehow implement ability to remove a member from the group to create...

    }
    public VBox getRoot(){
        return root;
    }


}
