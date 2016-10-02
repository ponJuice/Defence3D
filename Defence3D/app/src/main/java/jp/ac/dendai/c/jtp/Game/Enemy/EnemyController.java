package jp.ac.dendai.c.jtp.Game.Enemy;

import android.media.SoundPool;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.defence3d.R;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by wark on 2016/10/02.
 */

public class EnemyController {
    protected SoundPool sp;
    protected int cannon,explode;

    public void onPause(){
        if(sp != null)
            sp.release();
    }

    public void onResume(){
        if(sp == null)
            sp = new SoundPool(5,STREAM_MUSIC,0);
        explode = sp.load(GameManager.getAct(), R.raw.small_explosion3,0);
        cannon = sp.load(GameManager.getAct(),R.raw.cannon1,0);
    }

    public void startCannon(float left_v,float right_v,int roop,float rate){
        sp.play(cannon,left_v,right_v,0,roop,rate);
    }

    public void startExplode(float left_v,float right_v,int roop,float rate){
        sp.play(explode,left_v,right_v,0,roop,rate);
    }
}
