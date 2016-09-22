package jp.ac.dendai.c.jtp.Graphics.UI.Slider;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.Util.Figure.Rect;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/11.
 */
public class Slider extends UI {
    public enum SLIDER_ORIENT{
        portrait,
        landscape
    }
    //内部では0～1で処理する
    protected float min = 0,max = 1,innerValue = 0.5f;
    protected SLIDER_ORIENT slider_orient;
    protected Rect slider,line;
    protected Image slider_image,line_image;

    public Slider(float cx,float cy,float line_width,float line_height,float slide_width,float slide_height,SLIDER_ORIENT s){
        this.slider_orient = s;
        this.width = Math.max(line_width,slide_width);
        this.height = Math.max(line_height,slide_height);
        this.x = cx;
        this.y = cy;
        line = new Rect(cx - line_width/2f,cy + line_height/2f,cx + line_width/2f,cy - line_height/2f);
        slider = new Rect(cx - slide_width/2f,cy + slide_height/2f,cx + slide_width/2f,cy - slide_height/2f);
        line_image = new Image(Constant.getBitmap(Constant.BITMAP.system_button));
        line_image.useAspect(false);
        line_image.setWidth(line_width);
        line_image.setHeight(line_height);
        line_image.setX(cx);
        line_image.setY(cy);

        slider_image = new Image(Constant.getBitmap(Constant.BITMAP.system_button));
        slider_image.useAspect(false);
        slider_image.setWidth(slide_width);
        slider_image.setHeight(slide_height);
        slider_image.setX(cx);
        slider_image.setY(cy);
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getNowValue() {
        return innerValue * max + min;
    }

    public void setNowValue(float nowValue) {
        if(nowValue > max)
            nowValue = max;
        else if(nowValue < min)
            nowValue = min;
        this.innerValue = (max - min) / nowValue;
    }

    protected void updateSliderPos(){
        if(slider_orient == SLIDER_ORIENT.landscape) {
            slider.setCx(line.getCx()-(max-min)*innerValue/2f);
        }else{
            slider.setCy(line.getCy()-(max -min)*innerValue/2f);
        }
    }
    Touch touch;
    @Override
    public void touch(Touch touch) {
        /*if(this.touch == null && touch.getTouchID() == -1)
            return;
        if(this.touch == null)
            this.touch = touch;
        else if(this.touch != touch || this.touch.getTouchID() == -1)
            return;*/

        float _x = Constant.getActiveUiCamera().convertTouchPosToGLPosX(touch.getPosition(Touch.Pos_Flag.X));
        float _y = Constant.getActiveUiCamera().convertTouchPosToGLPosY(touch.getPosition(Touch.Pos_Flag.Y));

        if(this.touch != null && this.touch.getTouchID() == -1) {
            this.touch = null;
            return;
        }

        if(slider.contains(_x,_y) || this.touch == touch){
            if(this.touch == null)
                this.touch = touch;

            slider_image.setY(_y);
            slider.setCy(_y);

        }else if(line.contains(_x,_y)){
            if(this.touch == null)
                this.touch = touch;

            slider_image.setY(_y);
            slider.setCy(_y);
        }
    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        shader.drawUi(line_image,line_image.getX(),line_image.getY(),line_image.getWidth(),line_image.getHeight(),0,1);
        shader.drawUi(slider_image,slider_image.getX(),slider_image.getY(),slider_image.getWidth(),slider_image.getHeight(),0,1);
    }

    @Override
    public int getMaskWrapModeT() {
        return 0;
    }

    @Override
    public int getMaskWrapModeS() {
        return 0;
    }

    @Override
    public int getMaskFilterModeMin() {
        return 0;
    }

    @Override
    public int getMaskFilterModeMag() {
        return 0;
    }

    @Override
    public Bitmap getMaskBitmap() {
        return null;
    }

    @Override
    public void setMaskBitmap(Bitmap bitmap) {

    }

    @Override
    public float getMaskOffset(UV flag) {
        return 0;
    }

    @Override
    public float getMaskScale(UV flag) {
        return 0;
    }

    @Override
    public void setMaskOffset(UV flag, float n) {

    }

    @Override
    public void setMaskScale(UV flag, float n) {

    }

    @Override
    public float getMaskAlpha() {
        return 0;
    }

    @Override
    public void setMaskAlpha(float a) {

    }

    @Override
    public int getFilterModeMin() {
        return 0;
    }

    @Override
    public int getFilterModeMag() {
        return 0;
    }

    @Override
    public int getWrapModeT() {
        return 0;
    }

    @Override
    public int getWrapModeS() {
        return 0;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

    }

    @Override
    public float getTexOffset(UV flag) {
        return 0;
    }

    @Override
    public float getTexScale(UV flag) {
        return 0;
    }

    @Override
    public void setTexOffset(UV flag, float n) {

    }

    @Override
    public void setTexScale(UV flag, float n) {

    }

}
