package Controller;

import Model.Event;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class CompareScoresHandler extends UIHandler {

    Person person;
    String eventType;

    public CompareScoresHandler(String eventID, String personID){
        super.setDb();
        try {
            person = db.GetPerson(personID);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        setEventType(eventID);
    }

    /**
     * Gets the event type for the given event
     * @param eventID
     */
    private void setEventType(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                eventType = event.getEventType();
            }
        }
    }
}
