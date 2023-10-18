package Controller;

import Model.Database;
import javafx.event.ActionEvent;

public class SoloGamesHandler {

    /*
    Reference to the database
     */
    private Database db;

    public SoloGamesHandler() {
        this.setDb();
    }

    /**
     * When the system is initialized, and we enter the solo games page, this is called to get the reference to the database
     */
    public void setDb() {
        try {
            this.db = new Database();
        }
        catch (Exception e){
            System.out.println("Error in creating Database");
        }
    }

    /**
     * Takes a button click event and a string and will create a new person with the given name in the database
     * @param click the action on the add person button
     * @param newName the string from the text field to add person
     */
    public void handleNewPerson(ActionEvent click, String newName){

    }
}