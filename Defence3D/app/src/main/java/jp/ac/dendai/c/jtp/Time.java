package jp.ac.dendai.c.jtp;

/**
 * Created by Goto on 2016/09/22.
 */

public class Time {
    private static long currentTime = -1;
    private static float deltaTime = -1;
    private static long startTime = -1;
    private static long deltaTimeMillies = -1;
    public static void tick(){
        if(currentTime == -1){
            currentTime = System.currentTimeMillis();
            deltaTime = 0;
            startTime = currentTime;
        }else{
            long buff = System.currentTimeMillis();
            deltaTimeMillies = buff - startTime;
            deltaTime = (float)deltaTimeMillies / 1000f;
            startTime = buff;
        }
    }
    public static long getDeltaTimeMillies(){
        return deltaTimeMillies;
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

    public static int getHour(float second){
        return (int)(second / (60f * 60f));
    }

    public static float getMinute(float second){
        return (int)(second / 60 - getHour(second) * 60);
    }

    public static float getSecond(float second){
        return (int)(second - getMinute(second) * 60 - getHour(second) * 60 * 60);
    }

}
