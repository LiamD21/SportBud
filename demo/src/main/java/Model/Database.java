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
    private String filePath = "database.json";
    private JSONParser parser;
    JSONArray array;

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


    public Person GetPerson(String userName) throws ParseException, IOException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);
        JSONArray person = (JSONArray) personHT.get(userName);
        Person p = new Person((String) person.get(0));

        int x = 0;

        int numOfEvents = ((JSONArray) person.get(1)).size();
        int numOfGroups = ((JSONArray) person.get(2)).size();

        if (((JSONArray) person.get(1)).get(0) != null) {
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
        if (((JSONArray) person.get(2)).get(0) != null) {
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


        int numOfEvents = ((JSONArray)((JSONArray)((JSONArray) group.get(2)))).size();
        for (int i = 0; i < numOfEvents; i++){
            int numOfTimeEventHasBeenPlayed = ((JSONArray)((JSONArray)((JSONArray)((JSONArray) group.get(2))).get(i)).get(0)).size();
            String name = (String) ((JSONArray)((JSONArray)((JSONArray)((JSONArray) group.get(2)))).get(i)).get(1);
            String type = (String) ((JSONArray)((JSONArray)((JSONArray)((JSONArray) group.get(2)))).get(i)).get(2);
            g.AddGroupEvent(new Event(name,type,true));
            //System.out.println(numOfTimeEventHasBeenPlayed);
            for (int j = 0; j < numOfTimeEventHasBeenPlayed; j++){


                int[] arr = JSONArrayToJavaIntArray((JSONArray) ((JSONArray)((JSONArray)((JSONArray) group.get(2)).get(i)).get(0)).get(j));

                //Initializes score object loop required to ensure correct person gets put into the score object
                for (int k = 0; k < groupSize; k++) {
                    g.getGroupEvents().get(i).inputScores(new Score(type, ((String) ((JSONArray) group.get(1)).get(k)), j));
                }

                //Inputs the array into the score object
                g.getGroupEvents().get(i).getScores().get(j).inputScore(arr);
            }

        }
        return g;
    }
    public void AddPerson(Person person){

    }

    public void AddGroup(Group group){

    }

    /*
    * Return a string array of all usernames
     */
    public String[] GetPeople() throws IOException, ParseException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        JSONObject personHT = (JSONObject) array.get(0);
        int numOfPeople = personHT.size();

        String[] strArray = personHT.keySet().toString().split(", ");
        System.out.println(Arrays.toString(strArray));





        return null;
    }

    /*
    * Return a string array of all group usernames
     */
    public String GetGroups(){
        return null;
    }

    public void AddSoloScores(int[] scores, String eventName){

    }

    public void AddGroupScores(int[] scores, String eventName){

    }

    public void AddPersontoGroup(String personsName){

    }

    public static void main(String[] args) throws IOException, ParseException {
        Database db = new Database();
        db.GetPeople();

        Person Braeden = db.GetPerson("person1");

        /* Person Class Tests */
        /*System.out.println(Braeden.getName());*/

        /* Event Class tests*/
        /*
        System.out.println(Braeden.getPersonalEvents().get(0).getEventName());
        System.out.println(Braeden.getPersonalEvents().get(0).getEventType());
        System.out.println(Braeden.getPersonalEvents().get(0).getIsGroup() + "\n");

        System.out.println(Braeden.getPersonalEvents().get(1).getEventName());
        System.out.println(Braeden.getPersonalEvents().get(1).getEventType());
        System.out.println(Braeden.getPersonalEvents().get(1).getIsGroup()+"\n");

         */

        /* Golf1 tests (Score class) */
        /*
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(0).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(0).getScores())+"\n");

        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(1).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(1).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(1).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(1).getScores())+"\n");

        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(2).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(2).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(0).getScores().get(2).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(0).getScores().get(2).getScores())+"\n");

         */

        /* Golf2 tests (Score class) */
        /*
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(0).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(0).getScores())+"\n");

        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(1).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(1).getScores())+"\n");

        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getEventCounter());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getPersonsName());
        System.out.println(Braeden.getPersonalEvents().get(1).getScores().get(2).getType());
        System.out.println(Arrays.toString(Braeden.getPersonalEvents().get(1).getScores().get(2).getScores())+"\n");

         */

        /* Group tests (Person Class)*/
        /*System.out.println(Braeden.getGroups());*/

        Person M = db.GetPerson("person2");
        /* Person Class Tests */
        //System.out.println(M.getName());


        /* Event Class tests*/
        /*
        System.out.println(M.getPersonalEvents().get(0).getEventName());
        System.out.println(M.getPersonalEvents().get(0).getEventType());
        System.out.println(M.getPersonalEvents().get(0).getIsGroup() + "\n");

         */

        /* Golf1 tests (Score class) */
        /*
        System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getEventCounter());
        System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getPersonsName());
        System.out.println(M.getPersonalEvents().get(0).getScores().get(0).getType());
        System.out.println(Arrays.toString(M.getPersonalEvents().get(0).getScores().get(0).getScores())+"\n");

        System.out.println(M.getPersonalEvents().get(0).getScores().get(1).getEventCounter());
        System.out.println(M.getPersonalEvents().get(0).getScores().get(1).getPersonsName());
        System.out.println(M.getPersonalEvents().get(0).getScores().get(1).getType());
        System.out.println(Arrays.toString(M.getPersonalEvents().get(0).getScores().get(1).getScores())+"\n");



         */

        /* Group tests (Person Class)*/
        /*System.out.println(M.getGroups());*/

        //Someone with no personal events
        Person Hunter = db.GetPerson("person3");
        /* Group tests (Person Class)*/
        /*System.out.println(Hunter.getGroups());*/
        //System.out.println(Hunter.getPersonalEvents());

        //Someone with no groups
        Person Jane = db.GetPerson("person4");

        //Score Class Tests
        /*System.out.println(Jane.getPersonalEvents().get(0).getEventName());
        System.out.println(Arrays.toString(Jane.getPersonalEvents().get(0).getScores().get(0).getScores()));
        System.out.println(Arrays.toString(Jane.getPersonalEvents().get(0).getScores().get(1).getScores()));
         */

        //Group tests
        /*
        Jane.addGroup("gi");
        System.out.println(Jane.getGroups());
        */

        //GetGroup() tests
        Group group1 = db.GetGroup("group1");
        /*
        System.out.println(group1.getGroupName());
        System.out.println(db.GetPerson(group1.getPeople().get(0)).getName());
        System.out.println(db.GetPerson(group1.getPeople().get(1)).getName());
        System.out.println(group1.getGroupSize() + "\n");
        System.out.println(group1.getGroupEvents().get(0).getEventName());
        System.out.println(group1.getGroupEvents().get(0).getEventType());
        System.out.println(group1.getGroupEvents().get(0).getIsGroup());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(0).getScores()));
        System.out.println(group1.getGroupEvents().get(0).getScores().get(0).getPersonsName());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(1).getScores()));
        System.out.println(group1.getGroupEvents().get(0).getScores().get(1).getPersonsName());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(2).getScores()));
        System.out.println(group1.getGroupEvents().get(0).getScores().get(2).getPersonsName());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(0).getScores().get(3).getScores()));
        System.out.println(group1.getGroupEvents().get(0).getScores().get(3).getPersonsName()+"\n");


        System.out.println(group1.getGroupEvents().get(1).getEventName());
        System.out.println(group1.getGroupEvents().get(1).getEventType());
        System.out.println(group1.getGroupEvents().get(1).getIsGroup());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(0).getScores()));
        System.out.println(group1.getGroupEvents().get(1).getScores().get(0).getPersonsName());
        System.out.println(Arrays.toString(group1.getGroupEvents().get(1).getScores().get(1).getScores()));
        System.out.println(group1.getGroupEvents().get(1).getScores().get(1).getPersonsName());

         */

        Group proz = db.GetGroup("PGA Proz");
        //Test to make sure the people are assigned their respective scores in groups of 3
        /*
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(0).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(0).getPersonsName());
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(1).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(1).getPersonsName());
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(2).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(2).getPersonsName());
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(3).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(3).getPersonsName());
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(4).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(4).getPersonsName());
        System.out.println(Arrays.toString(proz.getGroupEvents().get(0).getScores().get(5).getScores()));
        System.out.println(proz.getGroupEvents().get(0).getScores().get(5).getPersonsName());
        System.out.println(proz.getGroupName());
        System.out.println(proz.getGroupSize());

         */

    }
}
