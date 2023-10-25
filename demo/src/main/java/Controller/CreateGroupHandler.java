package Controller;

import Model.Group;
import javafx.collections.ObservableList;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CreateGroupHandler extends UIHandler{

    public CreateGroupHandler(){
        super.setDb();
    }

    public String[] getPersonList(){
        try {
            return db.GetPeople();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int handleCreatedGroup(ObservableList<String> createdGroup, String groupname) throws IOException, ParseException {
        //TODO Connect the handler to the model here to create a new group
        Group newGroup = new Group(groupname);
        try {
            //for each person in the group add the person to the new created group object
            for (String each :createdGroup) {
                newGroup.AddGroupMember(each);
            }

            //write that group into the database
            db.AddGroup(groupname, newGroup);
            return 1;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);

        }

    }
}
