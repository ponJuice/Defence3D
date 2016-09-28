package jp.ac.dendai.c.jtp.SlopeUtil;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by wark on 2016/09/27.
 */

public class SlopeUtil {
    protected static Activity _act;
    protected static SensorManager manager;
    protected static int num = 17;
    protected static float[] rotationMatrix = new float[9];
    protected static float[][] gravity;
    protected static float[][] geomagnetic;
    protected static float[][] attitude_buff;
    protected static float[][] attitude_buff_2;
    protected static float[][] attitude_buff_3;
    protected static float[] gravity_ave = new float[3];
    protected static float[] geomagnetic_ave = new float[3];
    protected static float[] attitude = new float[3];
    protected static float[] correct_rot = new float[3];
    protected static float[] sensitivity = new float[3];
    protected static boolean enabled = true;

    public static void init(Activity act){
        _act = act;
        sensitivity[0] = 1;
        sensitivity[1] = 1;
        sensitivity[2] = 1;
        gravity = new float[num][3];
        geomagnetic= new float[num][3];
        attitude_buff = new float[num][3];
        attitude_buff_2 = new float[num][3];
        attitude_buff_3 = new float[num][3];
        medianBuffer = new float[num];
    }

    public static void onCreate(){
        manager = (SensorManager)_act.getSystemService(SENSOR_SERVICE);
    }

    public static void onPause(SensorEventListener sel){
        // Listenerの登録解除
        manager.unregisterListener(sel);
    }

    public static void onResume(SensorEventListener sel){
        // Listenerの登録
        manager.registerListener(sel,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(sel,manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(sel,manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public static void onSensorChanged(SensorEvent event){
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic_ave = event.values.clone();
                //geomagnetic_ave[0] *= sensitivity[0];
                //geomagnetic_ave[1] *= sensitivity[1];
                //geomagnetic_ave[2] *= sensitivity[2];
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity_ave = event.values.clone();
                gravity_ave[0] *= sensitivity[0];
                gravity_ave[1] *= sensitivity[1];
                gravity_ave[2] *= sensitivity[2];
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                if(enabled)
                    attitude = event.values.clone();
                break;
        } if(geomagnetic != null && gravity != null){
            //calcAve(geomagnetic,geomagnetic_ave);
            //calcAve(gravity,gravity_ave);
            //RCFilter(geomagnetic,geomagnetic_ave);
            //RCFilter(gravity,gravity_ave);
            //SensorManager.getRotationMatrix( rotationMatrix, null, gravity_ave, geomagnetic_ave);
            //SensorManager.getOrientation( rotationMatrix, attitude_buff[0]);
            //RCFilter5(attitude_buff,attitude_buff_2[0]);
            //median(attitude_buff,attitude_buff_2[0]);
            //median(attitude_buff,attitude);
            //movingAve(attitude_buff_2,attitude);
            //movingAve(attitude_buff,attitude);
           // median(attitude_buff,attitude_buff_2[0]);
            //RCFilter5(attitude_buff_2,attitude_buff_3[0]);
            //movingAve(attitude_buff,attitude);

        }
    }

    public static boolean isEnabled(){
        return enabled;
    }

    public static void enabled(boolean flag){
        enabled = flag;
    }

    public static void movingAve(float[][] in,float[] out){
        int n = 0;
        for(;n < 3;n++){
            out[n] = 0;
            for(int m = num-1;m >= 0;m--){
                out[n] += in[m][n] / num;
                if(m != 0)
                    in[m][n] = in[m-1][n];
            }
        }
    }

    protected static float[] medianBuffer;

    public static void median(float[][] in,float[] out){
        int n = 0;
        for(;n < 3;n++){

            for(int j = num-1;j >= 0;j--){
                medianBuffer[j] = in[j][n];
                if(j != 0){
                    in[j][n] = in[j - 1][n];
                }
            }
            for (int j = 1; j < num; j++) {
                float key = medianBuffer[j];
                int i = j - 1;
                while (i >= 0 && medianBuffer[i] > key) {
                    medianBuffer[i + 1] = medianBuffer[i];
                    i--;
                }
                medianBuffer[i + 1] = key;
            }
            if(num % 2 != 0)
                out[n] = medianBuffer[num/2];
            else{
                out[n] = (medianBuffer[num/2] + medianBuffer[num/2+1])/2f;
            }
        }
    }

    public static void RCFilter3(float[][] in,float[] out){
        //a * ひとつ前の出力値 ＋ (1-a) * センサの値
        int n = 0;
        for(;n < 3;n++){
            out[n] = 0;
            out[n] = in[2][n] * 0.6f + in[1][n] * 0.3f + in[0][n] * 0.1f;
            in[2][n] = in[1][n];
            in[1][n] = in[0][n];
        }
    }

    public static void RCFilter5(float[][] in,float[] out){
        //a * ひとつ前の出力値 ＋ (1-a) * センサの値
        int n = 0;
        for(;n < 3;n++){
            out[n] = 0;
            out[n] = in[4][n] * 0.02f + in[3][n] * 0.08f + in[2][n] * 0.16f + in[1][n] * 0.26f + in[0][n] * 0.48f;
            in[4][n] = in[3][n];
            in[3][n] = in[2][n];
            in[2][n] = in[1][n];
            in[1][n] = in[0][n];
        }
    }

    public static void correct(){
        correct_rot[0] = -attitude[0];
        correct_rot[1] = -attitude[1];
        correct_rot[2] = -attitude[2];
    }

    public static float getRotX(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees((attitude[0] + correct_rot[0]));
        return (attitude[0] + correct_rot[0]);
    }

    public static float getRotY(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees((attitude[1] + correct_rot[1]));
        return (attitude[1] + correct_rot[1]);
    }

    public static float getRotZ(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees((attitude[2] + correct_rot[2]));
        return (attitude[2] + correct_rot[2]);
    }

    public static void setSensitivityX(float value){
        sensitivity[0] = value;
    }

    public static void setSensitivityY(float value){
        sensitivity[1] = value;
    }

    public static void setSensitivityZ(float value){
        sensitivity[2] = value;
    }

    public static float getRawRotX(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees(attitude[0]);
        return attitude[0];
    }

    public static float getRawRotY(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees(attitude[1]);
        return attitude[1];
    }

    public static float getRawRotZ(boolean isDegree){
        if(isDegree)
            return (float)Math.toDegrees(attitude[2]);
        return attitude[2];
    }
}

