package Controller;

import Model.Event;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class GeneralAddScoreHandler extends UIHandler{

    private final Person person;
    private final Event event;

    private final String username;

    public GeneralAddScoreHandler(String eventID, String personID){
        username = personID;
        super.setDb();
        try {
            person = db.GetPerson(personID);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        event = getEvent(eventID);
    }

    /**
     * Finds the event corresponding to a given string
     * @param eventID the string event id corresponding to the event we wish to check for
     * @return null if the string does not correspond to an existing event, else returns the event
     */
    private Event getEvent(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }

    /**
     * Checks if the current event is a golf event
     * @return a boolean true if it is a golf event
     */
    public boolean isGolfEvent(){
        return event.isGolf();
    }

    /**
     * adds a new score to this event
     * @param scores an integer array containing all the scores for this event
     */
    public void setScore(int[] scores){
        if (!event.getIsGroup()) {
            try {
                db.AddSoloScores(this.username, this.event.getEventName(), scores);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}