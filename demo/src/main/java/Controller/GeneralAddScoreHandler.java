package Controller;

import Model.Event;
import Model.Person;
import Model.Score;
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

    private Event getEvent(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }

    public String getEventType(){
        return event.getEventType();
    }

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