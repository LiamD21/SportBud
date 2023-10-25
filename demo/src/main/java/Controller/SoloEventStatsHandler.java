package Controller;

import Model.Event;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * Gets the number of times this event has been played
     * @return the integer number of plays that this event has
     */
    public int getTimesPlayed(){
        return event.getScores().size();
    }

    /**
     * Gets this event's type
     * @return the String representing the events type
     */
    public String getEventType(){
        return event.getEventType();
    }

    /**
     * Gets a sorted array list of scores from lowest to highest
     * @return the sorted array list of scores in this event
     */
    public ArrayList<Score> getScores(){
        return sortScores(event.getScores());
    }

    /**
     * sorts a given golf score array
     * @return the sorted golf score array list
     */
    private ArrayList<Score> sortScores(ArrayList<Score> scores){
        ArrayList<Score> resultingScores = new ArrayList<>(scores.size());
        ArrayList<Integer> totalsPlaceholder = new ArrayList<>(scores.size());
        for (int i = 0; i < scores.size(); i++){
            Score item = scores.get(i);
            int[] holes = item.getScores();
            int total = 0;
            for (int hole : holes) {
                total += hole;
            }

            // if this is the first item, just put it in the new array
            if (i == 0){
                resultingScores.add(item);
                totalsPlaceholder.add(total);
            }

            // else, move up the array until you find the correct index to insert it at
            for (int j = 0; j < resultingScores.size(); j++){
                if (total <= totalsPlaceholder.get(j)){
                    resultingScores.add(j, item);
                    totalsPlaceholder.add(j, total);
                    break;
                }
            }
        }
        return resultingScores;
    }
}
