package jp.ac.dendai.c.jtp.Graphics.UI.Textrue;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector2;

/**
 * Created by テツヤ on 2016/09/18.
 */
public interface TextureInfo {
    public enum UV{
        u,
        v
    }
    public int getFilterModeMin();
    public int getFilterModeMag();
    public int getWrapModeT();
    public int getWrapModeS();
    public Bitmap getBitmap();
    public void setBitmap(Bitmap bitmap);
    public float getAlpha();
    public void setAlpha(float a);
    public float getTexOffset(UV flag);
    public float getTexScale(UV flag);
    public void setTexOffset(UV flag,float n);
    public void setTexScale(UV flag,float n);
}
