package jp.ac.dendai.c.jtp.Graphics.UI;

import android.graphics.Bitmap;

/**
 * Created by テツヤ on 2016/09/18.
 */
public interface MaskInfo {
    public enum MASK{
        u,
        v
    }
    public int getMaskWrapModeT();
    public int getMaskWrapModeS();
    public int getMaskFilterModeMin();
    public int getMaskFilterModeMag();
    public Bitmap getMaskBitmap();
    public void setMaskBitmap(Bitmap bitmap);
    public float getMaskOffset(MASK flag);
    public float getMaskScale(MASK flag);
    public void setMaskOffset(MASK flag,float n);
    public void setMaskScale(MASK flag,float n);
    public float getMaskAlpha();
    public void setMaskAlpha(float a);
}
