package jp.ac.dendai.c.jtp.Graphics.UI;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector2;

/**
 * Created by テツヤ on 2016/09/18.
 */
public interface TextureInfo {
    public int getFilterModeMin();
    public int getFilterModeMag();
    public int getWrapModeT();
    public int getWrapModeS();
    public Bitmap getBitmap();
    public float getAlpha();
    public void setAlpha(float a);
    public Vector2 getTexOffset();
    public Vector2 getTexScale();
}
