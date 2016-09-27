package jp.ac.dendai.c.jtp.Game.Screen;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Bullet.TestBattery;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemyObserver;
import jp.ac.dendai.c.jtp.Game.Enemy.Invader.InvaderFrontMoveState;
import jp.ac.dendai.c.jtp.Game.Enemy.Inveder;
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
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Graphics.UI.UIObserver;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontMtlReader;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsThread;
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
    private Mesh inveder_model,houdai_model,yuka_model,daiza_model,bullet_model;
    private Renderer renderer;
    private UiRenderer uiRenderer;
    private Shader shader;
    private Camera mainCamera;
    private GameObject[] inveders;
    private GameObject floor;
    private Button button,attackButton,leftButton,rightButton;
    private EnemyObserver eo;
    private Player player;
    private Bitmap buttonImage;
    private Slider angle,speedSlider;
    private Physics3D physics;
    private GameObject testBullet;
    private UIObserver uiObserver;
    private NumberText speedText;
    private StaticText attack;
    private PhysicsThread physicsThread;
    private GameObject bullet;

    public TestGameScreen(){
        mainCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        shader = Constant.getShader(Constant.SHADER.diffuse);
        shader.setCamera(mainCamera);
        renderer = new Renderer();
        uiRenderer = new UiRenderer();
        renderer.setShader(shader);
        uiRenderer.setShader((UiShader)Constant.getShader(Constant.SHADER.ui));

        PhysicsInfo pi = new PhysicsInfo();
        pi.gravity.setY(-9.8f);
        pi.maxObject = 100;
        pi.enabled = true;
        pi.bx = 100f;
        pi.by = 100f;
        pi.bz = 100f;
        Constant.setPhysicsInfo(pi);
        physics = new Physics3D(pi);

        physicsThread = new PhysicsThread(physics);

        inveder_model = WavefrontObjConverter.createModel("inveder.obj");
        houdai_model = WavefrontObjConverter.createModel("houdai.obj");
        yuka_model = WavefrontObjConverter.createModel("yuka.obj");
        daiza_model = WavefrontObjConverter.createModel("daiza.obj");
        bullet_model = WavefrontObjConverter.createModel("untitled.obj");

        final GameObject[] parts = new GameObject[2];
        parts[0] = new GameObject();
        parts[0].getRenderMediator().mesh = daiza_model;
        parts[0].getRenderMediator().isDraw = true;
        parts[0].setName("Parts[0]");
        parts[1] = new GameObject();
        parts[1].getRenderMediator().mesh = houdai_model;
        parts[1].getRenderMediator().isDraw = true;
        parts[1].setName("Parts[1]");

        player = new Player(parts);
        player.setCamera(mainCamera);
        player.getPos().setY(0.4f);
        player.setRadius(5f);
        player.setDebugDraw(false);
        player.useOBB(false);

        TestBattery tb = new TestBattery(physics,renderer,bullet_model,Constant.COLLISION_PLAYERBULLET,Constant.COLLISION_ENEMY | Constant.COLLISION_ENEMYBULLET);
        player.setBattery(tb);

        physics.addObject(player.getPhysicsObject());

        renderer.addItem(player);
        //renderer.addItem(parts[0]);
        //renderer.addItem(parts[1]);

        uiObserver = new UIObserver();

        floor = new GameObject();
        floor.getRenderMediator().mesh = yuka_model;
        floor.getRenderMediator().isDraw = true;
        //floor.getPos().setY(-5f);
        renderer.addItem(floor);

        testBullet = new GameObject();
        testBullet.getRenderMediator().mesh = bullet_model;
        testBullet.getRenderMediator().isDraw = false;
        testBullet.setPhysicsObject(new PhysicsObject(testBullet));
        testBullet.getPhysicsObject().useGravity = true;
        testBullet.getPhysicsObject().freeze = true;
        testBullet.getPhysicsObject().tag = Constant.COLLISION_PLAYERBULLET;
        testBullet.getPhysicsObject().mask = Constant.COLLISION_ENEMY | Constant.COLLISION_ENEMYBULLET;
        testBullet.setCollider(new OBBCollider(0,0,0,1,1,1));
        testBullet.getScl().setX(1f);
        testBullet.getScl().setY(1f);
        testBullet.getScl().setZ(1f);
        testBullet.setDebugDraw(false);
        testBullet.useOBB(false);
        testBullet.setName("Player Bullet");
        //player.setBullte(testBullet);
        //physics.addObject(testBullet.getPhysicsObject());
        //renderer.addItem(testBullet);

        bullet= new GameObject();
        bullet.getRenderMediator().mesh = bullet_model;
        bullet.getRenderMediator().isDraw = false;
        bullet.setPhysicsObject(new PhysicsObject(bullet));
        bullet.getPhysicsObject().mask = Constant.COLLISION_PLAYDER | Constant.COLLISION_PLAYERBULLET;
        bullet.getPhysicsObject().tag = Constant.COLLISION_ENEMYBULLET;
        bullet.getPhysicsObject().useGravity = true;
        bullet.getPhysicsObject().freeze = true;
        bullet.getPhysicsObject().useGravity = true;
        bullet.setCollisionListener(new CollisionListener() {
            @Override
            public void collEnter(ACollider col, GameObject owner) {
                owner.getRenderMediator().isDraw = false;
                owner.getPhysicsObject().freeze = true;
                Log.d("Enemy Bullet","Enemy Bullet Collision!! "+col.getGameObject().getClass());
            }

            @Override
            public void collExit(ACollider col, GameObject owner) {

            }

            @Override
            public void collStay(ACollider col, GameObject owner) {

            }
        });
        bullet.setCollider(new OBBCollider(0,0,0,1,1,1));
        bullet.setDebugDraw(false);
        bullet.useOBB(false);
        bullet.getScl().setX(0.2f);
        bullet.getScl().setY(0.2f);
        bullet.getScl().setZ(0.2f);
        bullet.setName("Enemy Bullet");
        physics.addObject(bullet.getPhysicsObject());
        renderer.addItem(bullet);

        GameObject[] attackTarget = new GameObject[1];
        attackTarget[0] = player;

        int array_length = 55;
        inveders = new Inveder[array_length];
        for(int n = 0;n < inveders.length;n++){
            inveders[n] = new Inveder(physics);
            ((Inveder)inveders[n]).setBullets(bullet);
            ((Inveder)inveders[n]).setTargetList(attackTarget);
            inveders[n].getRenderMediator().mesh = inveder_model;
            inveders[n].getRenderMediator().isDraw = true;
            inveders[n].getPos().setY(-5f);
            inveders[n].getPos().setZ(50f + (float)(n / 11) * 15f);
            inveders[n].getPos().setX((float)n % 11 * 3f - 11f);
            inveders[n].getScl().setZ(2);
            inveders[n].setDebugDraw(false);
            inveders[n].setName("Inveder");
            renderer.addItem(inveders[n]);
        }

        eo = new EnemyObserver(inveders);

        buttonImage = GLES20Util.loadBitmap(R.mipmap.button);

        button = new Button(0,0,0.3f,0.1f,"Back");
        button.setCriteria(Button.CRITERIA.HEIGHT);
        button.setHorizontal(UIAlign.Align.LEFT);
        button.setVertical(UIAlign.Align.TOP);
        button.setWidth(0.2f);
        button.setX(0);
        button.setY(GLES20Util.getHeight_gl());
        button.setBitmap(buttonImage);
        button.useAspect(true);
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

        leftButton = new Button(0,0,0.1f,0.1f,"<");
        leftButton.setBitmap(buttonImage);
        leftButton.setCriteria(Button.CRITERIA.HEIGHT);
        leftButton.setHorizontal(UIAlign.Align.RIGHT);
        leftButton.setVertical(UIAlign.Align.BOTTOM);
        leftButton.setX(GLES20Util.getWidth_gl() - 0.205f);
        leftButton.setY(0);
        leftButton.setWidth(0.2f);
        leftButton.useAspect(true);
        leftButton.setTouchThrough(false);
        leftButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {
                player.getPos().setX(player.getPos().getX() + 0.1f);
            }

            @Override
            public void touchUp(Button button) {
            }
        });

        rightButton = new Button(0,0,0.1f,0.1f,">");
        rightButton.setBitmap(buttonImage);
        rightButton.setCriteria(Button.CRITERIA.HEIGHT);
        rightButton.setHorizontal(UIAlign.Align.RIGHT);
        rightButton.setVertical(UIAlign.Align.BOTTOM);
        rightButton.setX(GLES20Util.getWidth_gl());
        rightButton.setY(0);
        rightButton.setWidth(0.2f);
        rightButton.useAspect(true);
        rightButton.setTouchThrough(false);
        rightButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {
                player.getPos().setX(player.getPos().getX() - 0.1f);
            }

            @Override
            public void touchUp(Button button) {

            }
        });

        angle = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        angle.setHorizontal(UIAlign.Align.RIGHT);
        angle.setVertical(UIAlign.Align.TOP);
        angle.setX(GLES20Util.getWidth_gl());
        angle.setY(GLES20Util.getHeight_gl());
        angle.setMin(1);
        angle.setMax(70);
        angle.setValue(40);
        angle.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                mainCamera.setAngleOfView(value);
            }
        });
        angle.setTouchThrough(false);

        speedSlider = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        speedSlider.setHorizontal(UIAlign.Align.LEFT);
        speedSlider.setVertical(UIAlign.Align.CENTOR);
        speedSlider.setX(0);
        speedSlider.setY(GLES20Util.getHeight_gl()/2f);
        speedSlider.setMin(10f);
        speedSlider.setMax(100f);
        speedSlider.setValue(10f);
        speedSlider.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                player.testSetBulletSpeed(value);
                player.getBattery().setRange(value);
                speedText.setNumber((int)value);
            }
        });
        speedSlider.setTouchThrough(false);

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
                player.attack();
            }
        });
        attackButton.setTouchThrough(false);

        speedText = new NumberText("メイリオ");
        speedText.setHeight(0.15f);
        speedText.setX(0.1f);
        speedText.setVertical(UIAlign.Align.CENTOR);
        speedText.setHorizontal(UIAlign.Align.LEFT);
        speedText.setY(GLES20Util.getHeight_gl()/2f);

        attack = new StaticText("Inveder Damaged!!");
        attack.useAspect(true);
        attack.setHorizontal(UIAlign.Align.CENTOR);
        attack.setVertical(UIAlign.Align.TOP);
        attack.setHeight(0.1f);
        attack.setX(GLES20Util.getWidth_gl()/2f);
        attack.setY(GLES20Util.getHeight_gl());
        attack.setAlpha(0);


        uiRenderer.addItem(button);
        uiRenderer.addItem(attackButton);
        uiRenderer.addItem(angle);
        uiRenderer.addItem(leftButton);
        uiRenderer.addItem(rightButton);
        uiRenderer.addItem(speedSlider);
        uiRenderer.addItem(speedText);
        uiRenderer.addItem(attack);

        uiObserver.addItem(button);
        uiObserver.addItem(attackButton);
        uiObserver.addItem(angle);
        uiObserver.addItem(leftButton);
        uiObserver.addItem(rightButton);
        uiObserver.addItem(speedSlider);
        uiObserver.addItem(speedText);
        uiObserver.addItem(attack);


        Constant.setDebugModel(bullet_model);
        Constant.setDebugCamera(mainCamera);
    }



    @Override
    public void Proc() {
        if(freeze)
            return;
        if(!physicsThread.isRun())
            physicsThread.start();
        player.proc();
        uiObserver.proc();
        eo.procAll();
        button.proc();
        attackButton.proc();

        bullet.getPos();

        if(attack.getAlpha() > 0){
            attack.setAlpha(attack.getAlpha() - 0.01f);
        }
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        renderer.drawAll();
        uiRenderer.drawAll();
        //Constant.debugDraw(0,0,0,1,1,1,0,0,0,1);
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        for(int n = 0;n < Input.getTouchArray().length;n++){
            boolean flag = true;
            flag = uiObserver.touch(Input.getTouchArray()[n],flag);
            if(flag)
                player.touch(Input.getTouchArray()[n]);
            //Log.d("touch","index:"+n+" flag:"+flag);
            Input.getTouchArray()[n].resetDelta();
        }
    }

    @Override
    public void death() {
        physicsThread.setEnd(true);
        physicsThread = null;
        inveder_model.deleteBufferObject();
        houdai_model.deleteBufferObject();
        daiza_model.deleteBufferObject();
        yuka_model.deleteBufferObject();
        bullet_model.deleteBufferObject();
    }

    @Override
    public void init() {
        inveder_model.useBufferObject();
        houdai_model.useBufferObject();
        daiza_model.useBufferObject();
        yuka_model.useBufferObject();
        bullet_model.useBufferObject();
    }
}
