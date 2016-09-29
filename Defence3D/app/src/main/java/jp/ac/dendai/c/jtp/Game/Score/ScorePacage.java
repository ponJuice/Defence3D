package jp.ac.dendai.c.jtp.Game.Score;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Goto on 2016/09/29.
 */

public class ScorePacage {
    public ArrayList<Score> scores;

    public void add(Score s){
        scores.add(s);
    }

    public void sort(){
        Collections.sort(scores);
    }
}
