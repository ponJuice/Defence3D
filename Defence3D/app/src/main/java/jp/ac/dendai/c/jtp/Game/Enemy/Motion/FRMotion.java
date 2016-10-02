package jp.ac.dendai.c.jtp.Game.Enemy.Motion;

import android.util.Log;

import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by wark on 2016/09/29.
 */

public class FRMotion extends Motion{
    public int step = 1;
    public int step_count = -1;
    public float delta_x = 5f,delta_y = 0,delta_z = 10f;
    public float moveTime = 1;
    public float interval = 0.5f;
    public boolean reverse = false,x_r = false;
    public int x = 11, z = 5, y = 1;
    protected float timeBuffer;
    protected float clamp_x_s = 1,clamp_x_e = 0;
    @Override
    protected void procAll() {
        int count = 0;

        if(getMoveTime_x() <= 0){

        }
        else if(timeBuffer >= getMoveTime_x() && (reverse || (step_count >= -step && step_count <= 0))) {
            timeBuffer = 0;
            if(x_r)
                step_count++;
            else
                step_count--;

            if(step_count <= -1) {
                Log.d("Motion","step");
                offset_z = offset_z - delta_z;
                //return;
                motionController.nextMotion();
                return;
            }

            if(step_count < -step || step_count > 0){
                if(x_r)
                    step_count--;
                else
                    step_count++;
                x_r = !x_r;
                float t = clamp_x_e;
                clamp_x_e = clamp_x_s;
                clamp_x_s = t;

            }
            Log.d("FRMotion","step_count : "+step_count);
        }
       // Log.d("FRMotion","step count : "+step_count);


        for(int i = step_count; i < z  + step_count; i++){
            float _z;
            if(z <= 1)
                _z = offset_z + delta_z * (i -1);
            else{
                _z = offset_z + delta_z * (i -1);
                _z = _z + delta_z * Clamp.clamp(clamp_x_s, clamp_x_e, getInterval_x(), getMoveTime_x(), timeBuffer);
            }

            for(int j = 0; j < y; j++){
                float _y = offset_y + delta_y * (j - 1);

                for(int k = 0; k < x; k++){
                    if(motionController.objects.length <= count) {
                        updateTimeBuffer();
                        return;
                    }
                    float _x = offset_x + delta_x * (k - 1);
                    //_y = _y - (max_y - min_y)/(float)y * Clamp.clamp(clamp_y_s,clamp_y_e,getInterval_y(),getMoveTime_y(),timeBuffer_y);
                    //_z = _z - (max_z - min_z)/(float)z * Clamp.clamp(clamp_z_s,clamp_z_e,getInterval_z(),getMoveTime_z(),timeBuffer_z);

                    motionController.objects[count].getPos().setX(_x);
                    motionController.objects[count].getPos().setY(_y);
                    motionController.objects[count].getPos().setZ(_z);

                    //Log.d("LRMotion","x,y,z"+_x+" : "+_y+" : "+_z);

                    motionController.objects[count].update();

                    count++;
                }
            }
        }
        updateTimeBuffer();
    }

    protected float getMoveTime_x(){
        return moveTime * motionController.speedCoefficient;
    }
    protected float getInterval_x(){
        return interval * motionController.speedCoefficient;
    }

    protected void updateTimeBuffer(){
        timeBuffer += Time.getDeltaTime();
    }

    @Override
    protected void init(Motion motion) {
        step_count = 0;
        timeBuffer = 0;
        clamp_x_s = 1;
        clamp_x_e = 0;
        offset_x = motion.offset_x;
        offset_y = motion.offset_y;
        offset_z = motion.offset_z;
    }
}
