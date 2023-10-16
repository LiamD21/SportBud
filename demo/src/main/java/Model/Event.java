package Model;

import java.util.ArrayList;

public class Event {
    private ArrayList<Score> scores;
    private String eventName;
    private String eventType;
    private Boolean isGroup;

    public Event(String name, String type, Boolean group){
        //Score s1, s2, s3, s4;
        scores = new ArrayList<>();
        this.eventName = name;
        this.eventType = type;
        this.isGroup = group;
        /*
        s1 = new Score(this.eventType);
        s2 = new Score(this.eventType);
        s3 = new Score(this.eventType);
        s4 = new Score(this.eventType);
        scores.add(s1);
        scores.add(s2);
        scores.add(s3);
        scores.add(s4);

         */

    }

    public String getEventName(){
        return this.eventName;
    }

    public String getEventType(){
        return this.eventType;
    }

    public Boolean getIsGroup(){
        return this.isGroup;
    }

    public ArrayList<Score> getScores(){
        return this.scores;
    }

    /*
    Take the scores and add them to the event
     */
    public void inputScores(Score inputScore){
        this.scores.add(inputScore);
    }

}
