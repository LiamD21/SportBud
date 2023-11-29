package Controller;

import Model.Event;
import Model.Group;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class GroupEventLbHandler extends UIHandler{
    private Event event;
    private Group group;

    private String groupIDStr;

    private String eventIDStr;

    public GroupEventLbHandler(String eventID, String groupID){
        super.setDb();
        eventIDStr = eventID;
        groupIDStr = groupID;
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
            while (Hms >= 3600){
                Hms -= 3600;
                h ++;
            }

            // find minutes
            while (Hms >= 60){
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
     * @param hole an integet to see what hole to sort by. 0 means total, -1 means
     *             no input, so we also sort by total.
     * @return the sorted array list of scores in this event
     */
    public ArrayList<String> getScores(int hole) throws ParseException, IOException {
        if (hole  ==-1){
            hole =0;
        }
        if (this.isTimedEvent()){
            return convertTime(sortScores(event.getScores(), hole));
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

        // if we want to sort by highest scores, reverse the array before removing scores
        if (Objects.equals(event.getEventType(), "Time-Highest") || Objects.equals(event.getEventType(), "Points-Highest")) {
            int start = 0;
            int end = totalsPlaceholder.size() - 1;

            while (start < end){
                int temp = totalsPlaceholder.get(start);
                String temp2 = peoplesScoresMirrorArr.get(start);

                totalsPlaceholder.set(start, totalsPlaceholder.get(end));
                peoplesScoresMirrorArr.set(start, peoplesScoresMirrorArr.get(end));
                totalsPlaceholder.set(end, temp);
                peoplesScoresMirrorArr.set(end, temp2);

                start = start + 1;
                end = end - 1;
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

        // if we initially reversed the array, we must reverse it again before returning
        if (Objects.equals(event.getEventType(), "Time-Highest") || Objects.equals(event.getEventType(), "Points-Highest")) {
            int start = 0;
            int end = bests.size() - 1;

            while (start < end){
                int temp = bests.get(start);
                String temp2 = seen.get(start);

                bests.set(start, bests.get(end));
                seen.set(start, seen.get(end));
                bests.set(end, temp);
                seen.set(end, temp2);

                start = start + 1;
                end = end - 1;
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

    public ArrayList<String> getComments() {
        return event.getChat();
    }

    /**
     * append a comment to the event chat
     */
    public void handleCommentAdd(String name, String comment) throws IOException, ParseException {
        db.AddGroupChat(groupIDStr, name, eventIDStr, comment);
    }
}
