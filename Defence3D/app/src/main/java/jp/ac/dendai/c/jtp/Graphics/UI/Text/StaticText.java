package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.widget.AlphabetIndexer;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/09/16.
 */
public class StaticText extends Image {
    protected float delta_u = 0,delta_v = 0;
    protected float counter = 0;
    public StaticText(String text){
        super(GLES20Util.stringToBitmap(text,"メイリオ",25,255,255,255));
    }
    public void setDelta_u(float u){
        delta_u = u;
    }
    public void setDelta_v(float v){
        delta_v = v;
    }

    public void init(){
        counter = 0;
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {
        counter++;
        if(counter*delta_u >= 2)
            counter = -2f / delta_u;
    }
}
