package jp.ac.dendai.c.jtp.Game.Enemy.Invader;

import android.support.v7.widget.LinearLayoutCompat;

import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemysState;
import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by Goto on 2016/09/22.
 */

public class InvaderState {
    public enum INVADERSTATE{
        Move_LR,
        Move_Frond
    }
    private int count;
    private INVADERSTATE state;
    private InvaderLRMoveState lr_move;
    private InvaderFrontMoveState front_move;
    private InvaderStopMoveState stop_move;
    private EnemysState now;
    private GameObject[] target;
    public InvaderState(GameObject[] t){
        target = t;
        lr_move = new InvaderLRMoveState(this,6,1);
        lr_move.setStateListener(new StateListener() {
            @Override
            public void call(EnemysState es) {
                state = INVADERSTATE.Move_Frond;
                now = front_move;
                now.init();
            }
        });
        front_move = new InvaderFrontMoveState(11,1,50f);
        front_move.setStateListener(new StateListener() {
            @Override
            public void call(EnemysState es) {
                state = INVADERSTATE.Move_LR;
                now = lr_move;
                now.init();
            }
        });
        stop_move = new InvaderStopMoveState();
        now = lr_move;
    }
    public void changeState(INVADERSTATE state){
        if(state == INVADERSTATE.Move_LR)
            now = lr_move;
        else if(state == INVADERSTATE.Move_Frond)
            now = front_move;
    }
    public void proc(){
        now.procAll(target);
    }
    public EnemysState getState(){
        return now;
    }
    public void setState(EnemysState es){
        now = es;
    }
}
