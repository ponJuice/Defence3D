package jp.ac.dendai.c.jtp.Game.GameState;

/**
 * Created by wark on 2016/09/28.
 */

public abstract class GameState {
    public enum GAME_STATE{
        PLAYING,
        PAUSE,
        GAMEOVER,
        CLEAR,
        SHOP
    }
    protected StateChangeListener listener;
    private static GAME_STATE state;
    public abstract void proc();
    public void changeState(GAME_STATE state){
        if(listener != null)
            listener.changeState(state);
        this.state = state;
    }
    public static GAME_STATE getState(){
        return state;
    }
    public GameState(GAME_STATE init){
        state = init;
    }
    public void setChangeStateListener(StateChangeListener scl){
        listener = scl;
    }
}
