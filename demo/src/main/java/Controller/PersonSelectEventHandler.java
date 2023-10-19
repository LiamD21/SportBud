package Controller;

import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PersonSelectEventHandler extends UIHandler{
    Person person;
    public PersonSelectEventHandler(String username) {
        super.setDb();
        try {
            person = db.GetPerson(username);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the person's full name from a username request from the view
     * @return the String full name of the person being referenced by the username
     */
    public String getName(){
        return person.getName();
    }
}
