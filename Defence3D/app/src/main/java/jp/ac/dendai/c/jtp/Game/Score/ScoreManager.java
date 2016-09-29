package jp.ac.dendai.c.jtp.Game.Score;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goto on 2016/09/29.
 */

public class ScoreManager {
    private static HashMap<String,ScorePacage> map = new HashMap<>();
    public static void loadScore(){
        String str = FileManager.readTextFile("score.dat");
        String[] lines = str.split("\n");
        String modeName = "";
        for(int line = 0;line < lines.length;line++){
            String[] sep = lines[line].split(",");
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
        while(ite.hasNext()){
            String key = (String)ite.next();
            sb.append(key);
            sb.append("\n");
            ScorePacage sp = map.get(key);
            for(int n = 0;n < sp.scores.size();n++){
                sb.append(sp.scores.get(n).toString());
                sb.append("\n");
            }
        }
        FileManager.writeTextFile("score.dat",sb.toString(),MODE_PRIVATE);
    }
}
