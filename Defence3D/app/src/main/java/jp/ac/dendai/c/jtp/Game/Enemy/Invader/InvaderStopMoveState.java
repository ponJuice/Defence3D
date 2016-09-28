package jp.ac.dendai.c.jtp.Game.Enemy.Invader;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemysState;
import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by wark on 2016/09/24.
 */

public class InvaderStopMoveState extends EnemysState {
    @Override
    public void procAll(GameObject[] enemys) {
        for(int n = 0;n < enemys.length;n++){
            enemys[n].update();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void setMoveTime(float time) {

    }

    @Override
    public float getMoveTime() {
        return 0;
    }
}
