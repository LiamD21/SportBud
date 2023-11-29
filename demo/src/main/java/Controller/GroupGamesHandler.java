package Controller;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GroupGamesHandler extends UIHandler {
    public GroupGamesHandler(){
        super.setDb();
    }
    public String[] getGroupList(){
        try {
            return db.GetGroups();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean dbHasPeople(){
        try {
            db.GetPeople();
        } catch (IOException | ParseException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
