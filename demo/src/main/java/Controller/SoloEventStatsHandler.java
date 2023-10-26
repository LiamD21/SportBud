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
    private Event getEvent(String eventID){
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
     * Gets this event's type
     * @return the String representing the events type
     */
    public String getEventType(){
        return event.getEventType();
    }

    /**
     * Gets a sorted array list of scores from lowest to highest
     * @param hole an integer to see what hole to sort by. 0 means total.
     * @return the sorted array list of scores in this event
     */
    public ArrayList<Integer> getScores(int hole){
        return sortScores(event.getScores(), hole);
    }

    /**
     * sorts a given golf score array
     * @param scores the unsorted array list of scores
     * @param hole an integer to see what hole to sort by. 0 means total.
     * @return the sorted golf score array list
     */
    private ArrayList<Integer> sortScores(ArrayList<Score> scores, int hole){
        ArrayList<Integer> totalsPlaceholder = new ArrayList<>(scores.size());
        for (int i = 0; i < scores.size(); i++){
            Score item = scores.get(i);
            int[] holes = item.getScores();
            int total;

            // find total score if we are looking for that
            if (hole == 0) {
                total = 0;
                for (int sc : holes) {
                    total += sc;
                }
            }

            // else, find single hole score
            else {
                total = holes[hole - 1];
            }

            // if this is the first item, just put it in the new array
            if (i == 0){
                totalsPlaceholder.add(total);
            }

            // else, move up the array until you find the correct index to insert it at
            for (int j = 0; j < totalsPlaceholder.size(); j++){
                if (total <= totalsPlaceholder.get(j)){
                    totalsPlaceholder.add(j, total);
                    break;
                }
            }
        }
        return totalsPlaceholder;
    }

    public int convertScoreView(String item){
        if (Objects.equals(item, "Total") || item == null){
            return 0;
        }
        else {
            return Integer.parseInt(item);
        }
    }
}
