package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/09.
 */
public class Text implements UI {
    StringBitmap sb;
    Texture image;
    public float x = 0,y = 0,z = 0;
    public float sx = 1,sy = 1;
    public float dx = 0,dy = 0,dz = 0;
    public float alpha = 1;
    int align_h = UI_CENTOR,align_v = UI_CENTOR;

    public Text(String text){
        sb = GLES20Util.stringToBitmap(text,1,255,255,255);
        image = new Texture(sb.getBitmap(), GLES20COMPOSITIONMODE.ALPHA);
    }

    public void setAlpha(float a){
        alpha = a;
    }

    public float getAlpha(){
        return alpha;
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        float bottom = sb.fm.bottom / (float)sb.bitmap.getHeight();
        shader.draw(image,x,y-(bottom*sy),image.getTexture().getWidth()/image.getTexture().getHeight()*sx,sy,dx,alpha);
        //shader.draw(image,0,0,image.getTexture().getWidth()/image.getTexture().getHeight()*sx,1,0,alpha);
    }
}
