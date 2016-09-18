package jp.ac.dendai.c.jtp.Graphics.UI.Image;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Vector2;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/09/16.
 */
public class Image extends UI{
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
    protected Bitmap image;
    protected Bitmap mask;
    protected float aspect;

    public void setWrap_s(int wrap_s) {
        this.wrap_s = wrap_s;
    }

    public void setWrap_t(int wrap_t) {
        this.wrap_t = wrap_t;
    }

    public void setFilter_min(int filter_min) {
        this.filter_min = filter_min;
    }

    public void setFilter_mag(int filter_mag) {
        this.filter_mag = filter_mag;
    }

    public void setMask_warp_s(int mask_warp_s) {
        this.mask_warp_s = mask_warp_s;
    }

    public void setMast_warp_t(int mast_warp_t) {
        this.mast_warp_t = mast_warp_t;
    }

    public void setMask_filter_min(int mask_filter_min) {
        this.mask_filter_min = mask_filter_min;
    }

    public void setMask_filter_mag(int mask_filter_mag) {
        this.mask_filter_mag = mask_filter_mag;
    }

    protected int wrap_s = GLES20.GL_REPEAT
            , wrap_t = GLES20.GL_REPEAT
            ,filter_min = GLES20.GL_NEAREST
            ,filter_mag = GLES20.GL_NEAREST
            ,mask_warp_s = GLES20.GL_CLAMP_TO_EDGE
            ,mast_warp_t = GLES20.GL_CLAMP_TO_EDGE
            ,mask_filter_min = GLES20.GL_NEAREST
            ,mask_filter_mag = GLES20.GL_NEAREST;
    protected UIAlign.Align holizontal = UIAlign.Align.CENTOR,vertical = UIAlign.Align.CENTOR;
    protected float width,height;
    protected Vector2 texOffset = new Vector2(),texScale = new Vector2(1,1);
    protected float alpha = 1;
    protected float mask_alpha = 1;
    protected float x = 0,y = 0;
    protected float r = 0.5f,g = 0.5f,b = 0.5f;
    protected float degree = 0;
    protected float mask_offset_u = 0
            ,mask_offset_v = 0
            ,mask_scale_u = 1
            ,mask_scale_v = 1;
    protected GLES20COMPOSITIONMODE mode = GLES20COMPOSITIONMODE.ALPHA;
    public void setBlendMode(GLES20COMPOSITIONMODE m){
        mode = m;
    }
    public Image(Bitmap bitmap){
        image = bitmap;
        aspect = (float)image.getWidth()/(float)image.getHeight();
        setBufferObject();
    }
    public Image(Image image){
        this.image = image.getImage();
        aspect =image.aspect;

        holizontal = image.holizontal;
        vertical = image.vertical;
        width = image.width;
        height = image.height;
        alpha = image.alpha;
        x = image.x;
        y = image.y;
        mode = image.mode;
    }
    public void setWidth(float width){
        this.width = width;
        this.height = width/aspect;
    }
    public void setHeight(float height){
        this.height = height;
        this.width = height*aspect;
    }
    @Override
    public void setAlpha(float a){
        alpha = a;
    }

    @Override
    public Vector2 getTexOffset() {
        return texOffset;
    }

    @Override
    public Vector2 getTexScale() {
        return texScale;
    }

    public float getDegree(){
        return degree;
    }

    public void setDegree(float a){
        degree = a;
    }

    @Override
    public Bitmap getBitmap() {
        return image;
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public Bitmap getMaskBitmap() {
        return mask;
    }

    @Override
    public void setMaskBitmap(Bitmap bitmap) {
        this.mask = bitmap;
    }

    @Override
    public float getMaskOffset(MASK flag) {
        if(flag == MASK.u)
            return mask_offset_u;
        else
            return mask_offset_v;
    }

    @Override
    public float getMaskScale(MASK flag) {
        if(flag == MASK.u)
            return mask_scale_u;
        else
            return mask_scale_v;
    }

    @Override
    public void setMaskOffset(MASK flag, float n) {
        if(flag == MASK.u)
            mask_offset_u = n;
        else
            mask_offset_v = n;
    }

    @Override
    public void setMaskScale(MASK flag, float n) {
        if(flag == MASK.u)
            mask_scale_u = n;
        else
            mask_scale_v = n;
    }

    @Override
    public float getMaskAlpha() {
        return mask_alpha;
    }

    @Override
    public void setMaskAlpha(float a) {
        mask_alpha = a;
    }

    @Override
    public int getFilterModeMin() {
        return filter_min;
    }

    @Override
    public int getFilterModeMag() {
        return filter_mag;
    }

    @Override
    public int getWrapModeT() {
        return wrap_t;
    }

    @Override
    public int getWrapModeS() {
        return wrap_s;
    }

    @Override
    public int getMaskWrapModeT() {
        return mast_warp_t;
    }

    @Override
    public int getMaskWrapModeS() {
        return mask_warp_s;
    }

    @Override
    public int getMaskFilterModeMin() {
        return mask_filter_min;
    }

    @Override
    public int getMaskFilterModeMag() {
        return mask_filter_mag;
    }

    @Override
    public GLES20COMPOSITIONMODE getBlendMode() {
        return mode;
    }

    @Override
    public float getColor(COLOR col)
    {
        if(col == COLOR.R)
            return r;
        else if(col == COLOR.G)
            return g;
        else
            return b;
    }

    @Override
    public int getVBO() {
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create vertex buffer object");
        return bufferObject[0];
    }

    @Override
    public int getIBO() {
        if(bufferObject[0] == -1)
            throw new RuntimeException("[Texture] not create texture buffer object");
        return bufferObject[1];
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        //GLES20Util.DrawGraph(x + UIAlign.convertAlign(width,holizontal),y + UIAlign.convertAlign(height,vertical),width,height,image,alpha, mode);
        shader.drawUi(this,x + UIAlign.convertAlign(width,holizontal),y + UIAlign.convertAlign(height,vertical),
                width,height,degree,alpha);
    }
    public void setBufferObject(){
        if(bufferObject[0] != -1 && bufferObject[1] != -1)
            return;
        FloatBuffer vertexBuffer = Mesh.makeFloatBuffer(plane);
        FloatBuffer texPosBuffer = Mesh.makeFloatBuffer(texPos);
        bufferObject = GLES20Util.createBufferObject(2);
        GLES20Util.setVertexBuffer(bufferObject[0], vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20Util.setVertexBuffer(bufferObject[1],texPosBuffer,GLES20.GL_STATIC_DRAW);
    }
    public void deleteBufferObject(){
        GLES20.glDeleteBuffers(2,bufferObject,0);
        bufferObject[0] = -1;
        bufferObject[1] = -1;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public GLES20COMPOSITIONMODE getMode() {
        return mode;
    }

    public void setMode(GLES20COMPOSITIONMODE mode) {
        this.mode = mode;
    }

    public UIAlign.Align getVertical() {
        return vertical;
    }

    public void setVertical(UIAlign.Align vertical) {
        this.vertical = vertical;
    }

    public UIAlign.Align getHolizontal() {
        return holizontal;
    }

    public void setHolizontal(UIAlign.Align holizontal) {
        this.holizontal = holizontal;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getImage(){
        return image;
    }
}
