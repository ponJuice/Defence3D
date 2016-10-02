package jp.ac.dendai.c.jtp.Game.Screen;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.SoundPool;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.Score.ScoreManager;
import jp.ac.dendai.c.jtp.Game.Score.ScorePacage;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingThread;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.LightedDiffuseShader;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.ModelConverter.Wavefront.WavefrontObjConverter;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by Goto on 2016/09/29.
 */

public class StartScreen extends Screenable {
    protected enum STATE{
        OPENING,
        MOVE,
        MODE_SELECT
    }
    protected STATE state = STATE.OPENING;
    protected DiffuseShader l_diffuseShader;
    protected Renderer renderer;
    protected Mesh text_3d;
    protected Plane title_mesh,push,mode_mesh;
    protected Camera camera;
    protected Bitmap title_image,mode_select_string,tap_start;
    protected GameObject title_plane1,title_plane2,title_plane3,text_3d_object,score1,score2,score3;
    protected GameObject mode_select_plane,tap_start_object;
    protected Vector3 bezie_start,bezie_end,bezie1,bezie2,target_pos;
    protected float timeBuffer,moveTime;
    protected float length,offset = 1f;
    protected float alphaTimeBuffer = 0;
    protected MediaPlayer mp;
    protected int bgm;

    public StartScreen(){
        ScoreManager.loadScore();
        camera = new Camera(Camera.CAMERA_MODE.PERSPECTIVE,0,0,-5f,0,0,0);
        l_diffuseShader = (DiffuseShader)Constant.getShader(Constant.SHADER.diffuse);
        l_diffuseShader.setCamera(camera);
        l_diffuseShader.setFogColor(0,0,0);
        l_diffuseShader.setFogDist(10f,15f);
        GameManager.setClearColor(GameManager.COLOR.R,0);
        GameManager.setClearColor(GameManager.COLOR.G,0);
        GameManager.setClearColor(GameManager.COLOR.B,0);

        renderer = new Renderer();
        renderer.setShader(l_diffuseShader);

        text_3d = WavefrontObjConverter.createModel("textobject.obj");

        title_image = GLES20Util.loadBitmap(R.mipmap.title);
        float aspect = (float)title_image.getWidth() / (float)title_image.getHeight();

        mode_select_string = GLES20Util.stringToBitmap("ENDLESS MODE",50,255,255,255);
        tap_start = GLES20Util.stringToBitmap("TAP START",25,255,255,255);
        
        title_mesh = new Plane();
        title_mesh.setImage(title_image);

        mode_mesh = new Plane();
        mode_mesh.setImage(mode_select_string);

        title_plane1 = new GameObject();
        title_plane1.getRenderMediator().mesh = title_mesh;
        title_plane1.getRenderMediator().isDraw = true;
        title_plane1.getScl().setZ(0.5f);
        title_plane1.getScl().setX(aspect * 0.5f);
        title_plane1.getRot().setX(-90f);
        title_plane1.getRot().setZ(180f);
        title_plane1.getPos().setY(1f);

        title_plane2 = new GameObject();
        title_plane2.getRenderMediator().mesh = title_mesh;
        title_plane2.getRenderMediator().isDraw = true;
        title_plane2.getScl().setZ(0.5f);
        title_plane2.getScl().setX(aspect * 0.5f);
        title_plane2.getRot().setX(-90f);
        title_plane2.getRot().setZ(180f);
        title_plane2.getPos().setY(1f);
        title_plane2.getPos().setZ(0.2f);

        title_plane3 = new GameObject();
        title_plane3.getRenderMediator().mesh = title_mesh;
        title_plane3.getRenderMediator().isDraw = true;
        title_plane3.getScl().setZ(0.5f);
        title_plane3.getScl().setX(aspect * 0.5f);
        title_plane3.getRot().setX(-90f);
        title_plane3.getRot().setZ(180f);
        title_plane3.getPos().setY(1f);
        title_plane3.getPos().setZ(0.4f);

        text_3d_object = new GameObject();
        text_3d_object.getRenderMediator().mesh = text_3d;
        text_3d_object.getRenderMediator().isDraw = true;
        text_3d_object.getRot().setY(180f);
        text_3d_object.getPos().setY(-0.5f);

        aspect = (float)mode_select_string.getWidth() / (float)mode_select_string.getHeight();

        mode_select_plane = new GameObject();
        mode_select_plane.getRenderMediator().mesh = mode_mesh;
        mode_select_plane.getRenderMediator().isDraw = false;
        mode_select_plane.getRot().setX(-90f);
        mode_select_plane.getRot().setZ(180f);
        mode_select_plane.getScl().setZ(0.5f);
        mode_select_plane.getScl().setX(aspect*0.5f);
        mode_select_plane.getPos().setZ(10f);
        mode_select_plane.getPos().setY(0.7f);

        ScorePacage sp = ScoreManager.getScore("ENDLESS");
        Bitmap def_score = GLES20Util.stringToBitmap("----/--/-- --:--:-- ----",25,255,255,255);
        Bitmap[] score_image = new Bitmap[3];
        for(int n = 0;n < score_image.length;n++){
            if(sp.scores.size() > n){
                score_image[n] = GLES20Util.stringToBitmap(sp.scores.get(n).getFormatString(),25,255,255,255);
            }else{
                score_image[n] = def_score;
            }
        }

        aspect = (float)score_image[0].getWidth() / (float)score_image[0].getHeight();
        score1 = new GameObject();
        score1.getRenderMediator().mesh = new Plane();
        score1.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = score_image[0];
        score1.getRenderMediator().isDraw = false;
        score1.getRenderMediator().mode = GLES20COMPOSITIONMODE.ALPHA;
        score1.getRot().setX(-90f);
        score1.getRot().setZ(180f);
        score1.getScl().setZ(0.5f);
        score1.getPos().setZ(10f);
        score1.getPos().setY(0.3f);
        score1.getScl().setZ(0.3f);
        score1.getScl().setX(aspect*0.3f);

        score2 = new GameObject();
        score2.getRenderMediator().mesh = new Plane();
        score2.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = score_image[1];
        score2.getRenderMediator().isDraw = false;
        score2.getRenderMediator().mode = GLES20COMPOSITIONMODE.ALPHA;
        score2.getRot().setX(-90f);
        score2.getRot().setZ(180f);
        score2.getScl().setZ(0.5f);
        score2.getPos().setZ(10f);
        score2.getPos().setY(0.0f);
        score2.getScl().setZ(0.3f);
        score2.getScl().setX(aspect*0.3f);
        
        score3 = new GameObject();
        score3.getRenderMediator().mesh = new Plane();
        score3.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = score_image[2];
        score3.getRenderMediator().isDraw = false;
        score3.getRenderMediator().mode = GLES20COMPOSITIONMODE.ALPHA;
        score3.getRot().setX(-90f);
        score3.getRot().setZ(180f);
        score3.getScl().setZ(0.5f);
        score3.getPos().setZ(10f);
        score3.getPos().setY(-0.3f);
        score3.getScl().setZ(0.3f);
        score3.getScl().setX(aspect*0.3f);

        aspect = (float)tap_start.getWidth() / (float)tap_start.getHeight();
        tap_start_object = new GameObject();
        tap_start_object.getRenderMediator().mesh = new Plane();
        tap_start_object.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = tap_start;
        tap_start_object.getRenderMediator().isDraw = true;
        tap_start_object.getRenderMediator().mode = GLES20COMPOSITIONMODE.ALPHA;
        tap_start_object.getRot().setX(-90f);
        tap_start_object.getRot().setZ(180f);
        tap_start_object.getScl().setZ(0.2f);
        //tap_start_object.getPos().setZ(10f);
        tap_start_object.getPos().setY(-1.3f);
        tap_start_object.getScl().setZ(0.6f);
        tap_start_object.getScl().setX(aspect*0.6f);
        
        renderer.addItem(title_plane1);
        renderer.addItem(title_plane2);
        renderer.addItem(title_plane3);
        renderer.addItem(text_3d_object);
        renderer.addItem(mode_select_plane);
        renderer.addItem(score1);
        renderer.addItem(score2);
        renderer.addItem(score3);
        renderer.addItem(tap_start_object);

        length = Math.abs(text_3d_object.getPos().getZ()-offset - camera.getPosition(Camera.POSITION.Z));

        target_pos = new Vector3(camera.getPosition(Camera.POSITION.X),camera.getPosition(Camera.POSITION.Y),camera.getPosition(Camera.POSITION.Z));
        bezie_start = new Vector3(camera.getPosition(Camera.POSITION.X),camera.getPosition(Camera.POSITION.Y),camera.getPosition(Camera.POSITION.Z));
        bezie_end = new Vector3(0,0,7f);
        bezie1 = new Vector3(0,0,-5f);
        bezie2 = new Vector3(0,0,7f);
        moveTime = 1f;

        onResume();
    }
    @Override
    public void Proc() {
        if(state == STATE.OPENING){
            tap_start_object.getRenderMediator().alpha = (float)Math.sin(2.0*3.19*0.5*alphaTimeBuffer);
            alphaTimeBuffer += Time.getDeltaTime();
        }
        if(state == STATE.MOVE) {
            Clamp.bezier3Trajectory(target_pos, bezie_start, bezie_end, bezie1, bezie2, 1f / moveTime * timeBuffer);
            timeBuffer += Time.getDeltaTime();
        }
        if(state == STATE.OPENING || state == STATE.MOVE) {
            camera.setPosition(0, 0, target_pos.getZ());
            camera.setLookPosition(0, 0, target_pos.getZ() + 0.05f);
            text_3d_object.getRenderMediator().alpha = (1f / length * Math.abs(text_3d_object.getPos().getZ() - offset - camera.getPosition(Camera.POSITION.Z)));
        }
        if(state == STATE.MOVE){
            mode_select_plane.getRenderMediator().isDraw = true;
            score1.getRenderMediator().isDraw = true;
            score2.getRenderMediator().isDraw = true;
            score3.getRenderMediator().isDraw = true;
            tap_start_object.getPos().setZ(10f);
            tap_start_object.getPos().setY(-0.8f);
            tap_start_object.getRenderMediator().alpha = 0;
            alphaTimeBuffer = 0;
        }
        if(state == STATE.MODE_SELECT){
            text_3d_object.getRenderMediator().isDraw = false;
            title_plane1.getRenderMediator().isDraw = false;
            title_plane2.getRenderMediator().isDraw = false;
            title_plane3.getRenderMediator().isDraw = false;
            tap_start_object.getRenderMediator().alpha = (float)Math.sin(2.0*3.19*0.5*alphaTimeBuffer);
            alphaTimeBuffer += Time.getDeltaTime();
        }
        if(timeBuffer != -1 && timeBuffer > moveTime){
            timeBuffer = -1;
            state = STATE.MODE_SELECT;
        }

    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        renderer.drawAll();
    }

    @Override
    public void Touch() {
        if(state == STATE.OPENING){
            for(int n = 0;n < Input.getTouchArray().length;n++){
                if(Input.getTouchArray()[n].getTouchID() != -1)
                    state = STATE.MOVE;
            }
        }else if(state == STATE.MODE_SELECT){
            for(int n = 0;n < Input.getTouchArray().length;n++){
                if(Input.getTouchArray()[n].getTouchID() != -1) {
                    LoadingTransition lt = LoadingTransition.getInstance();
                    lt.initTransition(TestGameScreen.class);
                    GameManager.transition = lt;
                    GameManager.isTransition = true;
                }
            }
        }
    }

    @Override
    public void death() {
        title_mesh.deleteBufferObject();
        text_3d.deleteBufferObject();
        onPause();
    }

    @Override
    public void init() {
        title_mesh.useBufferObject();
        text_3d.useBufferObject();
    }

    @Override
    public void onPause() {
        mp.stop();
        mp.release();
    }

    @Override
    public void onResume() {
        if(mp == null)
            mp = MediaPlayer.create(GameManager.getAct(),R.raw.start_bgm);
        mp.setLooping(true);
        mp.start();
    }
}
