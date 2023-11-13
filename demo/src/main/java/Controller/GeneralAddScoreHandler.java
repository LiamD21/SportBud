package Controller;

import Model.Event;
import Model.Group;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class GeneralAddScoreHandler extends UIHandler{

    private final Person person;
    private final Group group;
    private final Event event;
    private final String lastpage;
    private final String groupID;
    private final String personID;

    public GeneralAddScoreHandler(String eventID, String personID, String lastPage, String groupID){
        lastpage = lastPage;
        super.setDb();
        try {
            // if the last page was from the solo side, its a person and get from DB
            if (lastpage.equals("SoloEventStats")){
                person = db.GetPerson(personID);
                group = null;
                this.personID = personID;
                this.groupID = null;
            }
            //if the last page was from the group side, its a group and get from DB
            else if (lastpage.equals("GroupEventStats")){
                group = db.GetGroup(groupID);
                person = null;
                this.personID = null;
                this.groupID = groupID;
            }

            //something wrong occured. Both dont exist
            else{
                group = null;
                person = null;
                this.personID = null;
                this.groupID = null;
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
    public void setScore(int[] scores, String person){
        // if the event is not a group event, use the add solo scores
        if (!Objects.equals(lastpage, "GroupEventStats")) {
            try {
                db.AddSoloScores(this.personID, this.event.getEventName(), scores);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

        else {
            try {
                db.AddGroupScores(this.groupID, this.event.getEventName(), scores, person);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * If the event is a group event, get the array of people in the group
     * @return the array list of strings of people in the event
     */
    public ArrayList<String> getPeople(){
        ArrayList<String> people = new ArrayList<>();
        if (groupID != null){
            people.addAll(this.group.getPeople());
        }
        return people;
    }
}