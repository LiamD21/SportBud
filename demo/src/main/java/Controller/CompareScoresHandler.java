package Controller;

import Model.Event;
import Model.Person;
import Model.Score;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CompareScoresHandler extends UIHandler {

    Person person;
    String eventType;
    String personID;
    Event event;

    public CompareScoresHandler(String eventID, String personID){
        super.setDb();
        try {
            person = db.GetPerson(personID);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        setEventType(eventID);
        this.personID = personID;
        event = getEvent(eventID);
    }

    /**
     * Gets the event type for the given event
     * @param eventID the string event id
     */
    private void setEventType(String eventID){
        for (Event event : person.getPersonalEvents()){
            if (Objects.equals(event.getEventName(), eventID)){
                eventType = event.getEventType();
            }
        }
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
     * Gets the full name of a given person id
     * @param personID the string person id
     * @return the string person name
     */
    public String getPersonName(String personID){
        try {
            return db.GetPerson(personID).getName();
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * finds how many holes this golf event is for
     * @return the integer number of holes
     */
    private int getNumberHoles(){
        if (Objects.equals(this.eventType, "18")){
            return 18;
        }
        else {
            return 9;
        }
    }

    /**
     * finds a list of all players who have played a golf event of the given number of holes
     * @return an array list of string people's names
     */
    public ArrayList<String> getOtherGolfPlayers(){
        int numHoles = getNumberHoles();
        ArrayList<String> returnList = new ArrayList<>();
        String[] people;
        Person p;
        try {
            people = db.GetPeople();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        for (String person:people){
            try {
                p = db.GetPerson(person);
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Event> pEvents = p.getPersonalEvents();
            for (Event e:pEvents){
                if (e.isGolf()){
                    if ((numHoles == 18 && Objects.equals(e.getEventType(), "18")) || (numHoles == 9 && (Objects.equals(e.getEventType(), "Front 9") || Objects.equals(e.getEventType(), "Back 9")))) {
                        if (!Objects.equals(person, this.personID)) {
                            returnList.add(person);
                            break;
                        }
                    }
                }
            }
        }
        return returnList;
    }

    /**
     * Finds the best score for a given either 9 or 18 holes of golf
     * @param personID the string person id that we want to find the best score of
     * @return the integer best score
     */
    public int getBestScore(String personID){
        ArrayList<Event> events;
        int bestScore = -1;
        try {
            events = db.GetPerson(personID).getPersonalEvents();
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }

        events.removeIf(event -> !event.isGolf() || (getNumberHoles() == 18 && !Objects.equals(event.getEventType(), "18")) || (getNumberHoles() == 9 && !Objects.equals(event.getEventType(), "Back 9") && !Objects.equals(event.getEventType(), "Front 9")));

        for (Event eve:events){
            ArrayList<Score> scores = eve.getScores();
            for (Score sc:scores){
                int[] scoreList = sc.getScores();
                int sum = 0;
                for (Integer i:scoreList){
                    sum += i;
                }

                if (sum < bestScore || bestScore == -1){
                    bestScore = sum;
                }
            }
        }

        return bestScore;
    }
}
