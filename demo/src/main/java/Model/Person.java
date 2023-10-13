package Model;

public class Person {
    /*
    * Contains The first and last name seperated by a space
    * Ex "Jane Smith"
    * */
    private String name;

    public Person(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public static void main(String[] args) {
        Person p = new Person("Braeden Kroetsch");
        System.out.println(p.getName());
    }
}
