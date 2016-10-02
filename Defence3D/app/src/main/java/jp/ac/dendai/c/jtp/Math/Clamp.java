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
    public static void bezier2Trajectory(Vector3 targetPosition, Vector3 start, Vector3 end, Vector3 control, float time){
        targetPosition.setX((1f-time)*(1-time)*start.getX() + 2f*(1f-time)*time*control.getX() + time*time*end.getX());
        targetPosition.setY((1f-time)*(1-time)*start.getY() + 2f*(1f-time)*time*control.getY() + time*time*end.getY());
        targetPosition.setZ((1f-time)*(1-time)*start.getZ() + 2f*(1f-time)*time*control.getZ() + time*time*end.getZ());
    }
    public static void bezier3Trajectory(Vector3 targetPosition,Vector3 start,Vector3 end,Vector3 control1,Vector3 control2,float time){
        targetPosition.setX(
                (1f-time)*(1f-time)*(1f-time)*start.getX() + 3f*(1f-time)*(1f-time)*time*control1.getX() + 3f*(1-time)*time*time*control2.getX() + time*time*time*end.getX()
        );
        targetPosition.setY(
                (1f-time)*(1f-time)*(1f-time)*start.getY() + 3f*(1f-time)*(1f-time)*time*control1.getY() + 3f*(1-time)*time*time*control2.getY() + time*time*time*end.getY()
        );
        targetPosition.setZ(
                (1f-time)*(1f-time)*(1f-time)*start.getZ() + 3f*(1f-time)*(1f-time)*time*control1.getZ() + 3f*(1-time)*time*time*control2.getZ() + time*time*time*end.getZ()
        );
    }
}
