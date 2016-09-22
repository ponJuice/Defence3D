package jp.ac.dendai.c.jtp.Game.Enemy;

import jp.ac.dendai.c.jtp.Game.Enemy.Invader.InvaderState;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by Goto on 2016/09/22.
 */

public class EnemyObserver {
    private GameObject[] enemys;
    private InvaderState is;
    public EnemyObserver(GameObject[] list){
        this.enemys = list;
        is = new InvaderState(list);
    }
    public void procAll(){
        is.proc();
    }
}
