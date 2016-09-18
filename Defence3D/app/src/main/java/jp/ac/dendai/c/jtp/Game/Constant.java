package jp.ac.dendai.c.jtp.Game;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/08.
 */
public class Constant {
    public enum BITMAP{
        white,
        black,
        bilinear,
        system_button
    }
    public static final int COLLISION_PLAYERBULLET = 1;
    public static final int COLLISION_ENEMY = 2;
    private static float sens = 1.0f;
    public static void setSens(float n){ sens = n;}
    public static float getSens(){return sens;}
    public static final String fontName = "custom_font.ttf";
    protected static Bitmap text_effect_white,text_effect_mask,system_button,black;
    protected static Camera activeUiCamera;
    protected static UiShader loadingShader;

    public static UiShader getLoadingShader(){
        return loadingShader;
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

}
