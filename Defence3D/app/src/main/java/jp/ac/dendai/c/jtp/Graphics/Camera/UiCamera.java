package jp.ac.dendai.c.jtp.Graphics.Camera;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/19.
 */
public class UiCamera extends Camera {
    public UiCamera(){
        super(CAMERA_MODE.ORTHO, GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,-5f,GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,0);
    }
}
