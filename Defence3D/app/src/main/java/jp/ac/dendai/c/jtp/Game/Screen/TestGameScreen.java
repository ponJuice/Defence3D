package jp.ac.dendai.c.jtp.Game.Screen;

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
    private Mesh inveder_model,player_model,plane_model;
    private Renderer renderer;
    private UiRenderer uiRenderer;
    private Shader shader;
    private Camera mainCamera;
    private GameObject[] inveders;
    private GameObject floor;
    private Button button;
    private EnemyObserver eo;
    private Player player;

    public TestGameScreen(){
        mainCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        shader = Constant.getShader(Constant.SHADER.diffuse);
        shader.setCamera(mainCamera);
        renderer = new Renderer();
        uiRenderer = new UiRenderer();
        renderer.setShader(shader);
        uiRenderer.setShader((UiShader)Constant.getShader(Constant.SHADER.ui));

        inveder_model = WavefrontObjConverter.createModel("inveder.obj");
        player_model = WavefrontObjConverter.createModel("untitled.obj");
        plane_model = new Plane();

        player = new Player();
        player.setCamera(mainCamera);
        player.getRenderMediator() .mesh = player_model;
        player.getRenderMediator().isDraw = true;
        player.getScl().setX(0.1f);
        player.getScl().setY(0.1f);
        player.getScl().setZ(0.1f);
        //player.getPos().setY(-5f);
        player.setRadius(5f);
        renderer.addItem(player);

        floor = new GameObject();
        plane_model.getFaces()[0].matelial.tex_diffuse = ImageReader.readImageToAssets("Block.png");
        floor.getRenderMediator().mesh = plane_model;
        floor.getRenderMediator().isDraw = true;
        floor.getScl().setX(50f);
        floor.getScl().setY(50f);
        floor.getScl().setZ(50f);
        floor.getPos().setY(-5f);
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
        player.proc();
        eo.procAll();
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
            player.touch(Input.getTouchArray()[n]);
            button.touch(Input.getTouchArray()[n]);

            Input.getTouchArray()[n].resetDelta();
            //Input.getTouchArray()[n].updatePosition(Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.X),Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.Y));
        }
    }

    @Override
    public void death() {
        inveder_model.deleteBufferObject();
        player_model.deleteBufferObject();
        plane_model.deleteBufferObject();
    }

    @Override
    public void init() {
        inveder_model.useBufferObject();
        player_model.useBufferObject();
        plane_model.useBufferObject();
    }
}
