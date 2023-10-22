package Model;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.parser.*;
public class Database {
    private String filePath = "databaseTEST.json";
    private JSONParser parser;
    public Database() throws FileNotFoundException, ParseException {
        parser = new JSONParser();
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
                    p.getPersonalEvents().get(i).inputScores(new Score(type, name, j));

                    //Inputs the array into the score object
                    p.getPersonalEvents().get(i).getScores().get(j).inputScore(arr);
                }
            }
        }

        //Adds Groups to Person
        //System.out.println( ((JSONArray) person.get(2)).get(0) );
        if (!((JSONArray) person.get(2)).isEmpty()) {
            for (int i = 0; i < numOfGroups; i++) {
                p.addGroup((String) ((JSONArray) person.get(2)).get(i));
            }
        }


        return p;
    }

    public Group GetGroup(String groupName) throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);
        JSONArray group = (JSONArray) groupHT.get(groupName);
        Group g = new Group((String) group.get(0));

        int groupSize = ((JSONArray) group.get(1)).size();

        //Adds group members to group object
        for (int i = 0; i < groupSize; i++){
            g.AddGroupMember((String) ((JSONArray) group.get(1)).get(i));
        }
        int x = 0;


        int numOfEvents = ((JSONArray) group.get(2)).size();
        for (int i = 0; i < numOfEvents; i++){
            int numOfTimeEventHasBeenPlayed = ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).size();
            String name = (String) ((JSONArray)((JSONArray) group.get(2)).get(i)).get(1);
            String type = (String) ((JSONArray)((JSONArray) group.get(2)).get(i)).get(2);
            g.AddGroupEvent(new Event(name,type,true));
            //System.out.println(numOfTimeEventHasBeenPlayed);
            for (int j = 0; j < numOfTimeEventHasBeenPlayed; j++){


                int[] arr = JSONArrayToJavaIntArray((JSONArray) ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).get(j));
                g.getGroupEvents().get(i).inputScores(new Score(type, name, j));

                //Inputs the array into the score object
                g.getGroupEvents().get(i).getScores().get(j).inputScore(arr);
            }

        }
        return g;
    }
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
                int[] score = group.getGroupEvents().get(i).getScores().get(j).getScores();
                ((JSONArray)((JSONArray) eventArray.get(i)).get(0)).add(IntArrayToJsonArray(score));
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

    /*
    * Return a string array of all usernames
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

    /*
    * Return a string array of all group usernames
     */
    public String[] GetGroups() throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject groupHT = (JSONObject) array.get(1);
        System.out.println(groupHT);

        int numOfGroups = groupHT.size();

        String[] strArray = groupHT.keySet().toString().split(", ");
        strArray[0] = strArray[0].substring(1);
        strArray[numOfGroups-1] = strArray[numOfGroups-1].substring(0, strArray[numOfGroups-1].length()-1);

        return strArray;


    }

    public void AddSoloScores(int[] scores, String eventName){

    }

    public void AddGroupScores(int[] scores, String eventName){

    }

    public void AddPersontoGroup(String personsName){

    }

    public void AddGroupEvent(){

    }

    public void AddSoloEvent(){

    }

    public static void main(String[] args) throws IOException, ParseException {
        Database db = new Database();
        Group g = new Group("Golf Group 1");
        g.AddGroupEvent( new Event("Golf1","Front 9",true) );

        g.AddGroupMember("person1");
        g.AddGroupMember("person2");

        g.AddGroupEvent(new Event("Golf2","Back 9",true));
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","Golf1",0));
        g.getGroupEvents().get(0).getScores().get(0).inputScore(new int[]{1,1,1,1,1,1,1,1,1});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","Golf1",0));
        g.getGroupEvents().get(0).getScores().get(1).inputScore(new int[]{1,1,2,3,420,60,69,70,28});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","Golf1",1));
        g.getGroupEvents().get(0).getScores().get(2).inputScore(new int[]{1,1,1,1,1,2,1,1,1});
        g.getGroupEvents().get(0).inputScores(new Score("Back 9","Golf1",1));
        g.getGroupEvents().get(0).getScores().get(3).inputScore(new int[]{1,1,1,1,1,2,420,1,1});

        g.getGroupEvents().get(1).inputScores(new Score("Front 9","Golf1",0));
        g.getGroupEvents().get(1).getScores().get(0).inputScore(new int[]{69,1,2,3,4,5,6,7,8});
        g.getGroupEvents().get(1).inputScores(new Score("Front 9","Golf1",0));
        g.getGroupEvents().get(1).getScores().get(1).inputScore(new int[]{1,1,2,3,420,5,69,7,8});



        db.AddGroup("test",g);
        /*
        AddPerson Tests DO NOT RUN THIS ON database.json, use either databaseTEST.json or make a new .json file
        If you run this on Database.json it will rewrite everything on one line making the file much harder to read for
        the backend team.
         */

        /*
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


        System.out.println(Arrays.toString(Winston.getPersonalEvents().get(0).getScores().get(0).getScores()));
        System.out.println(Arrays.toString(Winston.getPersonalEvents().get(0).getScores().get(1).getScores()));

        System.out.println(Winston.getPersonalEvents().get(0).getScores().get(1));

        db.AddPerson("WinstonS",Winston);

         */

        //GetGroup() Test
        //System.out.println(Arrays.toString(db.GetGroups()));

        //GetPeople() Test
        /*System.out.println(Arrays.toString(db.GetPeople()));*/


        //Person Braeden = db.GetPerson("person1");

        /* Person Class Tests */

        /*
        if (!Braeden.getName().equals("Braeden Kroetsch")){
            System.out.print("Braeden Test 1 returned");
            System.out.println(Braeden.getName());
        }

         */

        /* Event Class tests*/
        /*
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

         */

        /* Golf1 tests (Score class) */

        /*
        if (Braeden.getPersonalEvents().get(0).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Braeden Test 8 should be 0 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(0).getScores().get(0).getEventName().equals("Golf1")){
            System.out.print("Error, Braeden Test 9 should be Golf1 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getEventName());
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

         */


        /* Golf2 tests (Score class) */
        /*
        if (Braeden.getPersonalEvents().get(1).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Braeden Test 18 should be 0 but it is returning");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getEventCounter());
        }

        if (!Braeden.getPersonalEvents().get(1).getScores().get(0).getEventName().equals("Golf2")){
            System.out.print("Error Braeden Test 19 should be Golf2 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getEventName());

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

        if (!Braeden.getPersonalEvents().get(1).getScores().get(1).getEventName().equals("Golf2")){
            System.out.print("Error Braeden Test 23 should be Golf2 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getEventName());

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

        if (!Braeden.getPersonalEvents().get(1).getScores().get(2).getEventName().equals("Golf2")){
            System.out.print("Error Braeden Test 27 should be Golf2 but it is retunring");
            System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getEventName());

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

         */


        /* Group tests (Person Class)*/

        /*
        ArrayList<String> test1 = new ArrayList<>();
        test1.add("group1");
        test1.add("PGA Proz");
        if (!Braeden.getGroups().equals(test1)){
            System.out.println("Error, Braeden Test 30 should be [group1, PGA Proz] but it is returning"
                    + Braeden.getGroups());
        }

        Person M = db.GetPerson("person2");

         */

        // Person Class Tests */

        /*
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

         */

        /*

        // Golf1 tests (Score class)
        if (M.getPersonalEvents().get(0).getScores().get(0).getEventCounter() != 0){
            System.out.print("Error, Mohammed Golfguy Test 4 should be 0 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        }

        if (!M.getPersonalEvents().get(0).getScores().get(0).getEventName().equals("Golf1")){
            System.out.print("Error, Mohammed Golfguy Test 5 should be 0 but it is returning");
            System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getEventName());
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

        if (!M.getPersonalEvents().get(0).getScores().get(1).getEventName().equals("Golf1")){
            System.out.print("Error, Mohammed Golfguy Test 9 should be 0 but it is returning");
            System.out.println(M.getPersonalEvents().get(1).getScores().get(1).getEventName());
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

         */

        //Group tests (Person Class)

        /*
        ArrayList<String> test2 = new ArrayList<>();
        test2.add("group1");
        if (!M.getGroups().equals(test2)){
            System.out.print("Error Mohammed Golfguy Test 12 should be [group1] but it is returning");
            System.out.println(M.getGroups());
        }

        //Someone with no personal events
        Person Hunter = db.GetPerson("person3");
        //Group tests (Person Class)
        */

        /*
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

        if (!group1.getGroupEvents().get(0).getScores().get(0).getEventName().equals("Golf1")){
            System.out.print("Error group1 Test 9 should be Golf1");
            System.out.println(group1.getGroupEvents().get(0).getScores().get(0).getEventName());
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
            System.out.print("Error group1 Test 12 should be [1, 1, 1, 1, 1, 1, 1, 1, 1] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(0).getScores()));
        }

        if (!Arrays.equals(group1.getGroupEvents().get(1).getScores().get(1).getScores(),
                new int[] {32, 34, 36, 835, 31, 33, 35, 343, 33})){
            System.out.print("Error group1 Test 12 should be [32, 34, 36, 835, 31, 33, 35, 343, 33] but it is returning");
            System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(1).getScores()));
        }

        Group proz = db.GetGroup("PGA Proz");
        System.out.println("Unit Testing Complete");

         */



    }
}
