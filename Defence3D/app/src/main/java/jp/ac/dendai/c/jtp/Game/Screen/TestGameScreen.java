package jp.ac.dendai.c.jtp.Game.Screen;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontMtlReader;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/21.
 */
public class TestGameScreen extends Screenable {
    private Mesh inveder_model;
    private Renderer renderer;
    private UiRenderer uiRenderer;
    private Shader shader;
    private Camera mainCamera;
    private GameObject[] inveders;
    private Button button;

    public TestGameScreen(){
        mainCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        shader = Constant.getShader(Constant.SHADER.diffuse);
        shader.setCamera(mainCamera);
        renderer = new Renderer();
        uiRenderer = new UiRenderer();
        renderer.setShader(shader);
        uiRenderer.setShader((UiShader)Constant.getShader(Constant.SHADER.ui));

        inveder_model = WavefrontObjConverter.createModel("inveder.obj");

        inveders = new GameObject[55];
        for(int n = 0;n < inveders.length;n++){
            inveders[n] = new GameObject();
            inveders[n].getRenderMediator().mesh = inveder_model;
            inveders[n].getRenderMediator().isDraw = true;
            inveders[n].getPos().setY(-5f);
            inveders[n].getPos().setZ(20f - (float)n / 11f * 5f);
            inveders[n].getPos().setX((float)n % 11 * 2f);
            renderer.addItem(inveders[n]);
        }

        button = new Button(0,0,0.3f,0.1f,"TEST");
        button.useAspect(true);
        button.setCriteria(Button.CRITERIA.HEIGHT);
        button.setHorizontal(UIAlign.Align.LEFT);
        button.setVertical(UIAlign.Align.BOTTOM);
        button.setX(0);
        button.setY(0);
        button.setBitmap(GLES20Util.loadBitmap(R.mipmap.button));
        button.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(StageSelectScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });

        uiRenderer.addItem(button);
    }

    @Override
    public void Proc() {
        if(freeze)
            return;
        button.proc();
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        renderer.drawAll();
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        for(int n = 0;n < Input.getMaxTouch();n++){
            button.touch(Input.getTouchArray()[n]);
        }
    }

    @Override
    public void death() {
        inveder_model.deleteBufferObject();
    }

    @Override
    public void init() {
        inveder_model.useBufferObject();
    }
}
