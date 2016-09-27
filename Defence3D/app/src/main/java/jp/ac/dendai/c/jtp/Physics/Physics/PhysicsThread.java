package jp.ac.dendai.c.jtp.Physics.Physics;

import android.util.Log;

/**
 * Created by wark on 2016/09/24.
 */

public class PhysicsThread extends Thread {
    protected volatile Physics3D physics;
    protected boolean isRun = false;
    protected boolean end = false;
    public boolean isRun(){
        return isRun;
    }
    public PhysicsThread(Physics3D p){
        physics = p;
    }
    public void setEnd(boolean end){
        this.end = end;
    }
    public boolean getEnd(){
        return end;
    }
    @Override
    public void run() {
        long start;
        float diff;
        isRun = true;
        while(!end) {
            start = System.nanoTime();
            physics.simulate();
            diff = (float)(System.nanoTime()- start)/1000000000f;
            //Log.d("Physics Thread",String.format(" time : [%f.5]",diff));
        }
    }
}
