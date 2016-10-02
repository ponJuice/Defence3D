package jp.ac.dendai.c.jtp.Game.Score;

import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goto on 2016/09/29.
 */

public class ScoreManager {
    private static int score = 0;
    private static boolean saved = false;
    private static HashMap<String,ScorePacage> map = new HashMap<>();
    public static void loadScore(){
        String str = FileManager.readTextFile("score.dat");
        Log.d("Loat file [score.dat]",str);
        String[] lines = str.split("\n");
        String modeName = "";
        for(int line = 0;line < lines.length;line++){
            String[] sep = lines[line].split(",");
            Log.d("Load Score","line : "+sep.length);
            if(sep.length != 5 && sep.length != 1) {
                throw new RuntimeException("スコアファイルが破損している可能性があります");
            }
            if(sep.length == 1){
                if(!modeName.equals(""))
                    map.get(modeName).sort();
                //モードの行
                modeName = sep[0];
                map.put(modeName,new ScorePacage());
            }else{
                //データの行
                String[] data = sep[0].split(",");
                ScorePacage sp = map.get(modeName);
                Score s = new Score(new Integer(data[0]).intValue()
                ,new Integer(data[1]).intValue()
                ,new Integer(data[2]).intValue()
                ,new Integer(data[3]).intValue()
                ,new Long(data[4]).longValue());
                sp.add(s);
            }

        }
    }
    public static void saveScore(){
        StringBuilder sb = new StringBuilder();
        Set<String> set = map.keySet();
        Iterator ite = set.iterator();
        int count = 0;
        while(ite.hasNext()){
            String key = (String)ite.next();
            sb.append(key);
            sb.append("\n");
            ScorePacage sp = map.get(key);
            for(int n = 0;n < Math.min(3,sp.scores.size());n++){
                sb.append(sp.scores.get(n).toString());
                sb.append("\n");
                count++;
            }
        }
        FileManager.writeTextFile("score.dat",sb.toString(),MODE_PRIVATE);
        Log.d("Save Score File","Score Data : "+count);
    }
    public static ScorePacage getScore(String modeName){
        if(!map.containsKey(modeName)) {
            ScorePacage sp = new ScorePacage();
            map.put(modeName, sp);
            return sp;
        }
        else {
            return map.get(modeName);
        }
    }

    public static void addScore(String modeName,int score,long time){
        ScorePacage sp = getScore(modeName);
        Calendar c = Calendar.getInstance();
        sp.add(new Score(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE),score,time));
        saved = true;
        Log.d("Score Saved!!",sp.toString());
    }

    public static void init(int n){
        score = 0;
        saved = false;
    }
    public static void add(int s){
        score += s;
        saved = false;
    }
    public static void sub(int s){
        score -= s;
        saved = false;
    }
    public static int getScore(){
        return score;
    }
    public static boolean isSaved(){
        return saved;
    }
}
