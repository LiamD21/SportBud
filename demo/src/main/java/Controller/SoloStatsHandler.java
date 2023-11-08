package Controller;

import Model.Event;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SoloStatsHandler extends UIHandler{

    private Event event;
    private Person person;
    public SoloStatsHandler(String eventID, String personID){
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
     * gets the average score of an event at the given hole or total
     * @param hole the integer which specifies a total score, 0, or a specific hole
     * @return a double representing the average score for this event
     */
    public int getAverage(int hole){
        ArrayList<Integer> scores = getScores(hole);
        double average = 0;

        for (int score:scores){
            average += score;
        }

        return (int) Math.round(average/ scores.size());
    }

    /**
     * gets the scores for this event, will get any of either total or a specific golf hole
     * @param hole the integer which specifies a total score, 0, or a specific hole
     * @return an array list of integers for the requested score each time played
     */
    private ArrayList<Integer> getScores(int hole){
        ArrayList<Score> scores = event.getScores();
        ArrayList<Integer> scoreTotals = new ArrayList<>(scores.size());

        if (hole == -1){
            hole = 0;
        }

        // go through all scores and add them up depending on what the hole is, total or specific
        for (int i = 0; i < scores.size(); i++) {
            Score oneGame = scores.get(i);
            int[] holes = oneGame.getScores();
            int tot;

            if (hole == 0) {
                tot = 0;
                for (int sc : holes) {
                    tot += sc;
                }
            }
            else {
                tot = holes[hole - 1];
            }

            // add the score to the array in order
            scoreTotals.add(i, tot);
        }
        return scoreTotals;
    }

    /**
     * converts the string score view to an integer to represent what to sort by
     * @param item the string value of what to sort by
     * @return the integer to use for sorting, 0 for overall/total, -1 for no selection
     */
    public int convertScoreView(String item){
        if (Objects.equals(item, "Total")){
            return 0;
        }
        else if (item == null){
            return -1;
        }
        else {
            return Integer.parseInt(item);
        }
    }

    /**
     * Checks if the current event is a golf event
     * @return a boolean true if it is a golf event
     */
    public boolean isGolfEvent(){
        return event.isGolf();
    }

    /**
     * Gets the data needed to populate a chart
     * @param hole the integer which specifies a total score, 0, or a specific hole
     * @return an arraylist of integers which represent scores to populate the chart with
     */
    public ArrayList<Integer> getChartData(int hole){
        return getScores(hole);
    }
}