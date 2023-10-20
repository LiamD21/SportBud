package Controller;

import Model.Person;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class CreateSoloEventHandler extends UIHandler{
    Person person;
    public CreateSoloEventHandler(String username){
        super.setDb();
        try {
            person = db.GetPerson(username);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
