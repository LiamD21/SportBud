package Model;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.json.simple.parser.*;
public class Database {
    String filePath = "database.json";
    JSONParser parser;
    JSONArray array;

    public Database() throws FileNotFoundException, ParseException {
        parser = new JSONParser();
    }

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

        //Number of events
        //System.out.println( ((JSONArray) person.get(1)).size() );
        // Event Name
        //System.out.println( ( ((JSONArray)((JSONArray) person.get(1)).get(x)).get(1)) );
        //Number of times the event has been played
       // System.out.println( ( ((JSONArray)((JSONArray)((JSONArray) person.get(1)).get(x)).get(0))).size() );
        // Event Type
        //System.out.println( ( ((JSONArray)((JSONArray) person.get(1)).get(x)).get(2)) );
       // System.out.println( ((JSONArray)((JSONArray)((JSONArray) person.get(1)).get(x)).get(0)).get(0) );
       // int[] testarray = JSONArrayToJavaIntArray(
            //    (JSONArray) ((JSONArray)((JSONArray)((JSONArray) person.get(1)).get(x)).get(0)).get(0)
      //  );
        //System.out.println(Arrays.toString(testarray));

        int numOfEvents = ((JSONArray) person.get(1)).size();

        for (int i = 0; i < numOfEvents; i++) {
           String name = (String) ((JSONArray)((JSONArray) person.get(1)).get(i)).get(1);
           String type = (String) ((JSONArray)((JSONArray) person.get(1)).get(i)).get(2);
           Boolean isGroup = (Boolean) ((JSONArray)((JSONArray) person.get(1)).get(i)).get(3);
           p.addPersonalEvent(new Event(name,type,isGroup));
           int numOfTimesEventHasBeenPlayed = (((JSONArray)((JSONArray)((JSONArray) person.get(1)).get(i)).get(0))).size();
            for (int j = 0; j < numOfTimesEventHasBeenPlayed; j++){

               //The current score array
               int[] arr = JSONArrayToJavaIntArray(
                       (JSONArray) ((JSONArray)((JSONArray)((JSONArray) person.get(1)).get(i)).get(0)).get(j));

               //Initializes the Score Object
               p.getPersonalEvents().get(i).inputScores(new Score(type,name,j));

               //Inputs the array into the score object
               p.getPersonalEvents().get(i).getScores().get(j).inputScore(arr);
           }
       }


        return p;
    }
    public static void main(String[] args) throws IOException, ParseException {
        Database db = new Database();
        Person Braeden = db.GetPerson("person1");

        /* Person Class Tests */
        System.out.println(Braeden.getName());

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



    }
}
