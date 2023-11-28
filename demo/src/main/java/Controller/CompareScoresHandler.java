package Controller;

import Model.Event;
import Model.Person;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CompareScoresHandler extends UIHandler {

    Person person;
    String eventType;
    String personID;

    public CompareScoresHandler(String eventID, String personID){
        super.setDb();
        try {
            person = db.GetPerson(personID);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        setEventType(eventID);
        this.personID = personID;
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
}
