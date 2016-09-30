package jp.ac.dendai.c.jtp.Game.Score;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Goto on 2016/09/29.
 */

public class ScorePacage {
    public ArrayList<Score> scores = new ArrayList<>();

    public void add(Score s){
        scores.add(s);
        sort();
    }

    public void sort(){
        Collections.sort(scores);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int n = 0;n < scores.size();n++){
            sb.append(scores.get(n).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
