package jp.ac.dendai.c.jtp.Time;

/**
 * Created by Goto on 2016/09/22.
 */

public class Time {
    private static long currentTime = -1;
    private static float deltaTime = -1;
    private static long startTime = -1;
    public static void tick(){
        if(currentTime == -1){
            currentTime = System.currentTimeMillis();
            deltaTime = 0;
            startTime = currentTime;
        }else{
            long buff = System.currentTimeMillis();
            deltaTime = (float)(buff - startTime) / 1000f;
            startTime = buff;
        }
    }
    public static float getDeltaTime(){
        return deltaTime;
    }

    public static long getHour(long millisecond){
        return (millisecond / (1000*60*60));
    }

    public static long getMinute(long millisecond){
        return millisecond / (1000*60) - getHour(millisecond) * 60;
    }

    public static long getSecond(long millisecond){
        return millisecond / 1000 - getMinute(millisecond) * 60 - getHour(millisecond) * 60 * 60;
    }
}
