package Model;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.FileWriter;
import java.io.IOException;

public class JsonTest {

    //JSONParser p;

    /*public static void main(String[] args) {
        System.out.println("Hi Mom");
    }*/

    public static void main(String[] args) {

        JSONArray userArray = new JSONArray();

        // Create and add user objects to the array
        JSONObject user1 = new JSONObject();
        user1.put("username", "Omar");
        user1.put("id", 1);
        userArray.add(user1);

        JSONObject user2 = new JSONObject();
        user2.put("username", "Braeden");
        user2.put("id", 2);
        userArray.add(user2);

        JSONObject user3 = new JSONObject();
        user3.put("username", "John");
        user3.put("id", 3);
        userArray.add(user3);

        JSONObject user4 = new JSONObject();
        user4.put("username", "Liam");
        user4.put("id", 4);
        userArray.add(user4);

        JSONObject user5 = new JSONObject();
        user5.put("username", "Isaac");
        user5.put("id", 5);
        userArray.add(user5);

        // Define the file path where you want to save the JSON file
        String filePath = "users.json";

        try {
            // Write the JSON array to a file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(userArray.toJSONString());
            fileWriter.close();
            System.out.println("JSON file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
