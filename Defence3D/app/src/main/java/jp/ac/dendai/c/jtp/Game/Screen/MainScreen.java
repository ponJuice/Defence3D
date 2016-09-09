package jp.ac.dendai.c.jtp.Game.Screen;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Line.Line;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.ModelObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.Text;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.defence3d.MainActivity;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/09.
 */
public class MainScreen implements Screenable {
    private float rotateX = 0 ,rotateY = 0;
    private Line line_x,line_y,line_z;
    private Renderer renderer;
    private Renderer testRenderer;
    private Plane plane;
    private Camera camera;
    private Camera uiCamera;
    private Camera testCamera;
    private GameObject[] gameObjects;
    private Button button;
    private Player player;
    private Physics3D physics;
    private Shader shader;
    private UiShader uiShader;
    private DiffuseShader testShader;
    private Texture tex;
    private ModelObject box,gird,sphear;
    private NumberText nt;
    private Text tx;
    public MainScreen(){
        String vertexShader = new String(FileManager.readShaderFile("DiffuseShaderVertex.txt"));
        String fragmentShader = new String(FileManager.readShaderFile("DiffuseShaderFragment.txt"));
        //テクスチャを１枚使えるようにする
        Shader.useTexture(1);
        //シェーダの作成
        testShader = new DiffuseShader();
        shader = new DiffuseShader();
        uiShader = new UiShader();
        //OpenGLES20のもろもろを使えるようにする
        GLES20Util.initGLES20Util(vertexShader, fragmentShader, false);

        //オブジェクトファイルの読み込み
        box = WavefrontObjConverter.createModel("untitled.obj");
        gird = WavefrontObjConverter.createModel("gird.obj");
        sphear = WavefrontObjConverter.createModel("sphear.obj");
        //バッファオブジェクトを使用する
        box.useBufferObject();
        gird.useBufferObject();
        sphear.useBufferObject();

        //プリミティブ型
        plane = new Plane();
        plane.useBufferObject();
        plane.setImage(ImageReader.readImageToAssets("Block.png"));

        //UI用のテクスチャ読み込み
        tex = new Texture(ImageReader.readImageToAssets("Block.png"), GLES20COMPOSITIONMODE.ALPHA);
        //バッファオブジェクトを使用する
        tex.setBufferObject();

        //ゲームオブジェクトを作成
        gameObjects = new GameObject[4];
        gameObjects[0] = new GameObject();
        gameObjects[1] = new GameObject();
        gameObjects[2] = new GameObject();
        gameObjects[3] = new GameObject();
        //メッシュをゲームオブジェクトに登録
        gameObjects[0].getRenderMediator().mesh = box;
        gameObjects[1].getRenderMediator().mesh = gird;
        gameObjects[2].getRenderMediator().mesh = plane;
        gameObjects[3].getRenderMediator().mesh = box;
        //コライダ―の設定
        CircleCollider cc = new CircleCollider(0.125f);
        cc.setDebugModel(sphear);
        cc.setDebugDraw(true);
        gameObjects[0].setCollider(cc);
        cc = new CircleCollider(1f);
        cc.setDebugModel(sphear);
        cc.setDebugDraw(true);
        gameObjects[3].setCollider(cc);
        //ゲームオブジェクトの位置を変更
        gameObjects[0].getPos().setX(-2.0f);
        gameObjects[1].getPos().setZ(0.5f);
        gameObjects[2].getScl().setX(50f);
        gameObjects[2].getScl().setY(50f);
        gameObjects[2].getScl().setZ(50f);
        gameObjects[2].getPos().setY(-4.5f);
        gameObjects[2].getPos().setZ(-25f);
        gameObjects[3].getPos().setZ(-25);

        //あたり判定用の処理を追加

        //プレイヤー
        player = new Player();
        player.getPos().zeroReset();
        player.getRenderMediator().mesh = box;
        player.getScl().setX(0.1f);
        player.getScl().setY(0.1f);
        player.getScl().setZ(0.1f);
        //タッチリスナで動かせるように
        Input.getTouchArray()[0].addTouchListener(new TouchListener() {
            @Override
            public void execute(Touch t) {
                rotateX += t.getDelta(Touch.Pos_Flag.Y) * 0.1f;
                rotateY += t.getDelta(Touch.Pos_Flag.X) * 0.1f;

                if(rotateX <= -90f)
                    rotateX = -89f;
                else if(rotateX >= 90f)
                    rotateX = 89f;

                if(rotateY < -90f)
                    rotateY = -90f;
                else if(rotateY > 90f)
                    rotateY = 90f;

                player.getRot().setY(rotateY);
                player.getRot().setX(-rotateX);
            }
        });

        //物理計算用クラス作成
        PhysicsInfo info = new PhysicsInfo();
        info.enabled = true;
        info.gravity = new Vector3(0,-0.98f,0);
        info.maxObject = 3;
        physics = new Physics3D(info);
        PhysicsObject po = new PhysicsObject(gameObjects[0]);
        po.gameObject.getScl().setX(0.25f);
        po.gameObject.getScl().setY(0.25f);
        po.gameObject.getScl().setZ(0.25f);
        po.gameObject.getPos().setY(10f);
        po.gameObject.getPos().setZ(-5f);
        po.useGravity = true;
        po.freeze = false;
        physics.addObject(po);
        PhysicsObject po2 = new PhysicsObject(gameObjects[3]);
        po2.useGravity = false;
        physics.addObject(po2);
        //po = new PhysicsObject(gameObjects[1]);
        //po.useGravity = false;
        //po.freeze = false;
        //physics.addObject(po);

        //レンダラを作成
        renderer = new Renderer();
        testRenderer = new Renderer();
        //レンダラにシェーダ―を登録
        renderer.setShader(shader);
        testRenderer.setShader(testShader);
        //レンダラに表示したいオブジェクトを登録
        renderer.addItem(gameObjects[0]);
        renderer.addItem(gameObjects[1]);
        renderer.addItem(gameObjects[2]);

        testRenderer.addItem(gameObjects[0]);
        testRenderer.addItem(gameObjects[1]);
        testRenderer.addItem(gameObjects[2]);
        testRenderer.addItem(gameObjects[3]);
        testRenderer.addItem(player);


        //カメラを作成
        camera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,-10f,10f,10f,0,0,0);
        camera.setFar(1000f);
        uiCamera = new Camera(Camera.CAMERA_MODE.ORTHO,0,0,10f,0,0,0);
        uiCamera.setNear(0.1f);
        testCamera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,0,0,0,-1);
        testCamera.setFar(1000f);
        testCamera.setNear(0.01f);

        //カメラを登録
        player.setCamera(testCamera);

        //シェーダ―に使用するカメラを登録
        shader.setCamera(camera);
        uiShader.setCamera(uiCamera);
        testShader.setCamera(testCamera);

        //ボタンを作成
        button = new Button(0,0.125f,0.125f,0);
        button.setCamera(uiCamera);
        button.setBackground(tex);
        button.setButtonListener(new TestButtonListener(player,po));

        //ナンバーテキスト
        nt = new NumberText("メイリオ");
        nt.setNumber(100);

        //テキスト
        tx = new Text("abcefghij");
        tx.sx = 0.5f;
        tx.sy = 0.5f;

        uiCamera.setPosition(GLES20Util.getAspect() / 2f, 0.5f, 0);
    }
    @Override
    public void Proc() {
        player.proc();
        //tempTouchProcess(Input.getTouchArray()[0]);
        for(int n = 0;n < Input.getMaxTouch();n++) {
            button.touch(Input.getTouchArray()[n]);
        }
        //gameObjects[3].getPos().setX(3f * (float)Math.sin(6.28f * ((float)count / 120f)));
        nt.setNumber(MainActivity.fpsController.getFps());
        button.proc();
        physics.simulate();
        tx.sx += 0.05f/60f;
        tx.sy = tx.sx;
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
/*testShader.useShader();
        testShader.updateCamera();
        for(int n = 0;n < 6;n++) {
            testShader.draw(plane,(float)n*0.1f,0,5f,0.5f,0.5f,0.5f,-90f,0,0,1);
        }*/
        //Log.d("Touch",Input.getTouchArray()[0].toString());
        testRenderer.drawAll();
        //renderer.drawAll();
        //shader.useShader();
        //shader.updateCamera();
        //shader.draw(gameObjects[0].getRenderMediator().mesh,0,0,0,1,1,1,0,0,0,1);
        //mode.draw(0, 0, 0, 1f, 1f, 1f, rotateX, rotateY, 0);


        uiShader.useShader();
        uiShader.updateCamera();

        nt.draw(uiShader);
        tx.draw(uiShader);

        button.draw(uiShader);
        //uiShader.draw(tex,0.125f,0.125f,0.25f,0.25f,0,1f);
        //uiShader.draw(tex,0.25f,0.25f,0.5f,0.5f,0,1f);

        /*
        //文字の描画
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0, 0, GLES20COMPOSITIONMODE.ALPHA);
        GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255,1f, 0.05f, 0,GLES20COMPOSITIONMODE.ALPHA);
        */
        //FPSの表示
        //GLES20Util.DrawFPS(0, 1.0f, fpsController.getFps(), fpsImage, 0.5f);
    }

    @Override
    public void Touch(MotionEvent event) {

    }

    @Override
    public void death() {

    }

    @Override
    public void freeze() {

    }

    @Override
    public void unFreeze() {

    }

    private void tempTouchProcess(Touch t){
        if(t.getTouchID() == -1)
            return;
        rotateX += t.getDelta(Touch.Pos_Flag.X) * 0.1f;
        rotateY += t.getDelta(Touch.Pos_Flag.Y) * 0.1f;

        if(rotateX <= -90f)
            rotateX = -89f;
        else if(rotateX >= 90f)
            rotateX = 89f;

        if(rotateY < -90f)
            rotateY = -90f;
        else if(rotateY > 90f)
            rotateY = 90f;

        player.getRot().setY(rotateY);
        player.getRot().setX(-rotateX);
    }

    class TestButtonListener implements ButtonListener {
        PhysicsObject target;
        Player player;
        public TestButtonListener(Player player,PhysicsObject target){
            this.target = target;
            this.player = player;
        }

        @Override
        public void touchDown(Button button) {

        }

        @Override
        public void touchHover(Button button) {

        }

        @Override
        public void touchUp(Button button) {
            target.gameObject.getPos().copy(player.getPos());
            target.gameObject.getRot().copy(player.getRot());
            target.velocity.zeroReset();
            target.velocity.setZ(-1);
            Vector.rotateX(Math.toRadians(player.getRot().getX()), target.velocity, target.velocity);
            Vector.rotateY(Math.toRadians(player.getRot().getY()), target.velocity, target.velocity);
            target.velocity.scalarMult(25f);
        }
    }
}
