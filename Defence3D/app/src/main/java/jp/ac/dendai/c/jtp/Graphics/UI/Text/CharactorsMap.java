package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import java.util.HashMap;

import jp.ac.dendai.c.jtp.openglesutil.Util.ArrayUtil;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/14.
 */
public class CharactorsMap {
    protected static HashMap<Character,Bitmap> charactors;
    public final static String def_fontName = "custom_font.ttf";
    public static Character[] loadText(String text){
        if(charactors == null)
            charactors = new HashMap<>();
        Character[] charas = ArrayUtil.toObjects(text);
        for(int n = 0;n < charas.length;n++){
            if(charas[n] == '\n' || charactors.containsKey(charas[n]))
                continue;
            Bitmap b = GLES20Util.stringToBitmap(String.valueOf(charas[n]),def_fontName,40, 255, 255, 255);
            charactors.put(charas[n],b);
        }
        return charas;
    }
    public static Bitmap getChara(Character c){
        if(charactors.containsKey(c))
            return charactors.get(c);
        return null;
    }
}
