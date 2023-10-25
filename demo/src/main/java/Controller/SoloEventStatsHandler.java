package Controller;

import Model.Event;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class SoloEventStatsHandler extends UIHandler{
    Event event;
    Person person;
    public SoloEventStatsHandler(String eventID, String personID){
        super.setDb();
        try {
            person = db.GetPerson(personID);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        event = getEvent(eventID);
    }

    public Event getEvent(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }

    public String getEventName(){
        return event.getEventName();
    }
}
