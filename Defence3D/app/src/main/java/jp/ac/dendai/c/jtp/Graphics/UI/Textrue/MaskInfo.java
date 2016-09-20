package jp.ac.dendai.c.jtp.Graphics.UI.Textrue;

import android.graphics.Bitmap;

/**
 * Created by テツヤ on 2016/09/18.
 */
public interface MaskInfo {
    public int getMaskWrapModeT();
    public int getMaskWrapModeS();
    public int getMaskFilterModeMin();
    public int getMaskFilterModeMag();
    public Bitmap getMaskBitmap();
    public void setMaskBitmap(Bitmap bitmap);
    public float getMaskOffset(TextureInfo.UV flag);
    public float getMaskScale(TextureInfo.UV flag);
    public void setMaskOffset(TextureInfo.UV flag, float n);
    public void setMaskScale(TextureInfo.UV flag, float n);
    public float getMaskAlpha();
    public void setMaskAlpha(float a);
}
