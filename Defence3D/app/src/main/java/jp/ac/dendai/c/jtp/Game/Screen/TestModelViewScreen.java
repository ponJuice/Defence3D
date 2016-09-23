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
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.Slider;
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.SliderChangeValueListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Physics.Collider.AABBCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
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
    protected Physics3D physics;
    protected Mesh box,grid,sphear;
    protected Slider slider_ob1_x,slider_ob1_y,slider_ob1_z,slider_ob2_x,slider_ob2_y,slider_ob2_z;
    protected float rotateX,rotateY;
    public TestModelViewScreen(){
        //シェーダの作成
        testShader = Constant.getShader(Constant.SHADER.diffuse);
        testShader.setCamera(new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-10,0,0,0));
        uiShader = (UiShader)Constant.getShader(Constant.SHADER.ui);
        uiShader.setCamera(Constant.getActiveUiCamera());


        //オブジェクトファイルの読み込み
        box = WavefrontObjConverter.createModel("untitled.obj");
        grid = WavefrontObjConverter.createModel("gird.obj");
        sphear = WavefrontObjConverter.createModel("sphear.obj");

        ob1 = new GameObject();
        ob1.setName("ob1");
        ob1.getRenderMediator().mesh = box;
        ob1.getRenderMediator().isDraw = true;
        ob1.getPos().setZ(5f);
        OBBCollider col = new OBBCollider(0,0,0,1,1,1);
        col.setUseOBB(true);
        ob1.setCollider(col);
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
        col = new OBBCollider(0,0,0,1,1,1);
        col.setUseOBB(true);
        ob2.setCollider(col);
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
                //physics.removeObject(owner.getPhysicsObject());
                //testRenderer.removeItem(owner);
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

        //スライダーの作成

        slider_ob1_x = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob1_x.setHorizontal(UIAlign.Align.LEFT);
        slider_ob1_x.setVertical(UIAlign.Align.TOP);
        slider_ob1_x.setY(GLES20Util.getHeight_gl());
        slider_ob1_x.setTouchThrough(false);
        slider_ob1_x.setMin(0);
        slider_ob1_x.setMax(360);
        slider_ob1_x.setValue(0);
        slider_ob1_x.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob1.getRot().setX(value);
            }
        });

        slider_ob1_y = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob1_y.setHorizontal(UIAlign.Align.LEFT);
        slider_ob1_y.setVertical(UIAlign.Align.TOP);
        slider_ob1_y.setY(GLES20Util.getHeight_gl());
        slider_ob1_y.setX(0.1f);
        slider_ob1_y.setTouchThrough(false);
        slider_ob1_y.setMin(0);
        slider_ob1_y.setMax(360);
        slider_ob1_y.setValue(0);
        slider_ob1_y.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob1.getRot().setY(value);
            }
        });

        slider_ob1_z = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob1_z.setHorizontal(UIAlign.Align.LEFT);
        slider_ob1_z.setVertical(UIAlign.Align.TOP);
        slider_ob1_z.setY(GLES20Util.getHeight_gl());
        slider_ob1_z.setTouchThrough(false);
        slider_ob1_z.setX(0.2f);
        slider_ob1_z.setMin(0);
        slider_ob1_z.setMax(360);
        slider_ob1_z.setValue(0);
        slider_ob1_z.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob1.getRot().setZ(value);
            }
        });

        slider_ob2_x = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob2_x.setHorizontal(UIAlign.Align.RIGHT);
        slider_ob2_x.setVertical(UIAlign.Align.TOP);
        slider_ob2_x.setY(GLES20Util.getHeight_gl());
        slider_ob2_x.setX(GLES20Util.getWidth_gl());
        slider_ob2_x.setTouchThrough(false);
        slider_ob2_x.setMin(0);
        slider_ob2_x.setMax(360);
        slider_ob2_x.setValue(0);
        slider_ob2_x.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob2.getRot().setX(value);
            }
        });

        slider_ob2_y = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob2_y.setHorizontal(UIAlign.Align.RIGHT);
        slider_ob2_y.setVertical(UIAlign.Align.TOP);
        slider_ob2_y.setY(GLES20Util.getHeight_gl());
        slider_ob2_y.setX(GLES20Util.getWidth_gl()-0.1f);
        slider_ob2_y.setTouchThrough(false);
        slider_ob2_y.setMin(0);
        slider_ob2_y.setMax(360);
        slider_ob2_y.setValue(0);
        slider_ob2_y.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob2.getRot().setY(value);
            }
        });

        slider_ob2_z = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        slider_ob2_z.setHorizontal(UIAlign.Align.RIGHT);
        slider_ob2_z.setVertical(UIAlign.Align.TOP);
        slider_ob2_z.setY(GLES20Util.getHeight_gl());
        slider_ob2_z.setX(GLES20Util.getWidth_gl()-0.2f);
        slider_ob2_z.setTouchThrough(false);
        slider_ob2_z.setMin(0);
        slider_ob2_z.setMax(360);
        slider_ob2_z.setValue(0);
        slider_ob2_z.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                ob2.getRot().setZ(value);
            }
        });

        //レンダラを作成
        testRenderer = new Renderer();
        uiRenderer = new UiRenderer();
        //レンダラにシェーダ―を登録
        testRenderer.setShader(testShader);
        uiRenderer.setShader(uiShader);
        //レンダラに表示したいオブジェクトを登録

        uiRenderer.addItem(slider_ob1_x);
        uiRenderer.addItem(slider_ob1_y);
        uiRenderer.addItem(slider_ob1_z);
        uiRenderer.addItem(slider_ob2_x);
        uiRenderer.addItem(slider_ob2_y);
        uiRenderer.addItem(slider_ob2_z);


        testRenderer.addItem(ob1);
        testRenderer.addItem(ob2);

        alphaRenderer = new AlphaRenderer();
        alphaRenderer.setShader(testShader);


        //カメラを作成
        Camera testCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,0,0,0, 1);
        testCamera.setFar(1000f);
        testCamera.setNear(0.01f);
    }

    @Override
    public void Proc() {
        if(freeze)
            return;
        physics.simulate();
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
        boolean flag;
        for(int n = 0;n < Input.getMaxTouch();n++){
            flag = slider_ob1_x.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = slider_ob1_y.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = slider_ob1_z.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = slider_ob2_x.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = slider_ob2_y.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[n].getTouchID() == -1)
                flag = slider_ob2_z.touch(Input.getTouchArray()[n]);
            if(flag || Input.getTouchArray()[0].getTouchID() == -1) {
                ob2.getPos().setX(Input.getTouchArray()[0].getDelta(Touch.Pos_Flag.X) * 0.001f + ob2.getPos().getX());
                ob2.getPos().setY(-Input.getTouchArray()[0].getDelta(Touch.Pos_Flag.Y) * 0.001f + ob2.getPos().getY());
            }
            Input.getTouchArray()[n].resetDelta();
        }
    }

    @Override
    public void death() {
        box.deleteBufferObject();
        grid.deleteBufferObject();
        sphear.deleteBufferObject();
    }

    @Override
    public void init() {
        //バッファオブジェクトを使用する
        box.useBufferObject();
        grid.useBufferObject();
        sphear.useBufferObject();
    }
}
