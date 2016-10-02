package jp.ac.dendai.c.jtp.Game.Screen;

import android.opengl.GLES20;
import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemyBulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Motion.FRMotion;
import jp.ac.dendai.c.jtp.Game.Enemy.Motion.LRMotion;
import jp.ac.dendai.c.jtp.Game.Enemy.Motion.MotionController;
import jp.ac.dendai.c.jtp.Game.GameState.EndlessModeState;
import jp.ac.dendai.c.jtp.Game.GameState.GameState;
import jp.ac.dendai.c.jtp.Game.GameState.StateChangeListener;
import jp.ac.dendai.c.jtp.Game.Score.ScoreManager;
import jp.ac.dendai.c.jtp.Graphics.UI.Panel.GameOverPanel;
import jp.ac.dendai.c.jtp.Graphics.UI.Panel.TimePanel;
import jp.ac.dendai.c.jtp.Graphics.UI.Panel.UIPanel;
import jp.ac.dendai.c.jtp.Game.Weapons.Battery.CuickBattery;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemyObserver;
import jp.ac.dendai.c.jtp.Game.Enemy.Inveder;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.AnimationBitmap;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.Animator;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.AlphaRenderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsThread;
import jp.ac.dendai.c.jtp.SlopeUtil.SlopeUtil;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/09/21.
 */
public class TestGameScreen extends Screenable {
    private Mesh inveder_model,houdai_model,yuka_model,daiza_model,bullet_model;
    private Renderer renderer;
    private AlphaRenderer alphaRenderer;
    private Shader shader;
    private Camera mainCamera;
    private Inveder[] inveders;
    private EnemyBulletList ebl;
    private GameObject floor;
    private EnemyObserver eo;
    private Player player;
    private UIPanel panel;
    private GameOverPanel g_panel;
    private TimePanel t_panel;
    private Image whiteOut;
    private float playTime;
    private int inveder_row = 5,inveder_column = 11;
    private float offset_x = -25f,offset_y = -5f,offset_z = 50f;

    private Image caution_image;

    private float clear_state_time_buffer = 0;

    private float cby,cbx;

    private MotionController motionController;


    private Physics3D physics;


    private PhysicsThread physicsThread;
    private Plane p;
    private float sens_x = 0.5f,sens_y = 0.5f,sens_z = 0.5f;
    private static EndlessModeState state;

