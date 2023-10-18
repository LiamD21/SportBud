package Controller;

import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SoloGamesHandler extends UIHandler{
    public SoloGamesHandler() {
        super.setDb();
    }

    /**
     * Used by the solo games class to get a string list of all usernames in the system
     * @return String list of all usernames in the system
     */
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
    public void handleNewPerson(String newName, String userName){
        Person person = new Person(newName);
        //db.AddPerson(userName, person);
        // TODO Liam will update this once the add person is done
    }
}