package jp.ac.dendai.c.jtp.Game.Enemy.Motion;

import android.util.Log;

import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by wark on 2016/09/29.
 */

public class AbsoluteStepMotion extends Motion{
    public float offset_x = 0,offset_y = 0,offset_z = 0;
    public int step_x = 5,step_y = 0,step_z = 0;
    public boolean x_reverse = true,y_reverse = false,z_reverse = false;
    public float moveTime_x = 5f,moveTime_y = -1f,moveTime_z = 0f;
    public float interval_x = 1f,interval_y = 0f,interval_z = 0f;
    public float min_x = 0,max_x = 30f,min_y = 0,max_y = 0f,min_z = 0,max_z = 50f;
    public int x = 11, z = 5, y = 1;
    protected float timeBuffer_x,timeBuffer_y,timeBuffer_z;
    protected int stepCount_x,stepCount_y,stepCount_z;
    protected boolean x_r = false,y_r = false,z_r = false;
    protected float clamp_x_s = 1,clamp_x_e = 0,clamp_y_s = 1,clamp_y_e = 0,clamp_z_s = 1,clamp_z_e = 0;
    protected void init(){
        stepCount_x = 0;
        stepCount_y = 0;
        stepCount_z = 0;

    }
    @Override
    protected void procAll() {
        int count = 0;
        if(getMoveTime_x() <= 0){

        }
        else if(timeBuffer_x >= getMoveTime_x() && (x_reverse || (stepCount_x >= 0 && stepCount_x < step_x))) {
            timeBuffer_x = 0;
            if(x_r)
                stepCount_x--;
            else
                stepCount_x++;

            if(stepCount_x < 0 || stepCount_x >= step_x){
                if(x_r)
                    stepCount_x++;
                else
                    stepCount_x--;
                x_r = !x_r;
                float t = clamp_x_e;
                clamp_x_e = clamp_x_s;
                clamp_x_s = t;
            }
        }

        if(getMoveTime_y() <= 0){
        }
        else if(timeBuffer_y >= getMoveTime_y() && stepCount_y < step_y){
            timeBuffer_y = 0;
            stepCount_y++;
        }

        if(getMoveTime_z() <= 0){
            timeBuffer_z = 0;
            clamp_z_e = 0;
            clamp_z_s = 0;
        }
        else if(timeBuffer_z >= getMoveTime_z() && stepCount_z < step_z){
            timeBuffer_z = 0;
            stepCount_z++;
        }


        for(int i = stepCount_z; i < z +stepCount_z; i++){
            float _z;
            if(z <= 1)
                _z = offset_z;
            else {
                _z = offset_z + (max_z - min_z) / (float) (z - 1) * i;
                _z = _z - (max_z - min_z)/(float)z * Clamp.clamp(clamp_z_s,clamp_z_e,getInterval_z(),getMoveTime_z(),timeBuffer_z);
            }

            for(int j = stepCount_y; j < y +stepCount_y; j++){
                float _y;
                if(y <= 1)
                    _y = offset_y;
                else {
                    _y = offset_y + (max_y - min_y) / (float) (y - 1) * j;
                    _y = _y - (max_y - min_y) / (float) y * Clamp.clamp(clamp_y_s, clamp_y_e, getInterval_y(), getMoveTime_y(), timeBuffer_y);
                }

                for(int k = stepCount_x; k < x +stepCount_x; k++){
                    if(motionController.objects[count] == null) {
                        updateTimeBuffer();
                        return;
                    }
                    float _x;
                    if(x <= 1)
                        _x = offset_x;
                    else {
                        _x = offset_x + (max_x - min_x) / (float) (x - 1) * k;
                        _x = _x - (max_x - min_x) / (float)(x - 1) * Clamp.clamp(clamp_x_s, clamp_x_e, getInterval_x(), getMoveTime_x(), timeBuffer_x);
                    }
                    //_y = _y - (max_y - min_y)/(float)y * Clamp.clamp(clamp_y_s,clamp_y_e,getInterval_y(),getMoveTime_y(),timeBuffer_y);
                    //_z = _z - (max_z - min_z)/(float)z * Clamp.clamp(clamp_z_s,clamp_z_e,getInterval_z(),getMoveTime_z(),timeBuffer_z);

                    motionController.objects[count].getPos().setX(_x);
                    motionController.objects[count].getPos().setY(_y);
                    motionController.objects[count].getPos().setZ(_z);

                    motionController.objects[count].update();

                    count++;
                }
            }
        }
        updateTimeBuffer();
    }

    protected float getMoveTime_x(){
        return moveTime_x * motionController.speedCoefficient;
    }
    protected float getMoveTime_y(){
        return moveTime_y * motionController.speedCoefficient;
    }
    protected float getMoveTime_z(){
        return moveTime_z * motionController.speedCoefficient;
    }
    protected float getInterval_x(){
        return interval_x * motionController.speedCoefficient;
    }
    protected float getInterval_y(){
        return interval_y * motionController.speedCoefficient;
    }
    protected float getInterval_z(){
        return interval_z * motionController.speedCoefficient;
    }

    protected void updateTimeBuffer(){
        timeBuffer_x += Time.getDeltaTime();
        timeBuffer_y += Time.getDeltaTime();
        timeBuffer_z += Time.getDeltaTime();
    }
}