    public TestGameScreen(){
        mainCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        shader = Constant.getShader(Constant.SHADER.diffuse);
        shader.setCamera(mainCamera);
        renderer = new Renderer();

        SlopeUtil.enabled(true);

        renderer.setShader(shader);
        alphaRenderer = new AlphaRenderer();
        alphaRenderer.setShader(shader);

        SlopeUtil.setSensitivityX(sens_x);
        SlopeUtil.setSensitivityY(sens_y);
        SlopeUtil.setSensitivityZ(sens_z);

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

        inveder_model = WavefrontObjConverter.createModel("crab.obj");
        houdai_model = WavefrontObjConverter.createModel("houdai.obj");
        yuka_model = WavefrontObjConverter.createModel("yuka.obj");
        daiza_model = WavefrontObjConverter.createModel("daiza.obj");
        bullet_model = WavefrontObjConverter.createModel("untitled.obj");
        p = new Plane();

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
        BulletTemplate bt = new BulletTemplate(bullet_model,new OBBCollider(0,0,0,1,1,1));
        bt.damage = 100;
        bt.tag = Constant.COLLISION_PLAYERBULLET;
        bt.mask = Constant.COLLISION_ENEMY | Constant.COLLISION_ENEMYBULLET;
        bt.scale_x = 0.3f;
        bt.scale_y = 0.3f;
        bt.scale_z = 0.3f;
        player.setBattery(new CuickBattery(physics,renderer,bt));

        physics.addObject(player.getPhysicsObject());

        renderer.addItem(player);
        //renderer.addItem(parts[0]);
        //renderer.addItem(parts[1]);

        floor = new GameObject();
        floor.getRenderMediator().mesh = yuka_model;
        floor.getRenderMediator().isDraw = true;
        //floor.getPos().setY(-5f);
        renderer.addItem(floor);

        GameObject[] attackTarget = new GameObject[1];
        attackTarget[0] = player;

        Animator damageAnimator = new Animator(AnimationBitmap.createAnimation(R.mipmap.exp_alpha,256,64,8,2));

        ebl = new EnemyBulletList(10,physics,renderer,alphaRenderer,bullet_model);

        int array_length = inveder_row * inveder_column;
        inveders = new Inveder[array_length];
        for(int n = 0;n < inveders.length;n++){
            inveders[n] = new Inveder(physics);
            inveders[n].setAlphaRenderer(alphaRenderer);
            inveders[n].setAnim(damageAnimator);
            inveders[n].setBullets(ebl);
            inveders[n].setTargetList(attackTarget);
            inveders[n].getRenderMediator().mesh = inveder_model;
            inveders[n].getRenderMediator().isDraw = true;
            inveders[n].getPos().setY(offset_y);
            inveders[n].getPos().setZ(offset_z + (float)(n / inveder_column) * 10f);
            inveders[n].getPos().setX(offset_x + (float)n % inveder_column * 5f - (float)inveder_column);
            inveders[n].setDebugDraw(false);
            inveders[n].setName("Inveder");
            renderer.addItem(inveders[n]);
        }

        motionController = new MotionController(inveders);
        LRMotion asm = new LRMotion();
        asm.offset_z = offset_z;
        asm.offset_x = offset_x;
        asm.offset_y = offset_y;

        motionController.addMotion(asm);
        motionController.addMotion(new FRMotion());
        motionController.setMotion(asm);
        //eo = new EnemyObserver(inveders);

        state = new EndlessModeState(motionController,player);
        state.setChangeStateListener(new StateChangeListener() {
            @Override
            public void changeState(GameState.GAME_STATE state) {
                Log.d("StateChange","Change:"+state.name());
                if(state == GameState.GAME_STATE.GAMEOVER){
                    physicsThread.setPause(true);
                    //eo.setPause(true);
                    SlopeUtil.enabled(false);
                    cbx = mainCamera.getPosition(Camera.POSITION.X);
                    cby = mainCamera.getPosition(Camera.POSITION.Y);
                }else if(state == GameState.GAME_STATE.CLEAR){
                    clear_state_time_buffer = 0;
                    for(int n = 0;n < inveders.length;n++){
                        inveders[n].getRenderMediator().isDraw = false;
                        inveders[n].getPhysicsObject().freeze = false;
                        inveders[n].init();
                        inveders[n].getPos().setY(offset_y);
                        inveders[n].getPos().setZ(offset_z + (float)(n / inveder_column) * 10f);
                        inveders[n].getPos().setX(offset_x + (float)(n % inveder_column) * 5f);
                        inveders[n].setDebugDraw(false);
                        inveders[n].setName("Inveder");
                        //renderer.addItem(inveders[n]);
                    }
                }
            }
        });
        state.setEnemys(inveders);

        Constant.setDebugModel(bullet_model);
        Constant.setDebugCamera(mainCamera);

        t_panel = new TimePanel();

        g_panel = new GameOverPanel(t_panel);
        g_panel.setEnabled(false);

        panel = new UIPanel(player,mainCamera);
        whiteOut = new Image();
        whiteOut.setBitmap(Constant.getBitmap(Constant.BITMAP.white));
        whiteOut.useAspect(false);
        whiteOut.setWidth(GLES20Util.getWidth_gl());
        whiteOut.setHeight(GLES20Util.getHeight_gl());
        whiteOut.setX(GLES20Util.getWidth_gl()/2f);
        whiteOut.setY(GLES20Util.getHeight_gl()/2f);
        whiteOut.setAlpha(0);
        whiteOut.setEnabled(false);

        panel.getUiRenderer().addItem(whiteOut);

        caution_image = new Image();
        caution_image.setBitmap(GLES20Util.loadBitmap(R.mipmap.caution));
        caution_image.useAspect(true);
        caution_image.setWidth(0.5f);
        caution_image.setBlendMode(GLES20COMPOSITIONMODE.ADD);
        caution_image.setHorizontal(UIAlign.Align.CENTOR);
        caution_image.setVertical(UIAlign.Align.CENTOR);
        caution_image.setX(GLES20Util.getWidth_gl()/2f);
        caution_image.setY(GLES20Util.getHeight_gl()/2f);
        caution_image.setAlpha(0f);
        caution_image.setEnabled(false);
        panel.getUiRenderer().addItem(caution_image);
    }


