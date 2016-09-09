package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * Created by テツヤ on 2016/09/09.
 */
public class StringBitmap {
    Bitmap bitmap;
    Paint.FontMetrics fm;

    public StringBitmap(Bitmap b, Paint.FontMetrics f){
        bitmap = b;
        fm = f;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public Paint.FontMetrics getFontMetrics(){
        return fm;
    }
}
