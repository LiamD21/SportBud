package Model;

import java.util.ArrayList;

public class Event {
    private ArrayList<Score> scores;
    private String eventName;
    private String eventType;
    private Boolean isGroup;
    private ArrayList<String> chat;

    public Event(String name, String type, Boolean group){
        this.scores = new ArrayList<>();
        this.eventName = name;
        this.eventType = type;
        this.isGroup = group;
        this.chat = new ArrayList<>();
    }
    /* Method to check if the event is related to golf */
    public boolean isGolf(){
        return this.eventType.equals("Front 9") || this.eventType.equals("Back 9") || this.eventType.equals("18");
    }
    /* Get the name of the event */
    public String getEventName(){
        return this.eventName;
    }
    /* Get the type of the event */
    public String getEventType(){
        return this.eventType;
    }
    /* Method to check if the event is a group event (true) or a solo event (false) */
    public Boolean getIsGroup(){
        return this.isGroup;
    }
    /* Get the list of scores associated to the event */
    public ArrayList<Score> getScores(){
        return this.scores;
    }

    public ArrayList<String> getChat() {return this.chat;}

    public void addChat(String username, String message){
        this.chat.add(username + ": " + message);
    }

    /*
        Take the scores and add them to the event
         */
    public void inputScores(Score inputScore){
        this.scores.add(inputScore);
    }

}
