package Controller;

import Model.Event;
import Model.Group;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class GeneralAddScoreHandler extends UIHandler{

    private final Person person;
    private final Group group;
    private final Event event;

    private final String username;

    public GeneralAddScoreHandler(String eventID, String ID){
        username = ID;
        super.setDb();
        try {
            // if its a person ID get that person from the DB via the string
            if(db.GetPerson(ID) != null){
                person = db.GetPerson(ID);
                group = null;
            }
            // if its a group ID get that group from the DB via the string
            else if(db.GetGroup(ID) != null){
                group =  db.GetGroup(ID);
                person = null;
            }
            //something is wrong here. Or Neither exist
            else{
                person = null;
                group = null;
            }

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
        //if there is a person assigned to the variable (it is a person and not a group)
        if (person != null) {
            for (Event event : person.getPersonalEvents()) {
                if (Objects.equals(event.getEventName(), eventID)) {
                    return event;
                }
            }
        }
        else{
            for (Event event : group.getGroupEvents()){
                if (Objects.equals(event.getEventName(), eventID)){
                    return event;
                }
            }

        }
        return null;
    }

    /**
     * Gets this event's type
     * @return the String representing the events type
     */
    public String getEventType(){
        return event.getEventType();
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
        // if the event is not a group event, use the add solo scores
        if (!event.getIsGroup()) {
            try {
                db.AddSoloScores(this.username, this.event.getEventName(), scores);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

        //else, if it is a group event, we need to add the int[] scores to the appropriate person
        else {

        }
    }
}