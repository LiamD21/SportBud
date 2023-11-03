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

    private Score score;

    public GeneralAddScoreHandler(String eventID, String personID){
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
        this.score = new Score(this.getEventType(), this.event.getEventName(), this.event.getScores().size() + 1);
        this.score.inputScore(scores);
        this.event.inputScores(score);
    }
}