package jp.ac.dendai.c.jtp.Game.Enemy.Motion;

import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by wark on 2016/09/28.
 */

public class MotionController {
    protected boolean stop = false;
    protected float speedCoefficient = 1;
    protected GameObject[] objects;
    protected Motion now;
    public MotionController(GameObject[] objects){
        this.objects = objects;
    }
    public void setMotion(Motion m){
        m.motionController = this;
        now = m;
    }
    public void setSpeedCoefficient(float c){
        speedCoefficient = c;
    }
    public float getSpeedCoefficient(){
        return speedCoefficient;
    }
    public void setStop(boolean flag){
        stop = flag;
    }
    public boolean getStop(){
        return stop;
    }
    public void procAll(){
        if(!stop && now != null)
            now.procAll();
    }
}
