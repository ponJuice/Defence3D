package jp.ac.dendai.c.jtp.Game.GameState;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemyObserver;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemysState;
import jp.ac.dendai.c.jtp.Game.Enemy.Motion.MotionController;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;

/**
 * Created by wark on 2016/09/28.
 */

public class EndlessModeState extends GameState{
    public enum BORDER_STATE{
        NON,
        CAUTION,
        DANGER
    }
    protected BORDER_STATE border_state = BORDER_STATE.NON;
    protected GameObject[] enemys;
    protected Player player;
    protected float border_line = 5f;
    protected float caution_line = 10f;
    protected MotionController motionController;
    protected float speed1 = 0.5f,speed2 = 0.1f;
    public EndlessModeState(MotionController mo,Player player){
        super(GAME_STATE.PLAYING);
        motionController = mo;
        //motionController.setSpeedCoefficient(0);
        this.player = player;
    }

    public BORDER_STATE getBorderState(){
        return border_state;
    }

    public void setEnemys(GameObject[] es){
        enemys = es;
    }

    public void setBorderline(float line){
        border_line = line;
    }

    @Override
    public void proc() {
        //終了条件
        //一定ラインまで敵が来る
        //敵が全員いなくなる
        boolean clear_flag = true;
        int deadCount = 0;
        border_state = BORDER_STATE.NON;
        for(int n = 0;n < enemys.length;n++) {
            if(!enemys[n].getPhysicsObject().freeze) {
                clear_flag = false;
                if (enemys[n].getPos().getZ() <= border_line && getState() != GAME_STATE.GAMEOVER) {
                    changeState(GAME_STATE.GAMEOVER);
                }
                if(enemys[n].getPos().getZ() <= caution_line && getState() != GAME_STATE.GAMEOVER){
                    border_state = BORDER_STATE.CAUTION;
                }
            }else if(enemys[n].getPhysicsObject().freeze)
                deadCount++;
        }
        if(player.getHp() <= 0){
            changeState(GAME_STATE.GAMEOVER);
        }
        if(clear_flag && getState() != GAME_STATE.PAUSE)
            changeState(GAME_STATE.CLEAR);
        if(deadCount > enemys.length / 4 * 3){
            //motionController.setSpeedCoefficient(speed2);
        }else if(deadCount > enemys.length/2) {
            //motionController.setSpeedCoefficient(speed1);
        }
    }
}
