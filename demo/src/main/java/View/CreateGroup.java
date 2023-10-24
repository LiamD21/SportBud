package View;

import Controller.CreateGroupHandler;
import Model.Group;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CreateGroup {
    private final CreateGroupHandler handler;
    private final VBox root;
    private final String[] people;

    public CreateGroup(Stage stage){

        stage.setTitle("Create a Group");
        handler = new CreateGroupHandler();
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
        members.setMaxSize(300,300);
        members.setPrefSize(150,150);

        //text inputs:
        TextField groupname = new TextField("Set group name here");
        groupname.setPrefColumnCount(20);
        groupname.setMaxWidth(300);
        groupname.setMinWidth(20);
        groupname.setPadding(new Insets(8,8,8,8));

        ListView<String> availableMembers = new ListView<>();
        people = this.getPeople();
        for (String person : people){
            availableMembers.getItems().add(person);
        }
//        availableMembers.getItems().addAll(this.getPeople());
        availableMembers.setPrefSize(200,200);
        availableMembers.setMaxSize(300,300);

        //setting margins for the text boxes so they arent so close together
        root.setMargin(groupname, new Insets(5));
        root.setMargin(availableMembers, new Insets(5));

        // Anchoring buttons
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton,10.0);

        AnchorPane.setBottomAnchor(createGroup, 10.0);
        AnchorPane.setRightAnchor(createGroup, 10.0);
        anchorPane.getChildren().addAll(backButton, createGroup);


        root.getChildren().addAll(createGroupTitle, groupname, availableMembers,
                addPerson, removePerson,members, anchorPane);


        // Event listener for return to GroupGames menu
        backButton.setOnAction(event -> {
            GroupGames groupgames = new GroupGames(stage);
            stage.setScene(new Scene(groupgames.getRoot(), 500, 500));
            stage.setTitle("Group Events");
        });

        //Event listener for the Create Button, send the list members to the handler
        createGroup.setOnAction(event -> {
            if (!members.getItems().isEmpty()) {
                int createdSuccess;
                try {
                    createdSuccess = handler.handleCreatedGroup(members.getItems(), groupname.getText());

                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }

                if (createdSuccess == 1) {
                    Alert a = new Alert(Alert.AlertType.NONE,"Created Group Successfully!", new ButtonType("OK",
                            ButtonBar.ButtonData.OK_DONE));
                    a.setContentText("Created Group successfully!");
                    a.show();
                }
            }
            else {

                Alert a  = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Need to input a name for your group");
                a.show();
            }
        });

        //Event Listener for the add/ remove button (if we have a remove button)
        // This is JUST adding person from the list of persons in the database, to their respective group panel
        // which is shown in the bottom listview
        addPerson.setOnAction(event ->{
            if (availableMembers.getSelectionModel().getSelectedItem()!=null){
                //if the selected value of the persons list is not in the members list. Add it in to the members list
                if (!members.getItems().contains(availableMembers.getSelectionModel().getSelectedItem())) {
                    members.getItems().add(availableMembers.getSelectionModel().getSelectedItem());
                }
            }
        });

        // when the remove person buttonis clicked, it checks the selected value in the members list view, and
        // removes the selected person from the group list
        removePerson.setOnAction(event ->{
            if (members.getSelectionModel().getSelectedItem()!= null){
                members.getItems().remove(members.getSelectionModel().getSelectedIndex());
            }
        });

    }
    public String[] getPeople(){
        return handler.getPersonList();
    }
    public VBox getRoot(){
        return root;
    }


}
