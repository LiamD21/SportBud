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

    /**
     * gets the event object that matches the event name passed in
     * @param eventID the String event name to find object of
     * @return the object that relates to the string passed in
     */
    public Event getEvent(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }

    /**
     * Gets the string name of the current event
     * @return the name of the current event
     */
    public String getEventName(){
        return event.getEventName();
    }
}
