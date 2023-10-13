package Model;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<Person> people;
    private ArrayList<Event> groupEvents;
    private int groupSize;
    private int eventSize;
    public Group(String name){
        groupSize = 0;
        eventSize = 0;
        this.groupName = name;
        people = new ArrayList<Person>();
        groupEvents = new ArrayList<Event>();
    }

    public void AddGroupMember(Person p){
        if (groupSize == 4){
            throw new RuntimeException("Error, groups cannot be bigger than 4");
        }

        people.add(p);
        groupSize++;
    }

    /*
    * Creates a new event and adds it to the groupEvents list
     */
    public void AddGroupEvent(){
        eventSize++;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public ArrayList<Person> getPeople(){
        return this.people;
    }

    public ArrayList<Event> getGroupEvents(){
        return this.groupEvents;
    }

    public int getGroupSize(){
        return this.groupSize;
    }

    public static void main(String[] args) {
        Group g = new Group("Group 1");
        System.out.println(g.getGroupName());
        System.out.println(g.getGroupSize());
        Person braeden = new Person("Braeden Kroetsch");
        g.AddGroupMember(braeden);
        System.out.println(g.getGroupSize());
        System.out.println(g.getPeople().get(0).getName());
    }
}
