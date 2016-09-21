package jp.ac.dendai.c.jtp.Graphics.UI;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/09/06.
 */
public abstract class UI implements TextureInfo,MaskInfo {
    protected static final float[] plane = {
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            -0.5f,0.5f,0,
            0.5f,0.5f,0
            /*0,0,0,
            1f,0,0,
            0,1f,0,
            1f,1f,0*/
    };
    protected static final float[] texPos = {
            0.0f,1.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            1.0f,0.0f
    };
    protected static int[] bufferObject = {-1,-1};  //0:頂点 1:テクスチャ座標

    protected UIAlign.Align horizontal = UIAlign.Align.CENTOR,vertical = UIAlign.Align.CENTOR;
    protected float width,height,x,y;
    protected float aspect; //縦横比
    protected boolean useAspect = true;
    protected float alpha = 1;
    protected float degree = 0;
    protected GLES20COMPOSITIONMODE mode = GLES20COMPOSITIONMODE.ALPHA;
    protected float r = 0.5f,g = 0.5f,b = 0.5f;

    public void copy(UI ui) {
        horizontal = ui.horizontal;
        vertical = ui.vertical;
        width = ui.width;
        height = ui.height;
        x = ui.x;
        y = ui.y;
        aspect = ui.aspect;
        useAspect = ui.useAspect;
        alpha = ui.alpha;
        degree = ui.degree;
        mode = ui.mode;
        r = ui.r;
        g = ui.g;
        b = ui.b;
    }

    public GLES20COMPOSITIONMODE getBlendMode(){
        return mode;
    }
    public void setBlendMode(GLES20COMPOSITIONMODE mode){
        this.mode = mode;
    }
    public void setR(float r){
        this.r = r;
    }
    public float getR(){
        return r;
    }
    public void setG(float g){
        this.g = g;
    }
    public float getG(){
        return g;
    }
    public void setB(float b){
        this.b = b;
    }
    public float getB(){
        return b;
    }
    public int getVBO() {
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create vertex buffer object");
        return bufferObject[0];
    }
    public int getIBO() {
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create texture buffer object");
        return bufferObject[1];
    }
    public void useAspect(boolean flag){
        useAspect = flag;
        if(useAspect)
            aspect = calcAspect(getBitmap());
    }
    public void setWidth(float width){
        this.width = width;
        if(useAspect){
            height = width / aspect;
        }
    }
    public void setHeight(float height){
        this.height = height;
        if(useAspect){
            width = height * aspect;
        }
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getAspect(){
        return aspect;
    }
    public void setAspect(float a){
        aspect = a;
    }
    public boolean isUseAspect(){
        return useAspect;
    }
    public void setHorizontal(UIAlign.Align align){
        horizontal = align;
    }
    public void setVertical(UIAlign.Align align){
        vertical = align;
    }
    public UIAlign.Align getHolizontal(){
        return horizontal;
    }
    public UIAlign.Align getVertical(){
        return vertical;
    }
    public void setX(float x){
        this.x = x + UIAlign.convertAlign(width,horizontal);
    }
    public void setY(float y){
        this.y = y + UIAlign.convertAlign(height,vertical);
    }
    @Override
    public void setAlpha(float a){
        alpha = a;
    }
    @Override
    public float getAlpha(){
        return alpha;
    }
    public void setDegree(float d){
        degree = d;
    }
    public float getDegree(){
        return degree;
    }

    public static void init(){
        if(bufferObject[0] != -1 && bufferObject[1] != -1)
            return;
        FloatBuffer vertexBuffer = Mesh.makeFloatBuffer(plane);
        FloatBuffer texPosBuffer = Mesh.makeFloatBuffer(texPos);
        bufferObject = GLES20Util.createBufferObject(2);
        GLES20Util.setVertexBuffer(bufferObject[0], vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20Util.setVertexBuffer(bufferObject[1],texPosBuffer,GLES20.GL_STATIC_DRAW);
    }
    protected static float calcAspect(Bitmap image){
        return (float)image.getWidth()/(float)image.getHeight();
    }

    public abstract void touch(Touch touch);
    public abstract void proc();
    public abstract void draw(UiShader shader);
}
