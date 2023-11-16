package View;

import Controller.SoloGamesHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SoloGames {

    private final VBox root;
    private final SoloGamesHandler handler;
    private String[] people;
    private ChoiceBox<String> personChooser;

    /**
     * Constructor for the group games menu
     * @param stage The main stage that this scene will be displayed in
     */
    public SoloGames(Stage stage) {
        // create the root
        stage.setTitle("Solo Events");
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(30);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Initialize the handler
        handler = new SoloGamesHandler();

        // Create elements
        AnchorPane anchorPane = new AnchorPane();
        Button backButton = new Button("🠔");
        Text titleText = new Text("Solo Events");
        HBox newPerson = new HBox();
        HBox selectPerson = new HBox();

        // Modify Title text
        titleText.setFont(new Font(22));

        // create items for select person and add them to the HBox
        Button selectButton = new Button("Select Person");
        setPersonChoice(createPersonDisplay());
        selectPerson.getChildren().addAll(personChooser, selectButton);
        selectPerson.setSpacing(10);
        selectPerson.setAlignment(Pos.CENTER);

        // create these for new person and add them to the Hbox
        TextField newName = new TextField("Full Name");
        TextField userName = new TextField("Username");
        Button addPersonButton = new Button("Add Person");
        newPerson.getChildren().addAll(newName, userName, addPersonButton);
        newPerson.setSpacing(10);
        newPerson.setAlignment(Pos.CENTER);

        // Anchor Back Button
        AnchorPane.setTopAnchor(backButton, 10.0);
        AnchorPane.setLeftAnchor(backButton, 15.0);

        anchorPane.getChildren().addAll(backButton);

        // Add elements to root
        root.getChildren().addAll(anchorPane, titleText, selectPerson, newPerson);

        // Event listener for return to main menu
        backButton.setOnAction(event -> {
            MainMenu menu = new MainMenu();
            menu.start(stage);
            stage.setScene(menu.getScene());
            stage.setTitle("Menu");
        });

        // If add person button clicked, pass to the controller
        // New person is only created if the user actually enters a name, else there is just an error popup
        addPersonButton.setOnAction(event -> {
            if (!newName.getCharacters().toString().equals("Full Name") && !userName.getCharacters().toString().equals("Username")){
                handler.handleNewPerson(newName.getCharacters().toString(), userName.getCharacters().toString());

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setContentText(String.format("New Person %s Created successfully with username %s", newName.getCharacters().toString(), userName.getCharacters().toString()));
                confirmAlert.show();

                selectPerson.getChildren().removeAll(personChooser, selectButton);
                setPersonChoice(createPersonDisplay());
                selectPerson.getChildren().addAll(personChooser, selectButton);
            }
            else {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setContentText("Error: A Name and Username must be entered to create a new person");
                failAlert.show();
            }
        });

        // If select button clicked, pass to the controller
        // Person is only selected if the user actually selects an option, else, show error popup
        selectButton.setOnAction(event -> {
            if (personChooser.getValue() != null){
                SoloPersonSelectEvent menu = new SoloPersonSelectEvent(stage, personChooser.getValue());
                stage.setScene(new Scene(menu.getRoot(), 800, 600));
            }
            else {
                Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                invalidAlert.setContentText("Error: Select a username before confirming your selection");
                invalidAlert.show();
            }
        });
    }

    /**
     * Getter method for the root for swapping scenes in the same stage
     * @return the VBox root of this scene
     */
    public VBox getRoot(){
        return root;
    }

    /**
     * gets the list of people from the handler
     * @return a string list of people's usernames
     */
    public String[] getPeople(){
        return handler.getPersonList();
    }

    /**
     * goes through all the people's usernames and adds them to a choice box
     * @return a choice box containing all the people's usernames
     */
    private ChoiceBox<String> createPersonDisplay(){
        people = this.getPeople();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        for (String person : people){
            choiceBox.getItems().add(person);
        }
        return choiceBox;
    }

    /**
     * Takes in a choice box and sets it to the main page choice box
     * @param choiceBox a choice box object
     */
    private void setPersonChoice(ChoiceBox<String> choiceBox){
        personChooser = choiceBox;
    }
}