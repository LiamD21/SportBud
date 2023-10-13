package Model;

import java.util.ArrayList;

public class Event {
    private ArrayList<Score> scores;
    private String eventName;
    private String eventType;
    private Boolean isGroup;

    public Event(String name, String type, Boolean group){
        scores = new ArrayList<>();
        this.eventType = name;
        this.eventType = type;
        this.isGroup = group;
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
    * Creates a new Score object and adds it to the list
     */
    public void addScore(int groupSize){

    }

}
