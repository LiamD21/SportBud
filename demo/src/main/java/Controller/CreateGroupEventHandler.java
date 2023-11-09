package Controller;

import Model.Group;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CreateGroupEventHandler extends UIHandler {
    private Group group;
    private String groupName;

    public CreateGroupEventHandler(String gName) {
        super.setDb();
        try {
            group = db.GetGroup(gName);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        this.groupName = gName;
    }

    /**
     * Creates a group event for the current group from the given parameters
     * @param groupName the string name of the group
     * @param eventName the string name for the new event
     * @param eventType the string event type - must match a known event type
     */
    public void createEvent(String groupName, String eventName, String eventType){
        try {
            db.AddGroupEvent(groupName, eventName, eventType);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}


