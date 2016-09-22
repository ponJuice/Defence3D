package jp.ac.dendai.c.jtp.Game.Enemy;

import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by Goto on 2016/09/22.
 */

public abstract class EnemysState {
    public abstract void procAll(GameObject[] enemys);
    public abstract void init();
}
