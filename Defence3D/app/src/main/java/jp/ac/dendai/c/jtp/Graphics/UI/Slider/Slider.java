package jp.ac.dendai.c.jtp.Graphics.UI.Slider;

import android.graphics.Bitmap;
import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Graphics.UI.Util.Figure.Rect;
import jp.ac.dendai.c.jtp.Math.Clamp;
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
    protected SLIDER_ORIENT slider_orient = SLIDER_ORIENT.portrait;
    protected Rect slider,line;
    protected Image slider_image,line_image;
    protected boolean hover = false;
    protected SliderChangeValueListener scvl;

    public Slider(float cx,float cy,float line_width,float line_height,float slide_width,float slide_height,SLIDER_ORIENT s){

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

        setSliderOrientation(s);
    }

    @Override
    public void setX(float x){
        super.setX(x);
        float offset = x + UIAlign.convertAlign(width,horizontal);
        line.setCx(offset);
        line_image.setX(offset);
        slider.setCx(offset);
        slider_image.setX(offset);
    }
    @Override
    public void setHorizontal(UIAlign.Align align){
        horizontal = align;
        float offset = x + UIAlign.convertAlign(width,horizontal);
        line.setCx(offset);
        line_image.setX(offset);
        slider.setCx(offset);
        slider_image.setX(offset);
    }
    @Override
    public void setVertical(UIAlign.Align align){
        vertical = align;
        float offset = y + UIAlign.convertAlign(height,vertical);
        line.setCy(offset);
        line_image.setY(offset);
        slider.setCy(offset);
        slider_image.setY(offset);
    }

    @Override
    public void setY(float y){
        super.setY(y);
        float offset = y + UIAlign.convertAlign(height,vertical);
        line.setCy(offset);
        line_image.setY(offset);
        slider.setCy(offset);
        slider_image.setY(offset);
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

    public float getValue() {
        return innerValue * max + min;
    }

    public void setValue(float nowValue) {
        if(nowValue > max)
            nowValue = max;
        else if(nowValue < min)
            nowValue = min;
        setInnerValue(nowValue / (max - min));
        updateSliderPos();
    }

    public void setSliderOrientation(SLIDER_ORIENT orient){
        if(this.slider_orient != orient){
            float temp = width;
            width = height;
            height = temp;

            temp = slider_image.getWidth();
            slider_image.setWidth(slider_image.getHeight());
            slider_image.setHeight(temp);

            temp = slider.getWidth();
            slider.setWidth(slider.getHeight());
            slider.setHeight(temp);

            temp = line.getWidth();
            line.setWidth(line.getHeight());
            line.setHeight(temp);

            temp = line_image.getWidth();
            line_image.setWidth(line_image.getHeight());
            line_image.setHeight(temp);
            }
        this.slider_orient= orient;
    }

    protected void updateSliderPos(){

        if(slider_orient == SLIDER_ORIENT.landscape) {
            slider.setCx(line.getLeft() + line.getWidth() * innerValue);
            slider_image.setX(slider.getCx());
            slider.setCy(line.getCy());
        }else{
            slider.setCy(line.getBottom() + line.getHeight() * innerValue);
            slider_image.setY(slider.getCy());
            slider.setCx(line.getCx());
        }
    }
    Touch touch;
    @Override
    public boolean touch(Touch touch) {
        /*if(this.touch == null && touch.getTouchID() == -1)
            return;
        if(this.touch == null)
            this.touch = touch;
        else if(this.touch != touch || this.touch.getTouchID() == -1)
            return;*/

        float _x,_y;
        if(this.touch != null && this.touch.getTouchID() != -1){
            _x = Constant.getActiveUiCamera().convertTouchPosToGLPosX(this.touch.getPosition(Touch.Pos_Flag.X));
            _y = Constant.getActiveUiCamera().convertTouchPosToGLPosY(this.touch.getPosition(Touch.Pos_Flag.Y));
        }
        else{
            if(this.touch != null && this.touch.getTouchID() == -1)
                this.touch = null;
            if(touch.getTouchID() == -1) {
                hover = false;
                return true;
            }
            _x = Constant.getActiveUiCamera().convertTouchPosToGLPosX(touch.getPosition(Touch.Pos_Flag.X));
            _y = Constant.getActiveUiCamera().convertTouchPosToGLPosY(touch.getPosition(Touch.Pos_Flag.Y));
            if (slider.contains(_x, _y) || line.contains(_x, _y)) {
                this.touch = touch;
            }else{
                hover = false;
                return true;
            }
        }

        if(this.touch.getAction() != Touch.ACTION.DOWN && this.touch.getDelta(Touch.Pos_Flag.X) == 0 && this.touch.getDelta(Touch.Pos_Flag.Y) == 0) {
            //hover = false;
            return true;
        }

        //Log.d("Slider bool","bool = "+(slider.contains(_x,_y) || this.touch == touch));

        Log.d("Action","id:"+this.touch.getTouchID() + " action:"+touch.getAction());
        if(slider.contains(_x,_y) || (this.touch.getAction() == Touch.ACTION.MOVE && hover)){
            if(slider_orient == SLIDER_ORIENT.landscape) {
                setValueAtPos(_x);
            }else{
                setValueAtPos(_y);
            }
            hover = true;
            return through;

        }else if(this.touch.getAction() == Touch.ACTION.DOWN && line.contains(_x,_y)){
            if(slider_orient == SLIDER_ORIENT.landscape) {
                setValueAtPos(_x);
            }else{
                setValueAtPos(_y);
            }
            return through;
        }
        return true;
    }

    protected void setValueAtPos(float value){
        if(slider_orient == SLIDER_ORIENT.landscape){
            value = Clamp.clamp(line.getLeft(),line.getRight(),value);
            slider_image.setX(value);
            slider.setCx(value);
            setInnerValue((value - line.getLeft())/line.getWidth());
        }else{
            value = Clamp.clamp(line.getBottom(),line.getTop(),value);
            slider_image.setY(value);
            slider.setCy(value);
            setInnerValue((value - line.getBottom())/line.getHeight());
        }
    }

    public void setChangeListener(SliderChangeValueListener listener){
        scvl = listener;
    }

    protected void setInnerValue(float value){
        innerValue = value;
        if(scvl != null)
            scvl.changeValue(getValue());
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
