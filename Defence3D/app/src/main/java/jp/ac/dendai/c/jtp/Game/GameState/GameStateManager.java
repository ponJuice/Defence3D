package jp.ac.dendai.c.jtp.Game.GameState;

import jp.ac.dendai.c.jtp.Game.Screen.Screenable;

/**
 * Created by Goto on 2016/09/27.
 */

public class GameStateManager {
    protected BaseState now;
    public void draw(){
        now.draw();
    }
    public void touch(){
        now.touch();
    }
    public void proc(){
        proc();
    }
}
