package jp.ac.dendai.c.jtp.Game.Screen;

import javax.microedition.khronos.opengles.GL;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Camera.UiCamera;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/20.
 */
public class StageSelectScreen extends Screenable {
    protected Button button;
    protected UiShader uiShader;
    protected UiRenderer uiRenderer;
    public StageSelectScreen(){
        button = new Button(0,0.1f,0.5f,0,"訓練");
        button.setCriteria(Button.CRITERIA.HEIGHT);
        button.setBitmap(GLES20Util.loadBitmap(R.mipmap.button));
        button.refreshHeight();
        uiShader = (UiShader) Constant.getShader(Constant.SHADER.ui);
        uiShader.setCamera(new UiCamera());
        uiRenderer = new UiRenderer();
        uiRenderer.setShader(uiShader);
        uiRenderer.addItem(button);
        button.setX(GLES20Util.getWidth_gl()/2f);
        button.setY(GLES20Util.getHeight_gl()/2f);
    }
    @Override
    public void Proc() {
        if(freeze)
            return;
        button.proc();
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        for(int n = 0;n < Input.getTouchArray().length;n++){
            button.touch(Input.getTouchArray()[n]);
        }
    }

    @Override
    public void death() {

    }

    @Override
    public void init() {

    }
}
