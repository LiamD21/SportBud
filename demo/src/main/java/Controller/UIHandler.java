package Controller;

import Model.Database;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;

public class UIHandler {

    /*
    Reference to the database
    */
    Database db;

    /**
     * When the system is initialized, and we enter the solo games page, this is called to get the reference to the database
     */
    public void setDb() {
        try {
            this.db = new Database();
        } catch (FileNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
