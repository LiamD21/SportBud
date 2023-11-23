package Controller;

import Model.Event;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SoloEventLbHandler extends UIHandler{
    private Event event;
    private Person person;
    public SoloEventLbHandler(String eventID, String personID){
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
     * Checks if the current event is a timed event
     * @return true if the current event is a timed event, false otherwise
     */
    public boolean isTimedEvent(){
        return Objects.equals(event.getEventType(), "Time-Highest") || Objects.equals(event.getEventType(), "Time-Lowest");
    }

    /**
     * takes an array of completed ready to display score strings and replaces the score part of each string with a time specific string
     * @param scores the array of strings containing the score and number of attempts
     * @return the updated array of scores with time formatting
     */
    private ArrayList<String> convertTime(ArrayList<String> scores){
        // create empty placeholder arrays
        ArrayList<String> onlyScore = new ArrayList<>(scores.size());
        ArrayList<String> onlyText = new ArrayList<>(scores.size());

        // split and extract each part of the original array
        for (int i = 0; i < scores.size(); i++){
            String secondsScore = "";
            String attemptsText = "";
            for (int j = 0; j < scores.get(i).length(); j++){
                if (scores.get(i).charAt(j) == ' '){
                    secondsScore = scores.get(i).substring(0, j);
                    for (int k = j+1; k < scores.get(i).length(); k++){
                        if (scores.get(i).charAt(k) != ' '){
                            attemptsText = scores.get(i).substring(k);
                            break;
                        }
                    }
                    break;
                }
            }

            onlyScore.add(i, secondsScore);
            onlyText.add(i, attemptsText);
        }

        // for each score, extract hours, minutes, and seconds and replace the array element with it
        for (int i = 0; i < onlyScore.size(); i++){
            int Hms = Integer.parseInt(onlyScore.get(i));
            int h = 0;
            int m = 0;

            // find hours
            while (Hms > 3600){
                Hms -= 3600;
                h ++;
            }

            // find minutes
            while (Hms > 60){
                Hms -= 60;
                m++;
            }

            // remaining amount is seconds
            int s = Hms;

            // create a new string to display the hours, minutes, and seconds
            String newDisplayStr = "";
            if (h > 0){
                newDisplayStr += String.format("%dhrs ", h);
            }
            if (m > 0){
                newDisplayStr += String.format("%dmins ", m);
            }
            if (s > 0){
                newDisplayStr += String.format("%dsec ", s);
            }

            // replace the current display string with the new one
            onlyScore.set(i, newDisplayStr);
        }

        // put the full strings back together then return
        for (int i = 0; i < onlyScore.size(); i++){
            onlyScore.set(i, onlyScore.get(i) + "      " + onlyText.get(i));
        }

        return onlyScore;
    }

    /**
     * Gets a sorted array list of scores from lowest to highest
     * @param hole an integer to see what hole to sort by. 0 means total, -1 means no input, so we also sort by total
     * @return the sorted array list of scores in this event
     */
    public ArrayList<String> getScores(int hole){
        if (hole == -1){
            hole = 0;
        }

        if (this.isTimedEvent()){
            return convertTime(sortScores(event.getScores(), hole));
        }
        return sortScores(event.getScores(), hole);
    }

    /**
     * sorts a given golf score array
     * @param scores the unsorted array list of scores
     * @param hole an integer to see what hole to sort by. 0 means total.
     * @return the sorted golf score array list
     */
    private ArrayList<String> sortScores(ArrayList<Score> scores, int hole){
        ArrayList<Integer> totalsPlaceholder = new ArrayList<>(scores.size());
        ArrayList<Integer> timesPlayedMirrorArr = new ArrayList<>(scores.size());
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
                timesPlayedMirrorArr.add(item.getEventCounter()+1);
            }
            else {
                boolean placed = false;
                // else, move up the array until you find the correct index to insert it at
                for (int j = 0; j < totalsPlaceholder.size(); j++) {
                    if (total <= totalsPlaceholder.get(j)) {
                        totalsPlaceholder.add(j, total);
                        timesPlayedMirrorArr.add(j, item.getEventCounter()+1);
                        placed = true;
                        break;
                    }
                }
                if (!placed){
                    totalsPlaceholder.add(totalsPlaceholder.size(), total);
                    timesPlayedMirrorArr.add(timesPlayedMirrorArr.size(), item.getEventCounter()+1);
                }
            }
        }

        // create arraylist of scores combined with attempt number
        ArrayList<String> strScores = new ArrayList<>(totalsPlaceholder.size());
        for (int i = 0; i < totalsPlaceholder.size(); i++){
            strScores.add(totalsPlaceholder.get(i).toString() + "      Attempt: " + timesPlayedMirrorArr.get(i).toString());
        }

        return strScores;
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
     * Checks if the current event contains any scores
     * @return true if the current event contains scores
     */
    public boolean hasScores(){
        return getScores(0).size() != 0;
    }

    /**
     * Checks if the event sorts its scores by highest first, or lowest first
     * @return true if highest score is best
     */
    public boolean getSortByHighest(){
        if (Objects.equals(getEventType(), "18")){
            return false;
        }
        else if (Objects.equals(getEventType(), "Back 9")){
            return false;
        }
        else if (Objects.equals(getEventType(), "Front 9")){
            return false;
        }
        else if (Objects.equals(getEventType(), "Points-Highest")){
            return true;
        }
        else if (Objects.equals(getEventType(), "Points-Lowest")){
            return false;
        }
        else if (Objects.equals(getEventType(), "Time-Highest")){
            return true;
        }
        else if (Objects.equals(getEventType(), "Time-Lowest")){
            return false;
        }
        return true;
    }
}
