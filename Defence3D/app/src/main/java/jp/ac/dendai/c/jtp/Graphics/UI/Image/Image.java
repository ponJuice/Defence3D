package jp.ac.dendai.c.jtp.Graphics.UI.Image;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureSetting;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Math.Vector2;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by wark on 2016/09/16.
 */
public class Image extends UI{
    protected Bitmap mask;
    protected Bitmap bitmap;

    protected TextureSetting textureSetting = new TextureSetting();
    protected Vector2 texOffset = new Vector2(),texScale = new Vector2(1,1);
    protected Vector2 maskOffset = new Vector2(),maskScale = new Vector2(1,1);
    protected float mask_alpha = 1;
    public Image(Bitmap bitmap){
        this.bitmap = bitmap;
        aspect = calcAspect(bitmap);
        height = 1;
        width = height * aspect;
    }
    public Image(Image image){
        this.copy(image);
    }

    public void copy(Image image){
        ((UI)this).copy(image);
        mask = image.mask;
        bitmap = image.bitmap;
        mask_alpha = image.mask_alpha;
        textureSetting.copy(image.textureSetting);
        texOffset.copy(image.texOffset);
        texScale.copy(image.texScale);
        maskOffset.copy(image.maskOffset);
        maskScale.copy(image.maskScale);
    }

    public Image(){
    }

    public void setWrap_s(int wrap_s) {
        textureSetting.setWrap_s(wrap_s);
    }

    public void setWrap_t(int wrap_t) {
        textureSetting.setWrap_t(wrap_t);
    }

    public void setFilter_min(int filter_min) {
        textureSetting.setFilter_min(filter_min);
    }

    public void setFilter_mag(int filter_mag) {
        textureSetting.setFilter_mag(filter_mag);
    }

    public void setMask_warp_s(int mask_warp_s) {
        textureSetting.setMask_warp_s(mask_warp_s);
    }

    public void setMask_warp_t(int mask_warp_t) {
        textureSetting.setMask_warp_t(mask_warp_t);
    }

    public void setMask_filter_min(int mask_filter_min) {
        textureSetting.setMask_filter_min(mask_filter_min);
    }

    public void setMask_filter_mag(int mask_filter_mag) {
        textureSetting.setMask_filter_mag(mask_filter_mag);
    }

    @Override
    public float getTexOffset(UV flag) {
        if(flag == UV.u)
            return texOffset.getX();
        else
            return texOffset.getY();
    }

    @Override
    public float getTexScale(UV flag) {
        if(flag == UV.u)
            return texScale.getX();
        else
            return texScale.getY();
    }

    @Override
    public void setTexOffset(UV flag,float n){
        if(flag == UV.u)
            texOffset.setX(n);
        else
            texOffset.setY(n);
    }

    @Override
    public void setTexScale(UV flag,float n){
        if(flag == UV.u)
            texScale.setX(n);
        else
            texScale.setY(n);
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
    public float getMaskOffset(UV flag) {
        if(flag == UV.u)
            return maskOffset.getX();
        else
            return maskOffset.getY();
    }

    @Override
    public float getMaskScale(UV flag) {
        if(flag == UV.u)
            return maskScale.getX();
        else
            return maskScale.getY();
    }

    @Override
    public void setMaskOffset(UV flag, float n) {
        if(flag == UV.u)
            maskOffset.setX(n);
        else
            maskOffset.setY(n);
    }

    @Override
    public void setMaskScale(UV flag, float n) {
        if(flag == UV.u)
            maskScale.setX(n);
        else
            maskScale.setY(n);
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
        return textureSetting.getFilter_min();
    }

    @Override
    public int getFilterModeMag() {
        return textureSetting.getFilter_mag();
    }

    @Override
    public int getWrapModeT() {
        return textureSetting.getWrap_t();
    }

    @Override
    public int getWrapModeS() {
        return textureSetting.getWrap_s();
    }

    @Override
    public int getMaskWrapModeT() {
        return textureSetting.getMask_warp_t();
    }

    @Override
    public int getMaskWrapModeS() {
        return textureSetting.getMask_warp_s();
    }

    @Override
    public int getMaskFilterModeMin() {
        return textureSetting.getMask_filter_min();
    }

    @Override
    public int getMaskFilterModeMag() {
        return textureSetting.getMask_filter_mag();
    }

    @Override
    public Bitmap getBitmap(){
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        aspect = calcAspect(bitmap);
    }

    public void refreshWidth(){
        setHeight(height);
    }

    public void refreshHeight(){
        setWidth(width);
    }

    @Override
    public boolean touch(Touch touch) {
        return through;
    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        //GLES20Util.DrawGraph(x + UIAlign.convertAlign(width,holizontal),y + UIAlign.convertAlign(height,vertical),width,height,image,alpha, mode);
        shader.drawUi(this,x,y,
                width,height,degree,alpha);
    }
}
