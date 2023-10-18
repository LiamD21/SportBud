package Controller;

import Model.Database;
import Model.Person;
import javafx.event.ActionEvent;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

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
        } catch (FileNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getPersonList(){
        try {
            return db.GetPeople();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes a button click event and a string and will create a new person with the given name in the database
     * @param newName the string from the text field to add person
     */
    public void handleNewPerson(String newName){
        Person person = new Person(newName);
        db.AddPerson(person);
        // TODO we might have some problems with this because the person class does not have a username attached?
    }
}