package jp.ac.dendai.c.jtp.Game.Screen;

import android.util.Log;

import javax.microedition.khronos.opengles.GL;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingThread;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.UiCamera;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/20.
 */
public class StageSelectScreen extends Screenable {
    protected Button button1,button2;
    protected UiShader uiShader;
    protected UiRenderer uiRenderer;
    protected Image grid;
    public StageSelectScreen(){
        grid = new Image(GLES20Util.loadBitmap(R.mipmap.grid));
        grid.setWidth(0.5f);
        grid.setX(GLES20Util.getWidth_gl()/2f);
        grid.setY(GLES20Util.getHeight_gl() / 2f);

        button1 = new Button(0,0,0.5f,0.1f,"訓練");
        button1.useAspect(true);
        button1.setHorizontal(UIAlign.Align.LEFT);
        button1.setVertical(UIAlign.Align.CENTOR);
        button1.setCriteria(Button.CRITERIA.HEIGHT);
        button1.setPadding(0.05f);
        button1.setBitmap(GLES20Util.loadBitmap(R.mipmap.button));
        button1.refreshHeight();
        button1.setWidth(0.5f);
        button1.setX(GLES20Util.getWidth_gl() / 2f);
        button1.setY(GLES20Util.getHeight_gl() / 2f);
        button1.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(TestGameScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });

        button2 = new Button(0,0,0.5f,0.1f,"電大防衛戦");
        button2.useAspect(true);
        button2.setHorizontal(UIAlign.Align.RIGHT);
        button2.setVertical(UIAlign.Align.CENTOR);
        button2.setCriteria(Button.CRITERIA.HEIGHT);
        button2.setPadding(0.05f);
        button2.setBitmap(GLES20Util.loadBitmap(R.mipmap.button));
        button2.refreshHeight();
        button2.setWidth(0.6f);
        button2.setX(GLES20Util.getWidth_gl()/2f);
        button2.setY(GLES20Util.getHeight_gl()/2f);

        uiShader = (UiShader) Constant.getShader(Constant.SHADER.ui);
        uiShader.setCamera(Constant.getActiveUiCamera());

        uiRenderer = new UiRenderer();
        uiRenderer.setShader(uiShader);
        uiRenderer.addItem(grid);
        uiRenderer.addItem(button1);
        uiRenderer.addItem(button2);
    }
    float count = 0;
    @Override
    public void Proc() {
        if(freeze)
            return;
        //button2.setX(GLES20Util.getWidth_gl()*(float)Math.cos(count * 0.01f) - GLES20Util.getWidth_gl()/2f);
        //  button2.setY(GLES20Util.getWidth_gl()*(float)Math.sin(count * 0.01f) + GLES20Util.getHeight_gl()/2f);
        button1.proc();
        button2.proc();
       // Log.d("",""+count);
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        if(Input.getTouchArray()[0].getTouchID() != -1)
            count-= Input.getTouchArray()[0].getDelta(Touch.Pos_Flag.Y)*0.2f;
        for(int n = 0;n < Input.getTouchArray().length;n++){
            button1.touch(Input.getTouchArray()[n]);
            button2.touch(Input.getTouchArray()[n]);

            //Input.getTouchArray()[n].updatePosition(Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.X),Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.Y));
        }
    }

    @Override
    public void death() {

    }

    @Override
    public void init() {

    }
}
