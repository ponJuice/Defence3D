package jp.ac.dendai.c.jtp.Game.Score;

import jp.ac.dendai.c.jtp.Time.Time;

/**
 * Created by Goto on 2016/09/29.
 */

public class Score implements Comparable{
    public int year = -1,month = -1,day = -1,score = -1;
    public long time = -1;

    public Score(int year,int manth,int day,int score,long time){
        this.year = year;
        this.month = month;
        this.day = day;
        this.score = score;
        this.time = time;
    }

    public int getHour(){
        return (int)Time.getHour(time);
    }

    public int getMinute(){
        return (int)Time.getMinute(time);
    }

    public int getSecont(){
        return (int)Time.getSecond(time);
    }

    public void setMilliSecond(long time){
        this.time = time;
    }

    @Override
    public int compareTo(Object another) {
        Score s = (Score)another;
        if(score > s.score)
            return -1;
        else if(score < s.score)
            return 1;
        else{
            if(time < s.time)
                return -1;
            else if(time > s.time)
                return 1;
            else
                return 0;
        }
    }

    @Override
    public String toString(){
        return year+","+month+","+day+","+score+","+time;
    }
}
