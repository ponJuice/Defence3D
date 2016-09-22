package jp.ac.dendai.c.jtp.Game.Enemy.Invader;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemysState;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by Goto on 2016/09/22.
 */

public class InvaderLRMoveState extends EnemysState {
    private InvaderState state;
    private int count = 3;
    private int sign = 1;
    private int moveCount;
    private float moveTime;
    private float buffer = 0;
    private StateListener sl;
    public InvaderLRMoveState(InvaderState is,int moveCount,float moveTime){
        state = is;
        this.moveCount = moveCount;
        this.moveTime = moveTime;
    }
    public void setStateListener(StateListener sl){
        this.sl = sl;
    }
    @Override
    public void init(){
        buffer = 0;
    }
    @Override
    public void procAll(GameObject[] enemys) {
        if(buffer >= moveTime){
            buffer = 0;
            if(count >= moveCount || count <= 0) {
                if(count <= 0 && sl != null)
                    sl.call(this);
                sign *= -1;
            }
            count += sign;
        }else{
            buffer += Time.getDeltaTime();
        }
        for (int n = 0; n < enemys.length; n++) {
            proc(enemys[n],n);
        }
    }

    public int getMoveCount(){
        return moveCount;
    }

    public float getMoveTime(){
        return moveTime;
    }

    private void proc(GameObject enemy,int n){
        float def_pos = (float)n % 11 * 2f - 11f;
        float count_clamp = count;
        if(sign > 0){
            count_clamp = (float)count - Clamp.clamp(1f,0f,0.5f,buffer);
        }else{
            count_clamp = (float)count - Clamp.clamp(-1,0,0.5f,buffer);
        }
        def_pos = def_pos + (count_clamp - moveCount/2) * 2f;
        enemy.getPos().setX(def_pos);
    }
}
