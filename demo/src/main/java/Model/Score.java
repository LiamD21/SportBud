package Model;
/*
* Represents the score from ONE TIME that the event has been played and for ONE PERSON
 */
public class Score {
    private String type;
    private int[] scores;

    public Score(String type){
        this.type = type;

        if (type.equals("Front 9") || type.equals("Back 9"))
            scores = new int[9];
        else if (type.equals("18"))
            scores = new  int[18];
        else
            throw new RuntimeException("Invalid Type");
    }
}
