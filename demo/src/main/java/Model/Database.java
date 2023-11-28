package Model;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.*;

// Database class is intializing JSON file where data is being stored
public class Database {
    private String filePath = "databaseTEST.json";
    private JSONArray jsonArray;
    private JSONParser parser;
    public Database() throws FileNotFoundException, ParseException {
        parser = new JSONParser();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                initializeData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Sets up the initial state of the data used by our program */
    private void initializeData() {

        JSONArray existingData = new JSONArray();

        if (existingData.isEmpty()) {
            existingData.add(new JSONObject());
            existingData.add(new JSONObject());
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            existingData.writeJSONString(fileWriter);
            fileWriter.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Helper function to convert a JSON array to a Java Int Array
     */
    private int[] JSONArrayToJavaIntArray(JSONArray jsonArray){
        int[] intArray = new int[jsonArray.size()];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = Integer.parseInt(jsonArray.get(i).toString());
        }
        return intArray;
    }

    /*
     * Helper function to convert a Java Int to a JSON Array
     */
    private JSONArray IntArrayToJsonArray(int[] intArray) {
        JSONArray jsonArray = new JSONArray();
        for (int value : intArray) {
            jsonArray.add(value);
        }
        return jsonArray;
    }

    /**
     * Retrieves a Person object from the data stored
     *
     * This method parses a JSON file to retrieve a Person's information from the 'userName' parameter
     * It then constructs a Person object and fills it with that person's
     * associated events, groups and scores.
     *
     * @param userName: Person's id
     * @return p: A Person object with the information retrieved
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public Person GetPerson(String userName) throws ParseException, IOException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);
        JSONArray person = (JSONArray) personHT.get(userName);
        Person p = new Person((String) person.get(0));

        int x = 0;

        int numOfEvents = ((JSONArray) person.get(1)).size();
        int numOfGroups = ((JSONArray) person.get(2)).size();

        if (!((JSONArray) person.get(1)).isEmpty()) {
            for (int i = 0; i < numOfEvents; i++) {
                String name = (String) ((JSONArray) ((JSONArray) person.get(1)).get(i)).get(1);
                String type = (String) ((JSONArray) ((JSONArray) person.get(1)).get(i)).get(2);
                Boolean isGroup = (Boolean) ((JSONArray) ((JSONArray) person.get(1)).get(i)).get(3);
                p.addPersonalEvent(new Event(name, type, isGroup));
                int numOfTimesEventHasBeenPlayed = (((JSONArray) ((JSONArray) ((JSONArray) person.get(1)).get(i)).get(0))).size();
                for (int j = 0; j < numOfTimesEventHasBeenPlayed; j++) {

                    //The current score array
                    int[] arr = JSONArrayToJavaIntArray(
                            (JSONArray) ((JSONArray) ((JSONArray) ((JSONArray) person.get(1)).get(i)).get(0)).get(j));

                    //Initializes the Score Object
                    p.getPersonalEvents().get(i).inputScores(new Score(type, userName, j));

                    //Inputs the array into the score object
                    p.getPersonalEvents().get(i).getScores().get(j).inputScore(arr);
                }
            }
        }

        //Adds Groups to Person
        if (!((JSONArray) person.get(2)).isEmpty()) {
            for (int i = 0; i < numOfGroups; i++) {
                p.addGroup((String) ((JSONArray) person.get(2)).get(i));
            }
        }


        return p;
    }

    /**
     * Retrieves a Group object from the data stored
     *
     * This method parses a JSON file to retrieve a Group's information from the 'groupName' parameter
     * It then constructs a Group object and fills it with the group's
     * associated events, people and scores.
     *
     * @param groupName: Group's id
     * @return g: A Ggroup object with the information retrieved
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public Group GetGroup(String groupName) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);
        JSONArray group = (JSONArray) groupHT.get(groupName);
        Group g = new Group((String) group.get(0));
        HashMap<String,Integer> counters = new HashMap<>();

        int groupSize = ((JSONArray) group.get(1)).size();

        //Adds group members to group object
        for (int i = 0; i < groupSize; i++){
            String name = (String) ((JSONArray) group.get(1)).get(i);
            g.AddGroupMember(name);
            counters.put(name,1);
        }
        int x = 0;


        int numOfEvents = ((JSONArray) group.get(2)).size();
        for (int i = 0; i < numOfEvents; i++){
            int numOfTimeEventHasBeenPlayed = ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).size();
            String name = (String) ((JSONArray)((JSONArray) group.get(2)).get(i)).get(1);
            String type = (String) ((JSONArray)((JSONArray) group.get(2)).get(i)).get(2);
            g.AddGroupEvent(new Event(name,type,true));
            for (int j = 0; j < numOfTimeEventHasBeenPlayed; j++){
                int[] arr = JSONArrayToJavaIntArray((JSONArray) ((JSONArray) ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).get(j)).get(0));
                String personsName = (String) ((JSONArray) ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).get(j)).get(1);

                g.getGroupEvents().get(i).inputScores(new Score(type, personsName, counters.get(personsName)));


                counters.put(personsName, counters.get(personsName) + 1 );

                //Inputs the array into the score object
                g.getGroupEvents().get(i).getScores().get(j).inputScore(arr);
            }

        }
        return g;
    }
    /**
     * Adds a new person to the data stored
     *
     * This method parses a JSON file and adds the person's information to the data
     * Data includes the person's name, events, scores, group members
     *
     * @param username: The new person's ID
     * @param person: Person object
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddPerson(String username, Person person) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);

        JSONArray info = new JSONArray();
        JSONArray eventArray = new JSONArray();
        JSONArray scoreArray = new JSONArray();


        info.add(person.getName());

        //Initialize Array with number of event sub arrays
        for (int i = 0; i < person.getPersonalEvents().size(); i++){
            eventArray.add(new JSONArray());
            ((JSONArray) eventArray.get(i)).add(new JSONArray());

        for (int j = 0; j < person.getPersonalEvents().get(i).getScores().size(); j++){
                int[] score = person.getPersonalEvents().get(i).getScores().get(j).getScores();
                ((JSONArray)((JSONArray) eventArray.get(i)).get(0)).add(IntArrayToJsonArray(score));
            }
            ((JSONArray) eventArray.get(i)).add(person.getPersonalEvents().get(i).getEventName());
            ((JSONArray) eventArray.get(i)).add(person.getPersonalEvents().get(i).getEventType());
            ((JSONArray) eventArray.get(i)).add(false);
        }

        info.add(eventArray);
        info.add(scoreArray);


        for (int i = 0; i < person.getGroups().size(); i++){
            ((JSONArray) info.get(2)).add(person.getGroups().get(i));
        }

        personHT.put(username,info);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Adds a new group to the data stored
     *
     * This method parses a JSON file and adds the group's information
     * Data includes the group's name, events, scores, group members
     *
     * @param groupUserName: The new group's ID
     * @param group: Group object
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddGroup(String groupUserName, Group group) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);

        JSONArray info = new JSONArray();
        JSONArray eventArray = new JSONArray();

        info.add(group.getGroupName());
        info.add(new JSONArray());


        for (int i = 0; i < group.getPeople().size(); i++){
            ((JSONArray) info.get(1)).add(group.getPeople().get(i));
        }

        //Initialize Array with number of event sub arrays

        for (int i = 0; i < group.getGroupEvents().size(); i++) {
            eventArray.add(new JSONArray());
            ((JSONArray) eventArray.get(i)).add(new JSONArray());

            for (int j = 0; j < group.getGroupEvents().get(i).getScores().size(); j++){
                JSONArray scoreArray = new JSONArray();
                int[] score = group.getGroupEvents().get(i).getScores().get(j).getScores();
                String personsName = group.getGroupEvents().get(i).getScores().get(j).getPersonsName();
                scoreArray.add(IntArrayToJsonArray(score));
                scoreArray.add(personsName);
                ((JSONArray)((JSONArray) eventArray.get(i)).get(0)).add(scoreArray);

            }

            ((JSONArray) eventArray.get(i)).add(group.getGroupEvents().get(i).getEventName());
            ((JSONArray) eventArray.get(i)).add(group.getGroupEvents().get(i).getEventType());
            ((JSONArray) eventArray.get(i)).add(true);
        }

        info.add(eventArray);

        groupHT.put(groupUserName,info);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the array of stored usernames
     *
     * @return Array of usernames representing people in the database
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public String[] GetPeople() throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);
        int numOfPeople = personHT.size();

        String[] strArray = personHT.keySet().toString().split(", ");
        strArray[0] = strArray[0].substring(1);
        strArray[numOfPeople-1] = strArray[numOfPeople-1].substring(0, strArray[numOfPeople-1].length()-1);

        return strArray;
    }

    /**
     * This method retrieves the array of stored group names
     *
     * @return Array of group names representing groups in the database
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public String[] GetGroups() throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);

        int numOfGroups = groupHT.size();

        String[] strArray = groupHT.keySet().toString().split(", ");
        strArray[0] = strArray[0].substring(1);

        if (numOfGroups >= 1)
            strArray[numOfGroups-1] = strArray[numOfGroups-1].substring(0, strArray[numOfGroups-1].length()-1);
        else {
            strArray[0] = strArray[0].substring(0, strArray[0].length()-1);
        }

        return strArray;


    }
    /**
     * This method updates the scores of a specified solo event for the
     * username given.
     *
     * @param Username: The person's id for the scores to be added
     * @param eventName: Event name for the scores to be added
     * @param scores: Array of scores for the specified event
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddSoloScores(String Username, String eventName, int[] scores) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);
        JSONArray person = (JSONArray) personHT.get(Username);
        JSONArray eventArray = (JSONArray) person.get(1);
        JSONArray event = null;

        for (int i = 0; i < eventArray.size(); i++) {
            if (eventName.equals(((JSONArray) eventArray.get(i)).get(1))) {
                event = ((JSONArray) eventArray.get(i));
                break;
            }
        }

        if (event == null)
            throw new RuntimeException("Error, there is no event by the name of " + eventName);


        ((JSONArray) event.get(0)).add(IntArrayToJsonArray(scores));

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * This method updates the scores of a specified group event for the
     * group name given and the person whose scores are to be added
     *
     * Group scores must be inputted in the SAME order that their names appear in the people ArrayList in the Group class
     * Ex, if Braeden is index 0 people ArrayList in the Group Class
     * then his scores MUST ALSO be at index 0 of the scores array
     *
     * @param groupUsername: The group's id to reference members
     * @param eventName: Event name for the scores to be added
     * @param scores: Array of scores for the specified event
     * @param personUsername: person's id for scores to be added to
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddGroupScores(String groupUsername,String eventName, int[] scores, String personUsername) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);
        JSONArray group = ((JSONArray) groupHT.get(groupUsername));
        JSONArray inputArray = new JSONArray();

        JSONArray eventArray = null;
        int eventSize = ((JSONArray) group.get(2)).size();


        //Searches for the event
        for (int i = 0; i < eventSize; i++){
            if (eventName.equals(((JSONArray)((JSONArray) group.get(2)).get(i)).get(1))){
                eventArray = ((JSONArray)((JSONArray) group.get(2)).get(i));
                break;
            }
        }

        if (eventArray == null)
            throw new RuntimeException("Error event not found");

        JSONArray scoreArray = (JSONArray) eventArray.get(0);
        inputArray.add(IntArrayToJsonArray(scores));
        inputArray.add(personUsername);

        scoreArray.add(inputArray);


        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void AddPersontoGroup(String personUsername, String groupUsername) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));

        JSONObject groupHT = (JSONObject) array.get(1);

        JSONArray group = ((JSONArray)((JSONArray)groupHT.get(groupUsername)).get(1));

        for (Object o : group) {
            if (personUsername.equals(o))
                throw new RuntimeException("Error, person is already in the group");
        }

        if (group.size() == 4)
            throw new RuntimeException("Error, groups cannot be bigger than 4");

        group.add(personUsername);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * This method adds a new group event and empty score array to the group specified
     *
     * @param groupUsername: The group's id to reference members
     * @param eventName: Event name for the scores to be added
     * @param eventType: Type of event
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddGroupEvent(String groupUsername, String eventName, String eventType) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);
        JSONArray group = ((JSONArray) groupHT.get(groupUsername));
        JSONArray events = (JSONArray) group.get(2);

        JSONArray newEvent = new JSONArray();

        //Initializing an empty score array
        newEvent.add(new JSONArray());

        newEvent.add(eventName);
        newEvent.add(eventType);
        newEvent.add(true);

        events.add(newEvent);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * This method adds a new solo event and empty score array
     * to the person specified
     *
     * @param Username: The person's id to reference the person
     * @param eventName: Event name for the scores to be added
     * @param eventType: Type of event
     * @throws ParseException: Parse Error in JSON file
     * @throws IOException: Error reading the file
     */
    public void AddSoloEvent(String Username, String eventName, String eventType) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);

        JSONArray person = ((JSONArray) personHT.get(Username));
        JSONArray events = (JSONArray) person.get(1);


        JSONArray newEvent = new JSONArray();

        //Initializing an empty score array
        newEvent.add(new JSONArray());

        newEvent.add(eventName);
        newEvent.add(eventType);
        newEvent.add(false);


        events.add(newEvent);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Database db = new Database();

        //GetGroup() Test
        if (!Arrays.equals(db.GetGroups(),new String[]{"group1", "PGA Proz"})){
            System.out.print("Error GetGroups() Test Failed should have returned [group1, PGA Proz] ");
            System.out.println("but it is returning"+Arrays.toString(db.GetGroups()));

        }

        //GetPeople() Test
        if (!Arrays.equals(db.GetPeople(),new String[]{"person4", "person3", "person2", "person1"})) {
            System.out.print("Error GetPeople() Test Failed should have returned [person4, person3, person2, person1] ");
            System.out.println(Arrays.toString(db.GetPeople()));
        }


        Person Braeden = db.GetPerson("person1");

        //Person Class Tests


        if (!Braeden.getName().equals("Braeden Kroetsch")){
            System.out.print("Braeden Test 1 returned");
            System.out.println(Braeden.getName());
        }


        // Event Class tests
        if (!Braeden.getPersonalEvents().get(0).getEventName().equals("Golf1")){
            System.out.print("Braeden Test 2 returned");
            System.out.print(Braeden.getPersonalEvents().get(0).getEventName());
            System.out.println("When it should've returned Golf1");

        }

        if (!Braeden.getPersonalEvents().get(0).getEventType().equals("Front 9")){
            System.out.print("Braeden Test 3 returned");
            System.out.print(Braeden.getPersonalEvents().get(0).getEventType());
            System.out.println("When it should've returned Front 9");

        }

        if (!Braeden.getPersonalEvents().get(0).isGolf()){
            System.out.println("Error Braeden Test 3.5 should be a golf event");
        }

        if (Braeden.getPersonalEvents().get(0).getIsGroup())
            System.out.println("Braeden Test 4 should be false");


        if (!Braeden.getPersonalEvents().get(1).getEventName().equals("Golf2")){
            System.out.print("Braeden Test 5 returned");
            System.out.print(Braeden.getPersonalEvents().get(1).getEventName());
            System.out.println("When it should've returned Golf2");

        }

        if (!Braeden.getPersonalEvents().get(1).getEventType().equals("Back 9")){
            System.out.print("Braeden Test 6 returned");
            System.out.print(Braeden.getPersonalEvents().get(1).getEventType());
            System.out.println("When it should've returned Back 9");

        }

        if (Braeden.getPersonalEvents().get(1).getIsGroup())
            System.out.println("Braeden Test 7 should be false");

        // Golf1 tests (Score class)


        if (Braeden.getPersonalEvents().get(0).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Braeden Test 8 should be 0 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(0).getScores().get(0).getPersonsName().equals("person1")){
            System.out.print("Error, Braeden Test 9 should be person1 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getPersonsName());
        }

        if (!Braeden.getPersonalEvents().get(0).getScores().get(0).getType().equals("Front 9")){
            System.out.print("Error, Braeden Test 10 should be Front 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(0).getScores().get(0).getScores(),
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error, Braeden Test 10 should be [10, 10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(0).getScores())+"\n");
        }

        if (Braeden.getPersonalEvents().get(0).getScores().get(1).getEventCounter() != 1){
            System.out.print("Error, Braeden Test 11 should be 1 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(1).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(0).getScores().get(1).getType().equals("Front 9")){
            System.out.print("Error, Braeden Test 13 should be Front 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(1).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(0).getScores().get(1).getScores(),
                new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3})){
            System.out.print("Error, Braeden Test 14 should be [3, 3, 3, 3, 3, 3, 3, 3, 3] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(1).getScores())+"\n");
        }


        if (Braeden.getPersonalEvents().get(0).getScores().get(2).getEventCounter() != 2){
            System.out.print("Error, Braeden Test 15 should be 2 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(2).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(0).getScores().get(2).getType().equals("Front 9")){
            System.out.print("Error, Braeden Test 16 should be Front 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(2).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(0).getScores().get(2).getScores(),
                new int[]{3, 3, 3, 3, 69, 420, 3, 3, 3})){
            System.out.print("Error, Braeden Test 17 should be [3, 3, 3, 3, 69, 420, 3, 3, 3] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(2).getScores())+"\n");
        }

        // Golf2 tests (Score class) */

        if (Braeden.getPersonalEvents().get(1).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Braeden Test 18 should be 0 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(0).getPersonsName().equals("person1")){
            System.out.print("Error Braeden Test 19 should be person1 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getPersonsName());

        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(0).getType().equals("Back 9")){
            System.out.print("Error, Braeden Test 20 should be Back 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(1).getScores().get(0).getScores(),
                new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1})){
            System.out.print("Error, Braeden Test 21 should be [3, 3, 3, 3, 69, 420, 3, 3, 3] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(0).getScores())+"\n");
        }

        if (Braeden.getPersonalEvents().get(1).getScores().get(1).getEventCounter() != 1){
            System.out.print("Error, Braeden Test 22 should be 1 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(1).getPersonsName().equals("person1")){
            System.out.print("Error Braeden Test 23 should be person1 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getPersonsName());

        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(1).getType().equals("Back 9")){
            System.out.print("Error, Braeden Test 24 should be Back 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(1).getScores().get(1).getScores(),
                new int[]{32, 34, 36, 835, 31, 33, 35, 343, 33})){
            System.out.print("Error, Braeden Test 25 should be [32, 34, 36, 835, 31, 33, 35, 343, 33] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(1).getScores())+"\n");
        }

        if (Braeden.getPersonalEvents().get(1).getScores().get(2).getEventCounter() != 2){
            System.out.print("Error, Braeden Test 26 should be 1 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(2).getPersonsName().equals("person1")){
            System.out.print("Error Braeden Test 27 should be person1 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getPersonsName());

        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(2).getType().equals("Back 9")){
            System.out.print("Error, Braeden Test 28 should be Back 9 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getType());
        }

        if (!Arrays.equals(Braeden.getPersonalEvents().get(1).getScores().get(2).getScores(),
                new int[]{32, 34, 36, 835, 31, 33, 35, 343, 33})){
            System.out.print("Error, Braeden Test 29 should be [32, 34, 36, 835, 31, 33, 35, 343, 33] but it is returning");
            System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(2).getScores())+"\n");
        }

        // Group tests (Person Class)*/
        ArrayList<String> test1 = new ArrayList<>();
        test1.add("group1");
        test1.add("PGA Proz");
        if (!Braeden.getGroups().equals(test1)){
            System.out.println("Error, Braeden Test 30 should be [group1, PGA Proz] but it is returning"
                    + Braeden.getGroups());
        }

        Person M = db.GetPerson("person2");

        // Person Class Tests */

        if (!M.getName().equals("Mohammed Golfguy")) {
            System.out.println("Error, Mohammed Golfguy Test 1 should be Mohammed Golfguy but it is returning" + M.getName());
        }

        // Event Class tests

        if (!M.getPersonalEvents().get(0).getEventName().equals("Golf1")){
            System.out.print("Error, Mohammed Golfguy Test 2 should be Golf1 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getEventName());
        }

        if (!M.getPersonalEvents().get(0).getEventType().equals("18")){
            System.out.print("Error, Mohammed Golfguy Test 2 should be 18 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getEventType());
        }

        if (M.getPersonalEvents().get(0).getIsGroup()){
            System.out.println("Error, Mohammed Golfguy Test 3 should be false");
        }

        // Golf1 tests (Score class)
        if (M.getPersonalEvents().get(0).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Mohammed Golfguy Test 4 should be 0 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        }

        if (!M.getPersonalEvents().get(0).getScores().get(0).getPersonsName().equals("person2")){
            System.out.print("Error, Mohammed Golfguy Test 5 should be person2 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getPersonsName());
        }

        if (!M.getPersonalEvents().get(0).getScores().get(0).getType().equals("18")){
            System.out.print("Error, Mohammed Golfguy Test 6 should be 18 but it is returning");
        }

        if (!Arrays.equals(M.getPersonalEvents().get(0).getScores().get(0).getScores(),
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error, Mohammed Golfguy Test 7 should be [10, 10, 10, 10, 10, " +
                    "10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(M.getPersonalEvents().get(0).getScores().get(0).getScores())+"\n");
        }

        if (M.getPersonalEvents().get(0).getScores().get(1).getEventCounter() != 1){
            System.out.print("Error, Mohammed Golfguy Test 8 should be 1 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(1).getEventCounter());
        }

        if (!M.getPersonalEvents().get(0).getScores().get(1).getPersonsName().equals("person2")){
            System.out.print("Error, Mohammed Golfguy Test 9 should be person2 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(1).getPersonsName());
        }

        if (!M.getPersonalEvents().get(0).getScores().get(1).getType().equals("18")){
            System.out.print("Error, Mohammed Golfguy Test 10 should be 18 but it is returning");
        }


        if (!Arrays.equals(M.getPersonalEvents().get(0).getScores().get(1).getScores(),
                new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error, Mohammed Golfguy Test 11 should be [3, 3, 3, 3, 3, 3, 3, 3, 3, 10, " +
                    "10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(M.getPersonalEvents().get(0).getScores().get(1).getScores())+"\n");
        }

        //Group tests (Person Class)

        ArrayList<String> test2 = new ArrayList<>();
        test2.add("group1");
        if (!M.getGroups().equals(test2)){
            System.out.print("Error Mohammed Golfguy Test 12 should be [group1] but it is returning");
            System.out.println(M.getGroups());
        }

        //Someone with no personal events
        Person Hunter = db.GetPerson("person3");
        //Group tests (Person Class)


        ArrayList<String> test3 = new ArrayList<>();
        test3.add("Golf Team 2");
        if (!Hunter.getGroups().equals(test3)){
            System.out.print("Error, Hunter test 1 should be [Golf Team 2] but it is returning");
            System.out.println(Hunter.getGroups());
        }

        if (!Hunter.getPersonalEvents().isEmpty()){
            System.out.println("Error, Hunter Test 2 should be an empty arraylist but it is returning");
            System.out.println(Hunter.getPersonalEvents());
        }

        //Someone with no groups
        Person Jane = db.GetPerson("person4");

        //Score Class Tests
        if (!Jane.getPersonalEvents().get(0).getEventName().equals("Golf1")){
            System.out.print("Error, Jane Test 1 should be Golf1 but it is returning");
            System.out.println(Jane.getPersonalEvents().get(0).getEventName());
        }

        if (!Arrays.equals(Jane.getPersonalEvents().get(0).getScores().get(0).getScores(),
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error, Jane Test 2 should be [10, 10, 10, 10, 10, " +
                    "10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(Jane.getPersonalEvents().get(0).getScores().get(0).getScores()));
        }

        if (!Arrays.equals(Jane.getPersonalEvents().get(0).getScores().get(1).getScores(),
                new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error, Jane Test 3 should be [3, 3, 3, 3, 3, 3, 3, 3, 3, 10, " +
                    "10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(Jane.getPersonalEvents().get(0).getScores().get(1).getScores()));
        }

        //GetGroup() tests
        Group group1 = db.GetGroup("group1");

        if (!group1.getGroupName().equals("Golf Team 1")){
            System.out.print("Error, group1 Test 1 should be returning Golf Team 1 but it is returning");
            System.out.println(group1.getGroupName());
        }

        if (!db.GetPerson(group1.getPeople().get(0)).getName().equals("Braeden Kroetsch")){
            System.out.print("Error, group1 Test 2 should be returning Braeden Kroetsch but it is returning");
            System.out.println(db.GetPerson(group1.getPeople().get(0)).getName());
        }

        if (!db.GetPerson(group1.getPeople().get(1)).getName().equals("Mohammed Golfguy")){
            System.out.print("Error, group1 Test 3 should be returning Mohammed Golfguy but it is returning");
            System.out.println(db.GetPerson(group1.getPeople().get(1)).getName());
        }

        if (group1.getGroupSize() != 2){
            System.out.print("Error, group1 Test 4 should be returning 2 but it is returning");
            System.out.println(group1.getGroupSize());
        }

        if (!group1.getGroupEvents().get(0).getEventName().equals("Golf1")){
            System.out.print("Error, group1 Test 5 should be returning Golf1 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getEventName());
        }

        if (!group1.getGroupEvents().get(0).getEventType().equals("Front 9")){
            System.out.print("Error, group1 Test 6 should be returning Front 9 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getEventType());
        }

        if (!group1.getGroupEvents().get(0).getIsGroup()){
            System.out.println("Error group1 Test 7 should be True");
        }

        if (!Arrays.equals(group1.getGroupEvents().get(0).getScores().get(0).getScores(),
                new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10})){
            System.out.print("Error group1 Test 8 should be [10, 10, 10, 10, 10, 10, 10, 10, 10] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(0).getScores()));
        }

        if (!group1.getGroupEvents().get(0).getScores().get(0).getPersonsName().equals("person1")){
            System.out.print("Error group1 Test 9 should be person1");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(0).getPersonsName());
        }

        if (!Arrays.equals(group1.getGroupEvents().get(0).getScores().get(1).getScores(),
                new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3})){
            System.out.print("Error group1 Test 10 should be [3, 3, 3, 3, 3, 3, 3, 3, 3] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(1).getScores()));
        }

        if (!Arrays.equals(group1.getGroupEvents().get(0).getScores().get(2).getScores(),
                new int[] {69, 30, 30, 30, 69, 420, 30, 30, 30})){
            System.out.print("Error group1 Test 11 should be [69, 30, 30, 30, 69, 420, 30, 30, 30] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(2).getScores()));
        }

        if (!Arrays.equals(group1.getGroupEvents().get(0).getScores().get(3).getScores(),
                new int[] {4, 2, 5, 2, 2, 2, 1, 5, 3})){
            System.out.print("Error group1 Test 12 should be [4, 2, 5, 2, 2, 2, 1, 5, 3] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(3).getScores()));
        }


        if (!group1.getGroupEvents().get(1).getEventName().equals("Golf2")){
            System.out.print("Error group1 test 13 should be Golf2 but it is returning");
            System.out.println(group1.getGroupEvents().get(1).getEventName());
        }

        if (!group1.getGroupEvents().get(1).getEventType().equals("Back 9")){
            System.out.print("Error group1 test 14 should be Back 9 but it is returning");
            System.out.println(group1.getGroupEvents().get(1).getEventType());
        }

        if (!group1.getGroupEvents().get(1).getIsGroup()){
            System.out.println("Error group1 test 15 should be true");
        }

        if (!Arrays.equals(group1.getGroupEvents().get(1).getScores().get(0).getScores(),
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1})){
            System.out.print("Error group1 Test 16 should be [1, 1, 1, 1, 1, 1, 1, 1, 1] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(0).getScores()));
        }

        if (!Arrays.equals(group1.getGroupEvents().get(1).getScores().get(1).getScores(),
                new int[] {32, 34, 36, 835, 31, 33, 35, 343, 33})){
            System.out.print("Error group1 Test 17 should be [32, 34, 36, 835, 31, 33, 35, 343, 33] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(1).getScores()));
        }

        if (group1.getGroupEvents().get(0).getScores().get(3).getEventCounter() != 2){
            System.out.print("Error group1 test 18 should be 2 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(3).getEventCounter());
        }

        if (group1.getGroupEvents().get(0).getScores().get(4).getEventCounter() != 3) {
            System.out.print("Error group1 test 19 should be 3 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(4).getEventCounter());
        }

        if (group1.getGroupEvents().get(0).getScores().get(5).getEventCounter() != 4) {
            System.out.print("Error group1 test 20 should be 3 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(5).getEventCounter());
        }

        if (group1.getGroupEvents().get(0).getScores().get(6).getEventCounter() != 3) {
            System.out.print("Error group1 test 21 should be 3 but it is returning");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(6).getEventCounter());
        }


        Group proz = db.GetGroup("PGA Proz");




        // TESTS THAT MODIFY THE DATABASE
        /*
        //AddGroup Tests
        Group g = new Group("Golf Group 1");
        g.AddGroupEvent( new Event("Golf1","Front 9",true) );

        g.AddGroupMember("person1");
        g.AddGroupMember("person2");


        g.AddGroupEvent(new Event("Golf2","Back 9",true));
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","person1",0));
        g.getGroupEvents().get(0).getScores().get(0).inputScore(new int[]{1,1,1,1,1,1,1,1,1});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","person2",0));
        g.getGroupEvents().get(0).getScores().get(1).inputScore(new int[]{1,1,2,3,420,60,69,70,28});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","person1",1));
        g.getGroupEvents().get(0).getScores().get(2).inputScore(new int[]{1,1,1,1,1,2,1,1,1});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","person2",1));
        g.getGroupEvents().get(0).getScores().get(3).inputScore(new int[]{1,1,1,1,1,2,420,1,1});

        g.getGroupEvents().get(1).inputScores(new Score("Front 9","person1",0));
        g.getGroupEvents().get(1).getScores().get(0).inputScore(new int[]{69,1,2,3,4,5,6,7,8});
        g.getGroupEvents().get(1).inputScores(new Score("Front 9","person2",0));
        g.getGroupEvents().get(1).getScores().get(1).inputScore(new int[]{1,1,2,3,420,5,69,7,8});

        g.AddGroupEvent(new Event("Push Ups","Push Ups",true));
        g.getGroupEvents().get(2).inputScores(new Score("Push Ups","person1",0));
        g.getGroupEvents().get(2).getScores().get(0).inputScore(new int[]{20});
        g.getGroupEvents().get(2).inputScores(new Score("Push Ups","person2",0));
        g.getGroupEvents().get(2).getScores().get(1).inputScore(new int[]{69});




        db.AddGroup("test",g);



        if (db.GetGroup("test").getGroupEvents().get(2).isGolf())
            System.out.println("Error, group push up event should not be golf");

        if (!Arrays.equals(db.GetGroup("test").getGroupEvents().get(2).getScores().get(0).getScores(),
                new int[]{20})){
            System.out.print("Error score should be [20] but it is returning");
            System.out.println(Arrays.toString(db.GetGroup("test").getGroupEvents().get(2).getScores().get(0).getScores()));
        }

        if (!Arrays.equals(db.GetGroup("test").getGroupEvents().get(2).getScores().get(1).getScores(),
                new int[]{69})){
            System.out.print("Error score should be [69] but it is returning");
            System.out.println(Arrays.toString(db.GetGroup("test").getGroupEvents().get(2).getScores().get(1).getScores()));
        }

        if (!Arrays.equals(db.GetGroups(),new String[]{"test","group1", "PGA Proz"})){
            System.out.print("Error AddGroup() Test 1 Failed should have returned [test, group1, PGA Proz] ");
            System.out.println("but it is returning"+Arrays.toString(db.GetGroups()));

        }

        ArrayList<String> test = new ArrayList<>();
        test.add("person1");
        test.add("person2");

        if (!db.GetGroup("test").getPeople().equals(test)){
            System.out.print("Error AddGroup() Test 2 Failed should have returned [person1, person 2] ");
            System.out.println("but it is returning"+db.GetGroup("test").getPeople().toString());
        }


        db.AddPersontoGroup("person434","test");

        test.add("person434");

        if (!db.GetGroup("test").getPeople().equals(test)){
            System.out.print("Error PersonToGroup() Test Failed should have returned [person1, person 2, person434] ");
            System.out.println("but it is returning"+db.GetGroup("test").getPeople().toString());
        }


        Person Winston = new Person("Winston Smith");
        Winston.addPersonalEvent(new Event("test","Front 9",false));
        Winston.getPersonalEvents().get(0).inputScores(new Score("Front 9","Winston Smith",0));
        Winston.getPersonalEvents().get(0).getScores().get(0).inputScore(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        Winston.getPersonalEvents().get(0).inputScores(new Score("Front 9","Winston Smith",1));
        Winston.getPersonalEvents().get(0).getScores().get(1).inputScore(new int[]{1, 1, 1, 1, 1, 69, 1, 1, 1});
        Winston.addGroup("test group");
        Winston.addGroup("PGA Proz");
        Winston.addPersonalEvent(new Event("test 2","Front 9",false));
        Winston.getPersonalEvents().get(1).inputScores(new Score("Front 9",Winston.getName(),0));
        Winston.getPersonalEvents().get(1).getScores().get(0).inputScore(new int[]{1, 1, 1, 1, 1, 69, 420, 1, 1});
        Winston.getPersonalEvents().get(1).inputScores(new Score("Front 9",Winston.getName(),1));
        Winston.getPersonalEvents().get(1).getScores().get(0).inputScore(new int[]{3, 5, 1, 1, 1, 69, 420, 1, 1});


        //Testing adding non golf events
        Winston.addPersonalEvent(new Event("Push Ups","Push Ups",false));
        Winston.getPersonalEvents().get(2).inputScores(new Score("Push Ups","Winston Smith",0));
        Winston.getPersonalEvents().get(2).getScores().get(0).inputScore(new int[]{1});
        Winston.getPersonalEvents().get(2).inputScores(new Score("Push Ups","Winston Smith",1));
        Winston.getPersonalEvents().get(2).getScores().get(1).inputScore(new int[]{6});

        db.AddPerson("WinstonS",Winston);


        if (!Arrays.equals(db.GetPerson("WinstonS").
                getPersonalEvents().get(0).getScores().get(0).getScores(),new int[]{1,1,1,1,1,1,1,1,1})){
            System.out.print("Error AddPerson() Test failed, scores should be [1, 1, 1, 1, 1, 1, 1, 1, 1]" +
                    "but it is returning");
            System.out.println(Arrays.toString(db.GetPerson("WinstonS")
                    .getPersonalEvents().get(0).getScores().get(0).getScores()));

        }


        if (db.GetPerson("WinstonS").getPersonalEvents().get(2).isGolf()){
            System.out.println("Error Winston's push up event should not be a golf event");
        }

        if (!Arrays.equals(db.GetPerson("WinstonS").getPersonalEvents().get(2).getScores().get(0).getScores(),
                new int[]{1})){
            System.out.print("Error, Winstons score on push ups should be [1] but it is returning");
            System.out.println(Arrays.toString(db.GetPerson("WinstonS").getPersonalEvents().get(2).getScores().get(0).getScores()));
        }

        if (!Arrays.equals(db.GetPerson("WinstonS").getPersonalEvents().get(2).getScores().get(1).getScores(),
                new int[]{6})){
            System.out.print("Error, Winstons score on push ups should be [6] but it is returning");
            System.out.println(Arrays.toString(db.GetPerson("WinstonS").getPersonalEvents().get(2).getScores().get(1).getScores()));
        }



        db.AddGroupScores("group1","Golf2",new int[]{42,42,42,424,6,4,5,2,7},"person2");

        if (!db.GetGroup("group1").getGroupEvents().get(1).getScores().get(2).getPersonsName().equals("person2")) {
            System.out.print("Error should be person2 but it is returning");
            System.out.println(db.GetGroup("group1").getGroupEvents().get(1).getScores().get(2).getPersonsName());
        }

        if (!Arrays.equals(db.GetGroup("group1").getGroupEvents().get(1).getScores().get(2).getScores(),
                new int[] {42,42,42,424,6,4,5,2,7})) {
            System.out.println("Error should be [42,42,42,424,6,4,5,2,7] but it is returning");
            System.out.println(Arrays.toString(db.GetGroup("group1").getGroupEvents().get(1).getScores().get(2).getScores()));
        }

         */


        System.out.println("Unit Testing Complete");

    }
}
