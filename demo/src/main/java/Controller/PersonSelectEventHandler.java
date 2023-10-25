package Controller;

import Model.Event;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

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

    /**
     * Gets the arraylist of the person's personal events names
     * @return array list of the persons personal events names
     */
    public ArrayList<String> getSoloEvents() {
        ArrayList<Event> events = person.getPersonalEvents();
        ArrayList<String> eventNames = new ArrayList<>(events.size());
        for (Event event : events) {
            if (!event.getIsGroup()) {
                eventNames.add(event.getEventName());
            }
        }
        eventNames.trimToSize();
        return eventNames;
    }
}
