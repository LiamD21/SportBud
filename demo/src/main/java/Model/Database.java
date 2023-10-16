package Model;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.*;
public class Database {
    String filePath = "database.json";
    JSONParser parser;
    JSONArray array;

    public Database() throws FileNotFoundException, ParseException {
        parser = new JSONParser();
    }
    public Person GetPerson(String userName) throws ParseException, IOException {
        JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
        System.out.println(array);

        return null;
    }
    public static void main(String[] args) throws IOException, ParseException {
        Database db = new Database();
        db.GetPerson("person1");
    }
}
