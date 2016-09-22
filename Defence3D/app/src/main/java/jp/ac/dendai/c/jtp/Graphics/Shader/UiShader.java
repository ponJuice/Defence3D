package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.textservice.TextInfo;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/08/30.
 */
public class UiShader extends Shader{
    private int uv_color;
    private int u_texPos;
    private int u_mask_pos;
    private int u_Sampler_mask;
    public UiShader(){
        super(FileManager.readTextFile("VSHADER.txt")
                ,FileManager.readTextFile("FSHADER.txt"));
    }

    @Override
    protected void createTexture() {
        textures = new int[2];
        // テクスチャオブジェクトを作成する
        GLES20.glGenTextures(2, textures, 0);
    }

    @Override
    protected void _useShader(){
        super._useShader();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        if(!GLES20.glIsEnabled(GLES20.GL_BLEND))
            GLES20.glEnable(GLES20.GL_BLEND);
    }

    @Override
    void loadShaderVariable() {
        u_Sampler = GLES20Util.getUniformLocation(program, "u_Sampler");
        uv_color = GLES20Util.getUniformLocation(program,"u_color");
        u_mask_pos = GLES20Util.getUniformLocation(program,"u_mask_pos");
        u_texPos = GLES20Util.getUniformLocation(program,"u_texPos");
        u_Sampler_mask = GLES20Util.getUniformLocation(program,"u_Sampler_mask");
    }

    @Override
    void setMaterial(Face face) {
        setOnTexture(face.matelial.tex_diffuse,u_Sampler);
    }

    @Override
    public void draw(Model model, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ,GLES20COMPOSITIONMODE mode) {

    }

    @Override
    public void draw(Mesh mesh, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ,float alpha,GLES20COMPOSITIONMODE mode) {

    }

    @Override
    public void draw(Texture tex, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ, float alpha) {

    }

    public void drawUi(UI tex, float x, float y, float lengthX, float lengthY, float degree, float alpha){
        //裏面を表示しない
        //GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);

        //float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, -10f);
        Matrix.scaleM(modelMatrix, 0, lengthX, lengthY, 1.0f);
        Matrix.rotateM(modelMatrix, 0, degree, 0, 0, 1);
        setShaderModelMatrix(modelMatrix);

        setOnTexture(tex.getBitmap(),tex);
        setOnMask(tex.getMaskBitmap()
                ,tex.getMaskOffset(TextureInfo.UV.u),tex.getMaskOffset(TextureInfo.UV.v)
                ,tex.getMaskScale(TextureInfo.UV.u),tex.getMaskScale(TextureInfo.UV.v)
                ,tex);
        GLES20.glUniform3f(uv_color,tex.getR(),tex.getG(),tex.getB());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getVBO());
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getIBO());
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        tex.getBlendMode().setBlendMode();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);	//描画

        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }

    /**
     * テクスチャ画像を設定する
     */
    //テクスチャ画像を設定する
    protected void setOnTexture(Bitmap image,TextureInfo t){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);   // テクスチャユニット0を有効にする

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]); // テクスチャオブジェクトをバインドする

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, t.getWrapModeS());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, t.getWrapModeT());

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, t.getFilterModeMin());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, t.getFilterModeMag());
        // テクスチャ画像を設定する
        chasedTexImage2D(image);

        GLES20.glUniform4f(u_texPos,t.getTexOffset(TextureInfo.UV.u),t.getTexOffset(TextureInfo.UV.v),t.getTexScale(TextureInfo.UV.u),t.getTexScale(TextureInfo.UV.v));
        GLES20.glUniform1f(u_alpha, t.getAlpha());		//サンプラにアルファを設定する
        GLES20.glUniform1i(u_Sampler, 0);     // サンプラにテクスチャユニットを設定する
    }

    /**
     * テクスチャ画像を設定する
     */
    //テクスチャ画像を設定する
    protected void setOnTexture(Bitmap image,float alpha,int ws,int wt,int min,int mag,float ox,float oy,float sx,float sy){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);   // テクスチャユニット0を有効にする

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]); // テクスチャオブジェクトをバインドする

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, ws);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wt);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, min);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, mag);
        // テクスチャ画像を設定する
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);

        GLES20.glUniform4f(u_texPos,ox,oy,sx,sy);
        GLES20.glUniform1f(u_alpha, alpha);		//サンプラにアルファを設定する
        GLES20.glUniform1i(u_Sampler, 0);     // サンプラにテクスチャユニットを設定する
    }

    //マスクの設定
    protected void setOnMask(Bitmap mask, float offset_x, float offset_y, float scale_x, float scale_y, MaskInfo m){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, m.getMaskWrapModeS());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, m.getMaskWrapModeT());

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, m.getMaskFilterModeMin());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, m.getMaskFilterModeMag());
        if(mask == null)
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,Constant.getBitmap(Constant.BITMAP.white),0);
        else
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mask,0);
        GLES20.glUniform1i(u_Sampler_mask, 1);

        GLES20.glUniform4f(u_mask_pos, offset_x, offset_y, scale_x, scale_y);
    }

    //マスクの設定
    protected void setOnMask(Bitmap mask,float alpha,int ws,int wt,int min,int mag,float ox,float oy,float sx,float sy){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, ws);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wt);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, min);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, mag);
        if(mask == null)
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,Constant.getBitmap(Constant.BITMAP.white),0);
        else
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mask,0);
        GLES20.glUniform1i(u_Sampler_mask, 1);

        GLES20.glUniform4f(u_mask_pos, ox, oy, sx, sy);
    }

    @Override
    protected void _clear(){

    }
}
