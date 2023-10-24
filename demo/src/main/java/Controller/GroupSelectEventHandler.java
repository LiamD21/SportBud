package Controller;

import Model.Event;
import Model.Group;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class GroupSelectEventHandler extends UIHandler{
    Group group;
    public GroupSelectEventHandler(String groupName){
        super.setDb();
        try {
            group = db.GetGroup(groupName);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGroupName(){
        return group.getGroupName();
    }

    public ArrayList<Event> getEvents(){
        return group.getGroupEvents();
    }

    public ArrayList<String> getGroupMembers(){
        return group.getPeople();
    }

}
