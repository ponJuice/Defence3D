package jp.ac.dendai.c.jtp.Game.Enemy.Motion;

/**
 * Created by wark on 2016/09/28.
 */

public abstract class Motion {
    public float offset_x = 0,offset_y = 0,offset_z = 0;
    protected MotionController motionController;
    protected abstract void procAll();
    protected abstract void init(Motion motion);
}
