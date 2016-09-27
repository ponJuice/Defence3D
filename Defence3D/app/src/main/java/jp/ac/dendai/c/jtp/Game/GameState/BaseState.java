package jp.ac.dendai.c.jtp.Game.GameState;

/**
 * Created by Goto on 2016/09/27.
 */

public abstract class BaseState {
    protected GameStateManager gsm;
    public abstract void draw();
    public abstract void touch();
    public abstract void proc();
}
