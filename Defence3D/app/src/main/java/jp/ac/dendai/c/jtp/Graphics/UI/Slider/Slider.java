package jp.ac.dendai.c.jtp.Graphics.UI.Slider;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.Util.Figure.Rect;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

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
    protected Bitmap slider_image,line_image;

    public Slider(float cx,float cy,float line_width,float line_height,float slide_width,float slide_height,SLIDER_ORIENT s){
        this.slider_orient = s;
        this.width = Math.max(line_width,slide_width);
        this.height = Math.max(line_height,slide_height);
        this.x = cx;
        this.y = cy;
        line = new Rect(cx - line_width/2f,cy + line_height/2f,cx + line_width/2f,cy - line_height/2f);
        slider = new Rect(cx - slide_width/2f,cy + slide_height/2f,cx + slide_width/2f,cy - slide_height/2f);
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
        float _x = Constant.getActiveUiCamera().convertTouchPosToGLPosX(touch.getPosition(Touch.Pos_Flag.X));
        float _y = Constant.getActiveUiCamera().convertTouchPosToGLPosY(touch.getPosition(Touch.Pos_Flag.Y));
        if(this.touch != touch)
            return;
        if(slider.contains(_x,_y)){
            this.touch = touch;

        }else if(line.contains(_x,_y)){
        }
    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {

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
