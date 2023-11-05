package Controller;

import Model.Person;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class CreateSoloEventHandler extends UIHandler{
    private Person person;
    private String username;
    public CreateSoloEventHandler(String username){
        super.setDb();
        try {
            person = db.GetPerson(username);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        this.username = username;
    }

    public void createEvent(String eventName, String eventType){
        try {
            db.AddSoloEvent(username, eventName, eventType);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
