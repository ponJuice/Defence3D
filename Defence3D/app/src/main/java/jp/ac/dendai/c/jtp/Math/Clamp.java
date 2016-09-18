package jp.ac.dendai.c.jtp.Math;

/**
 * Created by テツヤ on 2016/09/15.
 */
public class Clamp {
    public static float clamp(float start,float end,float lengthTime,float time){
        if(time < 0)
            return 0;
        else if(time > lengthTime)
            return lengthTime;
        return (end - start)*time/lengthTime+start;
    }
    public static int clamp(int start,int end,int lengthTime,int time){
        if(time < 0)
            return 0;
        else if(time > lengthTime)
            return lengthTime;
        return (end - start)*time/lengthTime+start;
    }
}
