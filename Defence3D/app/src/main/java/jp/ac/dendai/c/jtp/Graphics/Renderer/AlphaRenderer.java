package jp.ac.dendai.c.jtp.Graphics.Renderer;

import android.opengl.GLES20;

/**
 * Created by テツヤ on 2016/09/17.
 */
public class AlphaRenderer extends Renderer {
    public AlphaRenderer(){
        super();
    }
    @Override
    public void drawAll(){
        if(ite == null)
            return;
        GLES20.glDepthMask(false);
        shader.useShader();
        shader.updateCamera();
        RenderItem temp = ite;
        do{
            if(temp.rm != null)
                temp.rm.gameObject.update();
            if(temp.rm.isDraw) {
                temp.rm.draw();
            }
            temp = temp.prev;
        }while(temp != null && temp != ite);
        GLES20.glDepthMask(true);
    }
}
