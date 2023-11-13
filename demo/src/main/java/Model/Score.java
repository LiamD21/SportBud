package Model;
/*
* Represents the score from ONE TIME that the event has been played and for ONE PERSON
 */
public class Score {
    private String personsName;
    /*
    * Represents the time that the event was played (eg 1st time, 69th time, etc)
     */
    private int eventCounter;
    private String type;
    private int[] scores;

    public Score(String type, String name, int counter){
        this.type = type;
        this.eventCounter = counter;
        this.personsName = name;

        if (type.equals("Front 9") || type.equals("Back 9"))
            scores = new int[9];
        else if (type.equals("18"))
            scores = new int[18];
        else
            scores = new int[1];
    }


    public void inputScore(int[] inputScores){
        if (inputScores.length != this.scores.length)
            throw new RuntimeException("Array Sizes do not match in inputScores()");
        /* This needs to be tested */
        this.scores = inputScores.clone();
    }
    public String getPersonsName(){
        return this.personsName;
    }

    public int getEventCounter(){
        return this.eventCounter;
    }

    public String getType(){
        return this.type;
    }

    public int[] getScores() {
        return scores;
    }
}
