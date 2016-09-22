package jp.ac.dendai.c.jtp.defence3d;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Screen.DebugScreen;
import jp.ac.dendai.c.jtp.Game.Screen.StageSelectScreen;
import jp.ac.dendai.c.jtp.Game.Screen.TestModelViewScreen;
import jp.ac.dendai.c.jtp.Game.Screen.TestUIScreen;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.openglesutil.Util.ImageReader;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class MainActivity extends Activity implements GLSurfaceView.Renderer{

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int eventAction = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int ptrIndex = event.findPointerIndex(pointerId);
        Touch temp;

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                Input.addTouchCount();
                (Input.getTouch()).setTouch(event.getX(ptrIndex), event.getY(ptrIndex), pointerId);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Input.addTouchCount();
                if(Input.getTouchCount() <= Input.getMaxTouch()){
                    (Input.getTouch()).setTouch(event.getX(ptrIndex), event.getY(ptrIndex), pointerId);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                Input.subTouchCount();
                if((temp = Input.getTouch(pointerId)) != null){
                    temp.removeTouch();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Input.subTouchCount();
                if((temp = Input.getTouch(pointerId)) != null){
                    temp.removeTouch();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                //どれか一つでも移動された場合、全てのタッチ位置を更新する
                for(int n=0;n < Input.getMaxTouch();n++){
                    if((temp = Input.getTouchArray()[n]).getTouchID() != -1){
                        temp.updatePosition(event.getX(event.findPointerIndex(temp.getTouchID())),event.getY(event.findPointerIndex(temp.getTouchID())));
                        //Log.d("Input "+n,"Delta X :"+Input.getTouchArray()[n].getDelta(Touch.Pos_Flag.X));
                    }
                }
                break;
            default:
                for(int n=0;n < Input.getMaxTouch();n++){
                    if((temp = Input.getTouchArray()[n]).getTouchID() != -1){
                        temp.updatePosition(event.getX(event.findPointerIndex(temp.getTouchID())),event.getY(event.findPointerIndex(temp.getTouchID())));
                    }
                }
                break;
        }
        //pointerInfo.setText("pointerID:"+pointerId+" pointerIndex:"+pointerIndex+" ptrIndex:"+ptrIndex);
        //count.setText("count : " + Input.getTouchCount());
        //text1.setText(Input.getTouchArray()[0].toString());
        //text2.setText(Input.getTouchArray()[1].toString());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OpenGL ES 2.0が使用できるように初期化する
        GLSurfaceView glSurfaceView = GLES20Util.initGLES20(this, this);

        // GLSurfaceViewをこのアプリケーションの画面として使用する
        setContentView(glSurfaceView);

        //ファイルマネージャを使えるようにする
        FileManager.initFileManager(this);

        //イメージリーダーを使えるようにする
        ImageReader.initImageReader(this);

        //FPSマネージャを使えるようにする
        FpsController.initFpsController((short)60);

        //タッチマネージャーを使えるようにする
        Input.setMaxTouch(1);
        Input.setOrientation(getResources().getConfiguration().orientation);

        Log.d("onCreate", "onCreate finished");}

    @Override
    public void onDrawFrame(GL10 arg0) {
        // TODO 自動生成されたメソッド・スタブ
        process();
        draw();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        Log.d("MainActivity", "onSurfaceChanged");
        // 表示領域を設定する
        GLES20Util.initDrawErea(width, height, true);

        GameManager.init(this);
        GameManager.debugScreen = new DebugScreen();
        GameManager.debug = true;
        GameManager.nowScreen = new StageSelectScreen();
        GameManager.nowScreen.init();
        GameManager.nowScreen.unFreeze();

        //テクスチャの再読み込み
        //GLES20Util.initTextures();
        Log.d("onSurfaceCreated", "initShader");
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {


        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 画面をクリアする色を設定する
    }
    private void process(){
        FpsController.updateFps();
        Time.tick();
        GameManager.touch();
        GameManager.proc();
    }
    private void draw(){
        // 描画領域をクリアする
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GameManager.draw();
    }
}
