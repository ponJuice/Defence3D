package jp.ac.dendai.c.jtp.Game.Screen;

import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemyObserver;
import jp.ac.dendai.c.jtp.Game.Enemy.Invader.InvaderFrontMoveState;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.Slider;
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.SliderChangeValueListener;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontMtlReader;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/09/21.
 */
public class TestGameScreen extends Screenable {
    private Mesh inveder_model,houdai_model,yuka_model,daiza_model;
    private Renderer renderer;
    private UiRenderer uiRenderer;
    private Shader shader;
    private Camera mainCamera;
    private GameObject[] inveders;
    private GameObject floor;
    private Button button,attackButton;
    private EnemyObserver eo;
    private Player player;
    private Bitmap buttonImage;
    private Slider angle;

    public TestGameScreen(){
        mainCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        shader = Constant.getShader(Constant.SHADER.diffuse);
        shader.setCamera(mainCamera);
        renderer = new Renderer();
        uiRenderer = new UiRenderer();
        renderer.setShader(shader);
        uiRenderer.setShader((UiShader)Constant.getShader(Constant.SHADER.ui));

        inveder_model = WavefrontObjConverter.createModel("inveder.obj");
        houdai_model = WavefrontObjConverter.createModel("houdai.obj");
        yuka_model = WavefrontObjConverter.createModel("yuka.obj");
        daiza_model = WavefrontObjConverter.createModel("daiza.obj");

        GameObject[] parts = new GameObject[2];
        parts[0] = new GameObject();
        parts[0].getRenderMediator().mesh = daiza_model;
        parts[0].getRenderMediator().isDraw = true;
        parts[1] = new GameObject();
        parts[1].getRenderMediator().mesh = houdai_model;
        parts[1].getRenderMediator().isDraw = true;

        player = new Player(parts);
        player.setCamera(mainCamera);
        player.getPos().setY(0.4f);
        player.setRadius(5f);
        renderer.addItem(parts[0]);
        renderer.addItem(parts[1]);

        floor = new GameObject();
        floor.getRenderMediator().mesh = yuka_model;
        floor.getRenderMediator().isDraw = true;
        //floor.getPos().setY(-5f);
        renderer.addItem(floor);

        inveders = new GameObject[55];
        for(int n = 0;n < inveders.length;n++){
            inveders[n] = new GameObject();
            inveders[n].getRenderMediator().mesh = inveder_model;
            inveders[n].getRenderMediator().isDraw = true;
            inveders[n].getPos().setY(-5f);
            inveders[n].getPos().setZ(50f - (float)(n / 11) * 5f);
            inveders[n].getPos().setX((float)n % 11 * 2f - 11f);
            renderer.addItem(inveders[n]);
        }

        eo = new EnemyObserver(inveders);

        buttonImage = GLES20Util.loadBitmap(R.mipmap.button);

        button = new Button(0,0,0.3f,0.1f,"TEST");
        button.useAspect(true);
        button.setCriteria(Button.CRITERIA.HEIGHT);
        button.setHorizontal(UIAlign.Align.LEFT);
        button.setVertical(UIAlign.Align.BOTTOM);
        button.setX(0);
        button.setY(0);
        button.setBitmap(buttonImage);
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
        button.setTouchThrough(false);

        angle = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        angle.setHorizontal(UIAlign.Align.RIGHT);
        angle.setVertical(UIAlign.Align.TOP);
        angle.setX(GLES20Util.getWidth_gl());
        angle.setY(GLES20Util.getHeight_gl());
        angle.setMin(0.5f);
        angle.setMax(2);
        angle.setValue(1);
        angle.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                player.getScl().setX(value);
            }
        });
        angle.setTouchThrough(false);

        attackButton = new Button(0,0,0.3f,0.1f,"Attack");
        attackButton.useAspect(true);
        attackButton.setCriteria(Button.CRITERIA.HEIGHT);
        attackButton.setHorizontal(UIAlign.Align.LEFT);
        attackButton.setVertical(UIAlign.Align.BOTTOM);
        attackButton.setX(0);
        attackButton.setY(0.1f);
        attackButton.setBitmap(buttonImage);
        attackButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {

            }
        });
        attackButton.setTouchThrough(false);

        uiRenderer.addItem(button);
        uiRenderer.addItem(attackButton);
        uiRenderer.addItem(angle);
    }

    @Override
    public void Proc() {
        if(freeze)
            return;
        player.proc();
        eo.procAll();
        button.proc();
        attackButton.proc();
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
        boolean flag;
        for(int n = 0;n < Input.getMaxTouch();n++){
            flag = button.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = attackButton.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = angle.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                player.touch(Input.getTouchArray()[n]);
            Input.getTouchArray()[n].resetDelta();
            //Input.getTouchArray()[n].updatePosition(Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.X),Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.Y));
        }
    }

    @Override
    public void death() {
        inveder_model.deleteBufferObject();
        houdai_model.deleteBufferObject();
        daiza_model.deleteBufferObject();
        yuka_model.deleteBufferObject();
    }

    @Override
    public void init() {
        inveder_model.useBufferObject();
        houdai_model.useBufferObject();
        daiza_model.useBufferObject();
        yuka_model.useBufferObject();
    }
}
