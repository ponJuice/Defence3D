package jp.ac.dendai.c.jtp.Game.Enemy.Invader;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemysState;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by Goto on 2016/09/22.
 */

public class InvaderFrontMoveState extends EnemysState {
    private float buffer = 0;
    private float moveTime;
    private float def_z_pos;
    private int column_size;
    private float count = 0;
    private StateListener listener;
    public InvaderFrontMoveState(int column_size,float moveTime,float def_z_pos){
        this.moveTime = moveTime;
        this.def_z_pos = def_z_pos;
        this.column_size = column_size;
    }
    public void setStateListener(StateListener sl){
        listener = sl;
    }
    @Override
    public void init(){
        buffer = 0;
    }
    @Override
    public void procAll(GameObject[] enemys) {
        if(buffer >= moveTime){
            buffer = 0;
            count++;
            if(listener != null)
                listener.call(this);
        }else{
            buffer += Time.getDeltaTime();
            for(int n = 0;n < enemys.length;n ++ ){
                proc(enemys[n],n);
            }
        }

    }

    public void proc(GameObject enemy,int n){
        float _n = Clamp.clamp(0f,1f,moveTime,buffer);//(float)(n / column_size) + ;
        float temp = 50f + (float)(n / 11) * 15f - count * 5f;
        float def_pos = temp - _n  * 5f;

        //float def_pos = enemy.getPos().getZ() - _n;

        enemy.getPos().setZ(def_pos);
        enemy.update();
    }
}
