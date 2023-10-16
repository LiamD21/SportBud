package Model;

import java.util.ArrayList;
/* Groups can have multiple types of events */
public class Group {
    private String groupName;
    /* References the every person in the group */
    private ArrayList<Person> people;
    private ArrayList<Event> groupEvents;
    public Group(String name){
        this.groupName = name;
        people = new ArrayList<Person>();
        groupEvents = new ArrayList<Event>();
    }

    /*
    * Adds a group member to the people arraylist.
    * Do NOT call from here, use the person class.
     */
    public void AddGroupMember(Person p){
        if (people.size() == 4){
            throw new RuntimeException("Error, groups cannot be bigger than 4");
        }

        people.add(p);
    }

    /*
    * adds an event to the groupEvents list
     */
    public void AddGroupEvent(Event e){
        if (!e.getIsGroup())
            throw new RuntimeException("Error trying to add a personal event to the group event arraylist");
        this.groupEvents.add(e);
    }

    /*
    * Returns the name of the group
     */
    public String getGroupName(){
        return this.groupName;
    }

    /*
    Returns the arraylist containing the people in this group
     */
    public ArrayList<Person> getPeople(){
        return this.people;
    }

    /*
    * Returns the arraylist of the events that are in this group
     */
    public ArrayList<Event> getGroupEvents(){
        return this.groupEvents;
    }

    public int getGroupSize(){
        return this.people.size();
    }

    /*
    public static void main(String[] args) {
        Group g = new Group("Group 1");
        System.out.println(g.getGroupName());
        System.out.println(g.getGroupSize());
        Person braeden = new Person("Braeden Kroetsch");
        g.AddGroupMember(braeden);
        System.out.println(g.getGroupSize());
        System.out.println(g.getPeople().get(0).getName());
    }

     */
}