    protected float whiteBuffer = 0;

    @Override
    public void Proc() {
        if(freeze)
            return;
        if(!physicsThread.isRun())
            physicsThread.start();
        if(state.getState() == GameState.GAME_STATE.PLAYING) {
            panel.proc();
            motionController.procAll();
            //eo.procAll();
            player.proc();

            state.proc();
            t_panel.setTime(playTime);
            playTime += Time.getDeltaTime();
            //Log.d("Time",""+playTime);
        }else if(state.getState() == GameState.GAME_STATE.GAMEOVER){
            whiteOut.setEnabled(true);
            float a = Clamp.bezier2Trajectory(0,1,1f,whiteBuffer/3f);
            whiteOut.setAlpha(a);
            mainCamera.setPosition(cbx + (Constant.getRandom().nextFloat() - 0.5f)*a
                    ,cby + (Constant.getRandom().nextFloat()-0.5f)*a
                    ,mainCamera.getPosition(Camera.POSITION.Z));

            whiteBuffer += Time.getDeltaTime();
            if(whiteBuffer >= 3){
                if(!ScoreManager.isSaved()) {
                    ScoreManager.addScore("ENDLESS", ScoreManager.getScore(), (long) (playTime * 1000f));
                    ScoreManager.saveScore();
                }
                //ゲームオーバー画面の表示
                g_panel.setEnabled(true);
                g_panel.proc();
            }
        }else if(state.getState() == GameState.GAME_STATE.CLEAR){
            float deltaTime = 20f;
            if(clear_state_time_buffer > (float)inveders.length / deltaTime){
                state.changeState(GameState.GAME_STATE.PLAYING);
            }
            for(int n = 0;n < inveders.length;n++){
                if(clear_state_time_buffer > (float)n/deltaTime){
                    inveders[n].getRenderMediator().isDraw = true;
                }
            }
            clear_state_time_buffer += Time.getDeltaTime();
        }
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        renderer.drawAll();
        if(!g_panel.getEnabled())
            t_panel.draw();
        panel.draw();
        if(g_panel.getEnabled())
            t_panel.draw();
        g_panel.draw();
        alphaRenderer.drawAll();
        //Constant.debugDraw(0,0,0,1,1,1,0,0,0,1);
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        for (int n = 0; n < Input.getTouchArray().length; n++) {
            boolean flag = true;
            if(state.getState() == GameState.GAME_STATE.PLAYING) {
                flag = panel.touch(true, Input.getTouchArray()[n]);
                if (flag)
                    player.touch(Input.getTouchArray()[n]);
            }else if(state.getState() == GameState.GAME_STATE.GAMEOVER) {
                g_panel.touch(true, Input.getTouchArray()[n]);
            }
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
        p.deleteBufferObject();
    }

    @Override
    public void init() {
        inveder_model.useBufferObject();
        houdai_model.useBufferObject();
        daiza_model.useBufferObject();
        yuka_model.useBufferObject();
        bullet_model.useBufferObject();
        p.useBufferObject();

        ScoreManager.init(0);
    }
}
