package jp.ac.dendai.c.jtp.Game;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.util.Random;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Camera.UiCamera;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Shader.DiffuseShader;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsInfo;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class Constant {
    public enum SHADER{
        ui,
        diffuse
    }
    public enum BITMAP{
        white,
        black,
        bilinear,
        system_button
    }
    public static Bitmap tex_chash;
    public static final int COLLISION_PLAYERBULLET = 1;
    public static final int COLLISION_ENEMY = 2;
    public static final int COLLISION_PLAYDER = 4;
    public static final int COLLISION_ENEMYBULLET = 8;
    private static PhysicsInfo pi;
    private static Random ram;
    private static float sens = 1.0f;
    public static void setSens(float n){ sens = n;}
    public static float getSens(){return sens;}
    public static final String fontName = "custom_font.ttf";
    protected static Bitmap text_effect_white,text_effect_mask,system_button,black;
    protected static Camera activeUiCamera;
    protected static UiShader loadingShader;
    protected static UiCamera loadingCamera;
    protected static Shader uiShader,diffuseShader;
    protected static Mesh debugModel;
    protected static Camera debugCamera;

    public static void setPhysicsInfo(PhysicsInfo _pi){
        pi = _pi;
    }

    public static PhysicsInfo getPhysicsInfo(){
        return pi;
    }

    public static UiShader getLoadingShader(){
        return loadingShader;
    }

    public static void setDebugModel(Mesh mesh){
        debugModel = mesh;
    }

    public static void setDebugCamera(Camera c){
        debugCamera = c;
    }

    public static void debugDraw(float x,float y,float z,float lx,float ly,float lz,float dx,float dy,float dz,float alpha){
        //GLES20.glDepthMask(false);
        diffuseShader.useShader();
        diffuseShader.updateCamera();
        GLES20.glDepthMask(false);
        diffuseShader.draw(debugModel,x,y,z,lx,ly,lz,dx,dy,dz,alpha, GLES20COMPOSITIONMODE.ALPHA);
        GLES20.glDepthMask(true);
        //GLES20.glDepthMask(true);
    }

    public static UiCamera getLoadingCamera(){
        return loadingCamera;
    }

    public static Random getRandom(){
        return ram;
    }

    public static Camera getActiveUiCamera(){
        return activeUiCamera;
    }
    public static void setActiveUiCamera(Camera c){
        activeUiCamera = c;
    }
    public static void init(){
        if(text_effect_white == null)
            text_effect_white = GLES20Util.loadBitmap(R.mipmap.text_effect_white);
        if(text_effect_mask == null)
            text_effect_mask = GLES20Util.loadBitmap(R.mipmap.text_effect_mask);
        if(system_button == null)
            system_button = GLES20Util.loadBitmap(R.mipmap.button);
        if(black == null)
            black = GLES20Util.createBitmap(255,0,0,0);

        ram = new Random();

        //カメラ
        activeUiCamera = new UiCamera();
        loadingCamera = new UiCamera();

        loadingShader = new UiShader();
        loadingShader.setCamera(loadingCamera);

        diffuseShader = new DiffuseShader();
        uiShader = new UiShader();
        uiShader.setCamera(getActiveUiCamera());

        //UIを使えるようにする
        UI.init();
    }
    public static Bitmap getBitmap(BITMAP f){
        if(f == BITMAP.white)
            return text_effect_white;
        else if(f == BITMAP.bilinear)
            return text_effect_mask;
        else if(f == BITMAP.system_button)
            return system_button;
        else if(f == BITMAP.black){
            return black;
        }
        return text_effect_white;
    }
    public static Shader getShader(SHADER type){
        if(type == SHADER.ui)
            return uiShader;
        else if(type == SHADER.diffuse)
            return diffuseShader;
        else
            return null;
    }
}
