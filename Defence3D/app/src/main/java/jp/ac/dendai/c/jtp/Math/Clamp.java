package jp.ac.dendai.c.jtp.Math;

/**
 * Created by テツヤ on 2016/09/15.
 */
public class Clamp {
    public static float clamp(float start,float end,float lengthTime,float time){
        if(time < 0)
            return start;
        else if(time >= lengthTime)
            return end;
        return (end - start)*time/lengthTime+start;
    }
    public static int clamp(int start,int end,int lengthTime,int time){
        if(time < 0)
            return start;
        else if(time >= lengthTime)
            return end;
        return (end - start)*time/lengthTime+start;
    }
    public static float clamp(float start,float end,float value){
       if(value <= start)
           return start;
       else if( value >= end)
           return end;
       return value;
   }
    public static float clamp(float start,float end,float intervalTime,float lengthTime,float time){
        if(time < 0)
            return start;
        else if(time >= lengthTime-intervalTime)
            return end;
        return (end - start)/(lengthTime-intervalTime)*time+start;
    }
    public static float bezier2Trajectory(float start,float end,float control,float time){
        if(time >= 1)
            return end;
        else if(time <= 0)
            return start;
        return (1f-time)*(1-time)*start + 2f*(1f-time)*time*control+ time*time*end;
    }
    public static float bezier3Trajectory(float start,float end,float control1,float control2,float time){
        if(time >= 1)
            return end;
        else if(time <= 0)
            return start;
        return (1f-time)*(1f-time)*(1f-time)*start + 3f*(1f-time)*(1f-time)*time*control1 + 3f*(1-time)*time*time*control2 + time*time*time*end;

    }
}
