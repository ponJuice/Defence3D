package jp.ac.dendai.c.jtp.Game.Screen;

import android.util.Log;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Renderer.AlphaRenderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Physics.Collider.AABBCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/09/17.
 */
public class TestModelViewScreen extends Screenable {
    protected Shader testShader;
    protected UiShader uiShader;
    protected Renderer testRenderer;
    protected Renderer alphaRenderer;
    protected UiRenderer uiRenderer;
    protected GameObject ob1,ob2;
    protected Player player;
    protected Physics3D physics;
    protected Mesh box,grid,sphear;
    protected float rotateX,rotateY;
    public TestModelViewScreen(){
        //シェーダの作成
        testShader = Constant.getShader(Constant.SHADER.diffuse);
        uiShader = (UiShader)Constant.getShader(Constant.SHADER.ui);


        //オブジェクトファイルの読み込み
        box = WavefrontObjConverter.createModel("untitled.obj");
        grid = WavefrontObjConverter.createModel("gird.obj");
        sphear = WavefrontObjConverter.createModel("sphear.obj");

        ob1 = new GameObject();
        ob1.setName("ob1");
        ob1.getRenderMediator().mesh = box;
        ob1.getRenderMediator().isDraw = true;
        ob1.getPos().setZ(5f);
        ob1.setCollider(new AABBCollider(2,2,2));
        ob1.setPhysicsObject(new PhysicsObject(ob1));
        ob1.getPhysicsObject().tag = Constant.COLLISION_ENEMY;
        ob1.getPhysicsObject().mask = Constant.COLLISION_ENEMY;
        ob1.getPhysicsObject().useGravity = false;
        ob1.setCollisionListener(new CollisionListener() {
            @Override
            public void collEnter(ACollider col,GameObject owner) {
                ob1.getRenderMediator().alpha = 0.5f;
            }

            @Override
            public void collExit(ACollider col,GameObject owner) {
                ob1.getRenderMediator().alpha = 1f;
            }

            @Override
            public void collStay(ACollider col,GameObject owner) {
                Log.d("Collision!!",owner.getName()+" to "+col.getGameObject().getName());
            }
        });

        ob2 = new GameObject();
        ob2.setName("ob2");
        ob2.getRenderMediator().mesh = box;
        ob2.getRenderMediator().isDraw = true;
        ob2.getPos().setZ(5f);
        ob2.getPos().setX(2.1f);
        ob2.setCollider(new AABBCollider(2,2,2));
        ob2.setPhysicsObject(new PhysicsObject(ob2));
        ob2.getPhysicsObject().tag = Constant.COLLISION_ENEMY;
        ob2.getPhysicsObject().mask = Constant.COLLISION_ENEMY;
        ob2.getPhysicsObject().useGravity = false;
        ob2.setCollisionListener(new CollisionListener() {
            @Override
            public void collEnter(ACollider col,GameObject owner) {
                ob2.getRenderMediator().alpha = 0.5f;
            }

            @Override
            public void collExit(ACollider col,GameObject owner) {
                physics.removeObject(owner.getPhysicsObject());
                testRenderer.removeItem(owner);
                ob2.getRenderMediator().alpha = 1f;
            }

            @Override
            public void collStay(ACollider col,GameObject owner) {
                Log.d("Collision!!",owner.getName()+" to "+col.getGameObject().getName());
            }
        });

        PhysicsInfo info = new PhysicsInfo();
        info.enabled = true;
        info.gravity = new Vector3(0,-9.8f,0);
        info.maxObject = 10;
        physics = new Physics3D(info);
        physics.addObject(ob1.getPhysicsObject());
        physics.addObject(ob2.getPhysicsObject());

        //レンダラを作成
        testRenderer = new Renderer();
        uiRenderer = new UiRenderer();
        //レンダラにシェーダ―を登録
        testRenderer.setShader(testShader);
        uiRenderer.setShader(uiShader);
        //レンダラに表示したいオブジェクトを登録

        testRenderer.addItem(ob1);
        testRenderer.addItem(ob2);

        alphaRenderer = new AlphaRenderer();
        alphaRenderer.setShader(testShader);


        //カメラを作成
        Camera testCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,0,0,0, 1);
        testCamera.setFar(1000f);
        testCamera.setNear(0.01f);

        testShader.setCamera(testCamera);
        player = new Player();
        player.setCamera(testCamera);
        player.getScl().setX(0.1f);
        player.getScl().setY(0.1f);
        player.getScl().setZ(0.1f);
        player.getRenderMediator().isDraw = true;
        player.getRenderMediator().mesh = box;
        player.proc();
    }

    @Override
    public void Proc() {
        if(freeze)
            return;
        physics.simulate();
        player.proc();
        /*if(ACollider.isCollision((AABBCollider) ob1.getCollider(),(AABBCollider) ob2.getCollider())){
            ob1.getRenderMediator().alpha = 0.5f;
            ob2.getRenderMediator().alpha = 0.5f;
        }else{
            ob1.getRenderMediator().alpha = 1;
            ob2.getRenderMediator().alpha = 1;
        }*/
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        testRenderer.drawAll();
        alphaRenderer.drawAll();
        uiRenderer.drawAll();
    }

    @Override
    public void Touch() {
        if(freeze)
            return;
        for(int n = 0;n < Input.getMaxTouch();n++){
            if(Input.getTouchArray()[0].getTouchID() != -1) {
                ob2.getPos().setX(Input.getTouchArray()[0].getDelta(Touch.Pos_Flag.X) * 0.001f + ob2.getPos().getX());
                player.getRot().setY(player.getRot().getY() + Input.getTouchArray()[0].getDelta(Touch.Pos_Flag.X)*0.01f);
                Input.getTouchArray()[n].updatePosition(Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.X),Input.getTouchArray()[n].getPosition(Touch.Pos_Flag.Y));
            }
        }
    }

    @Override
    public void death() {

    }

    @Override
    public void init() {
        //バッファオブジェクトを使用する
        box.useBufferObject();
        grid.useBufferObject();
        sphear.useBufferObject();
    }
}
