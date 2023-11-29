package Model;

import java.util.ArrayList;

public class Person {
    /*
    * Contains The first and last name seperated by a space
    * Ex "Jane Smith"
    * */
    private String name;
    private ArrayList<Event> personalEvents;
    private ArrayList<String> groups;

    public Person(String name){
        this.name = name;
        personalEvents = new ArrayList<>();
        groups = new ArrayList<>();
    }
    /* Get the name of the person */
    public String getName() {
        return this.name;
    }

    /* Get the groups associated to this person */
    public ArrayList<String> getGroups(){
        return this.groups;
    }

    /* Get the personal events associated to this person */
    public ArrayList<Event> getPersonalEvents(){
        return this.personalEvents;
    }

    /* Add a group to the person */
    public void addGroup(String g){
        groups.add(g);
    }

    /* Add an event to the personal events list */
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
