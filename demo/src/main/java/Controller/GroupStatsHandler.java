package Controller;

import Model.Event;
import Model.Group;
import Model.Score;
import javafx.scene.chart.XYChart;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GroupStatsHandler extends UIHandler{
    private Event event;
    private Group group;
    public GroupStatsHandler(String groupName, String eventname){

        super.setDb();
        try {
            group = db.GetGroup(groupName);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        event = getEvent(eventname);
    }


    private Event getEvent(String eventID){
        for (Event event : group.getGroupEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                return event;
            }
        }
        return null;
    }


    public String getEventType(){
        return event.getEventType();
    }

    public String getGroupName(){
        return group.getGroupName();
    }

    public ArrayList<String> getGroupPeople(){
        return group.getPeople();
    }

    public void populatePersonsSeries(XYChart.Series series, int holeNumber){
        //go through each score in the event, and if the person has that score, getScore()
        // and insert with that persons name
        int nthScore = 0;   //holds the n'th score that was inserted for this event by a single player

        for (int i = 0; i < event.getScores().size(); i++){
            Score score = event.getScores().get(i);
            //if score name ==  series name (ie same person) getScore
            if(score.getPersonsName().equals(series.getName())){
                nthScore += 1;
                //get the persons score, and add it to the series with .. Name? and the personsScore
                int personsScore = getAScore(score, holeNumber);
                series.getData().add(new XYChart.Data("Score # "+nthScore, personsScore));

            }
        }
    }

    //get a score total for a specific
    public int getAScore(Score score, int holeNumber){
        int scoreTotaled;

        //if the holenumber is 0 then it is the total we want to get
        if (holeNumber == 0) {
            scoreTotaled = 0;
            for (int holeScore: score.getScores()) {
                scoreTotaled += holeScore;
            }
        }
        //else if the holenumber is not zero, just get the value of the hole

        else {
            scoreTotaled = score.getScores()[holeNumber - 1];
        }

        //return the score/total of the specified hole
        return scoreTotaled;
    }

    public boolean isGolfEvent(){
        return event.isGolf();
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

}
