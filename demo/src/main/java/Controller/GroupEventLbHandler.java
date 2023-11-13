package Controller;

import Model.Event;
import Model.Group;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GroupEventLbHandler extends UIHandler{
    private Event event;
    private Group group;

    public GroupEventLbHandler(String eventID, String groupID){
        super.setDb();
        try {
            group = db.GetGroup(groupID);
        } catch (ParseException | IOException e){
            throw new RuntimeException(e);
        }
        event = getEvent(eventID);
    }



    /**
     * gets the event object that matches the event name passed in
     * @param eventID string of event name
     * @return event object
     */
    private Event getEvent(String eventID){
        for (Event event : group.getGroupEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }

    /**get string of event name
     * @return String of the name of the event
     */
    public String getEventName(){
        return event.getEventName();
    }


    /**Gets the events type
     * @return  String representing events type
     */
    public String getEventType() {
        return event.getEventType();
    }


    /**
     * Gets a sorted array list of scores from lowest to highest
     * @param hole an integet to see what hole to sort by. 0 means total, -1 means
     *             no input, so we also sort by total.
     * @return the sorted array list of scores in this event
     */
    public ArrayList<String> getScores(int hole) throws ParseException, IOException {
        if (hole  ==-1){
            hole =0;
        }
        return sortScores(event.getScores(),hole);
    }


    private ArrayList<String> sortScores(ArrayList<Score> scores, int hole) throws ParseException, IOException {
        //array to hold the scores /totals
        ArrayList<Integer> totalsPlaceholder = new ArrayList<>(scores.size());
        // a mirrored index array, that has the name of the person whom did the score
        // each index corresponds to the indices in totalsPlaceholder...
        ArrayList<String> peoplesScoresMirrorArr = new ArrayList<>(scores.size());

        for (int i = 0; i < scores.size(); i++){
            Score item = scores.get(i);
            int[] holes = item.getScores();
            String personForScore = item.getPersonsName();
            int total;

            // find total, if that is the specified hole to search for (aka hole = 0)
            if (hole == 0){
                total = 0;
                //for each score in the hole, add it up
                for (int score : holes){
                    total += score;
                }
            }
            //else, find the specified holes score
            else{
                total = holes[hole-1];
            }

            //if this is the first item, just put it in the new array
            if(i == 0){
                totalsPlaceholder.add(total);
                //add the person in the group... somehow? There is no way to actively
                // obtain the person that the event pertains to via the group.
                peoplesScoresMirrorArr.add(personForScore);

            }

            else {
                boolean placed = false;
                //else, move up the array until you find the correct index to
                //insert it at
                for (int j = 0; j < totalsPlaceholder.size(); j++) {
                    if (total <= totalsPlaceholder.get(j)) {
                        totalsPlaceholder.add(j, total);
                        peoplesScoresMirrorArr.add(j, personForScore);
                        placed = true;
                        break;
                    }
                }
                if (!placed){
                    totalsPlaceholder.add(totalsPlaceholder.size(), total);
                    peoplesScoresMirrorArr.add(peoplesScoresMirrorArr.size(), personForScore);
                }
            }
        }

        // check and remove scores from people who have more than one
        // keep their highest score
        ArrayList<Integer> bests = new ArrayList<>(group.getGroupSize());
        ArrayList<String> seen = new ArrayList<>(group.getGroupSize());
        for (int i = 0; i < totalsPlaceholder.size(); i++){
            if (!seen.contains(peoplesScoresMirrorArr.get(i))){
                seen.add(peoplesScoresMirrorArr.get(i));
                bests.add(totalsPlaceholder.get(i));
            }
        }

        // create arraylist of scores combined with attempt number
        ArrayList<String> strScores = new ArrayList<>(bests.size());
        for (int i = 0; i < bests.size(); i++){
            strScores.add(bests.get(i).toString() + "      Scored by: "
                    + seen.get(i));
        }

        return strScores;
    }

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


    public boolean isGolfEvent() {
        return event.isGolf();
    }


    /**
     * Checks if the current event contains any scores
     * @return true if the current event contains scores
     */
    public boolean hasScores() throws ParseException, IOException {
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
