package Controller;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void handleCreatedGroup(ObservableList<String> createdGroup){
        //TODO Connect the handler to the model here to create a new group

    }

}
