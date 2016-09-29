package jp.ac.dendai.c.jtp.Game.Enemy.Motion;

import java.util.ArrayList;

import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by wark on 2016/09/28.
 */

public class MotionController {
    protected boolean stop = false;
    protected float speedCoefficient = 0.05f;
    protected GameObject[] objects;
    protected int ite = 0;
    protected ArrayList<Motion> motions;
    protected Motion now;
    public MotionController(GameObject[] objects){
        this.objects = objects;
        motions = new ArrayList<>();
    }
    public void addMotion(Motion m){
        motions.add(m);
        m.motionController=this;
    }
    public void setMotion(Motion m){
        m.motionController = this;
        now = m;
    }
    public void nextMotion(){
        ite++;
        if(ite >= motions.size()){
            ite = 0;
        }
        Motion t = now;
        now = motions.get(ite);
        now.init(t);
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
