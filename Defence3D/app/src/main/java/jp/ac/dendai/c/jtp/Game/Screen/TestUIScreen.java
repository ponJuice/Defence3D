package jp.ac.dendai.c.jtp.Game.Screen;

import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/18.
 */
public class TestUIScreen extends Screenable {
    protected UiShader uiShader;
    protected Camera uiCamera;
    protected UiRenderer uiRenderer;
    public TestUIScreen(){
        uiCamera = new Camera(Camera.CAMERA_MODE.ORTHO,0,0,-5,0,0,0);
        uiShader = new UiShader();
        uiShader.setCamera(uiCamera);
        Image image = new Image(GLES20Util.loadBitmap(R.mipmap.test));
        image.setX(0);
        image.setY(0);
        image.setHeight(1f);
        image.setFilter_mag(GLES20.GL_NEAREST);
        image.setFilter_min(GLES20.GL_NEAREST);
        image.setAlpha(1.0f);
        image.setMaskBitmap(GLES20Util.loadBitmap(R.mipmap.text_effect_mask));
        StaticText text = new StaticText("TEXT");
        text.setX(0.5f);
        text.setY(0);
        text.setWidth(0.5f);
        text.setFilter_mag(GLES20.GL_LINEAR);
        text.setFilter_min(GLES20.GL_LINEAR);
        text.setMaskBitmap(GLES20Util.loadBitmap(R.mipmap.text_effect_mask));
        uiRenderer = new UiRenderer();
        uiRenderer.setShader(uiShader);

        uiRenderer.addItem(image);
        uiRenderer.addItem(text);
    }
    @Override
    public void Proc() {

    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {

    }

    @Override
    public void death() {

    }
}
