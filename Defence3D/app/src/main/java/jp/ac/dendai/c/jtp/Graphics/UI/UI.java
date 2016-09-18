package jp.ac.dendai.c.jtp.Graphics.UI;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/09/06.
 */
public abstract class UI implements TextureInfo,MaskInfo{
    public enum COLOR{
        R,
        G,
        B
    }
    public final static int UI_LEFT     = 0;
    public final static int UI_CENTOR   = 1;
    public final static int UI_RIGHT    = 2;
    public final static int UI_TOP      = 3;
    public final static int UI_BOTTOM   = 4;
    public abstract GLES20COMPOSITIONMODE getBlendMode();
    public abstract float getColor(COLOR col);
    public abstract int getVBO();
    public abstract int getIBO();
    public abstract void touch(Touch touch);
    public abstract void proc();
    public abstract void draw(UiShader shader);
}
