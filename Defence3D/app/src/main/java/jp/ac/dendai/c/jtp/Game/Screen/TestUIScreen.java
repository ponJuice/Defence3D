package jp.ac.dendai.c.jtp.Game.Screen;

import android.opengl.GLES20;
import android.os.FileObserver;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Camera.UiCamera;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StreamText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/18.
 */
public class TestUIScreen extends Screenable {
    protected UiShader uiShader;
    protected UiRenderer uiRenderer;
    protected Button btn;
    protected NumberText nt;
    public TestUIScreen(){
        uiShader = (UiShader) Constant.getShader(Constant.SHADER.ui);
        uiShader.setCamera(Constant.getActiveUiCamera());
        Image image = new Image(ImageReader.readImageToAssets("Block.png"));
        image.setX(0);
        image.setY(0);
        image.setHeight(1f);
        image.setFilter_mag(GLES20.GL_NEAREST);
        image.setFilter_min(GLES20.GL_NEAREST);
        image.setAlpha(1.0f);
        image.setHorizontal(UIAlign.Align.LEFT);
        image.setVertical(UIAlign.Align.BOTTOM);
        StaticText text = new StaticText("TEXT");
        text.setX(0);
        text.setY(0);
        text.setWidth(0.5f);
        text.setFilter_mag(GLES20.GL_LINEAR);
        text.setFilter_min(GLES20.GL_LINEAR);
        text.setHorizontal(UIAlign.Align.LEFT);
        text.setVertical(UIAlign.Align.BOTTOM);

        String str = "あいうえお\nかきくけこ";
        StreamText st = StreamText.createStreamText(str,GLES20Util.loadBitmap(R.mipmap.text_mask),25,255,255,255);
        st.setChar_x(3);
        st.setChar_y(1);
        st.setWidth(0.5f);
        st.setHolizontal(UIAlign.Align.LEFT);
        st.setVertical(UIAlign.Align.BOTTOM);

        nt = new NumberText("メイリオ");
        nt.setX(GLES20Util.getWidth_gl()/2f);
        nt.setY(GLES20Util.getHeight_gl()/2f);
        nt.setHeight(0.1f);

        btn = new Button(0,0.5f,0.2f,0.4f,"ボタン");
        btn.setBitmap(GLES20Util.loadBitmap(R.mipmap.button));
        btn.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(TestModelViewScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });

        uiRenderer = new UiRenderer();
        uiRenderer.setShader(uiShader);

        uiRenderer.addItem(image);
        uiRenderer.addItem(text);
        uiRenderer.addItem(st);
        uiRenderer.addItem(btn);
        //uiRenderer.addItem(nt);
    }
    @Override
    public void Proc() {
        btn.proc();
        nt.setNumber(FpsController.getFps());
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {
        for(int n = 0;n < Input.getMaxTouch();n++) {
            btn.touch(Input.getTouchArray()[n]);
        }
    }

    @Override
    public void death() {
    }

    @Override
    public void init() {

    }
}
