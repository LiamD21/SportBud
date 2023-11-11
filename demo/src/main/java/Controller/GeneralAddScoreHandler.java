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
    private final String lastpage;

    private final String username;

    public GeneralAddScoreHandler(String eventID, String ID, String lastPage){
        username = ID;
        lastpage = lastPage;
        super.setDb();
        try {
            // if the last page was from the solo side, its a person and get from DB
            if (lastpage.equals("SoloEventStats")){
                person = db.GetPerson(username);
                group = null;

            }
            //if the last page was from the group side, its a group and get from DB
            else if (lastpage.equals("GroupEventStats")){
                group = db.GetGroup(username);
                person = null;
            }

            //something wrong occured. Both dont exist
            else{
                group = null;
                person = null;
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
        //how do we get the appropriate person??
//        else {
//            this.group.
//            db.AddGroupScores(this.username, this.event.getEventName(), );
//        }
    }
}