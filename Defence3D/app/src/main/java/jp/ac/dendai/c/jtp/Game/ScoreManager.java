package jp.ac.dendai.c.jtp.Game;

import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Math.Clamp;

/**
 * Created by Goto on 2016/09/27.
 */

public class ScoreManager {
    protected static int _score = 0;
    public static void init(int score){
        _score = score;
    }
    public static void add(int score){
        _score += score;
    }
    public static void sub(int score){
        _score -= score;
    }
    public static int getScore(){
        return _score;
    }
}
