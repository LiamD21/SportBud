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
    private ArrayList<String> groups;

    public Person(String name){
        this.name = name;
        personalEvents = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getGroups(){
        return this.groups;
    }

    public ArrayList<Event> getPersonalEvents(){
        return this.personalEvents;
    }

    public void addGroup(String g){
        groups.add(g);
    }

    /*
    * add an event to the personal events list
     */
    public void addPersonalEvent(Event e){
        this.personalEvents.add(e);
    }

    /*
    public static void main(String[] args) {
        Person p = new Person("Braeden Kroetsch");
        System.out.println(p.getName());
    }

     */
}
