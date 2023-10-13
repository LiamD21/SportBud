package Model;

import java.util.ArrayList;

public class Person {
    /*
    * Contains The first and last name seperated by a space
    * Ex "Jane Smith"
    * */
    private String name;

    /*
    * A list of all their personal events
     */
    private ArrayList<Event> personalEvents;

    /*
     * A list of all their group events
     */
    private ArrayList<Group> groups;

    /*
    * To be replaced with opening a JSON file later
     */
    public Person(String name){
        this.name = name;
        personalEvents = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Group> getGroupEvents(){
        return this.groups;
    }

    public ArrayList<Event> getPersonalEvents(){
        return this.personalEvents;
    }

    public void addGroup(Group g){
        groups.add(g);
    }

    /*
    * Creates a new event and adds it to the personal events list
     */
    public void addPersonalEvent(){

    }

    public static void main(String[] args) {
        Person p = new Person("Braeden Kroetsch");
        System.out.println(p.getName());
    }
}
