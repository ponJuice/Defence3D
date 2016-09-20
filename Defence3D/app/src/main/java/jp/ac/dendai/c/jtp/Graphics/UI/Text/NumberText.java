package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import java.util.HashMap;


import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureSetting;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Vector2;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/09.
 */
public class NumberText extends UI {
    protected static HashMap<String,StringBitmap[]> numberFont;
    protected TextureSetting ts = new TextureSetting();
    protected Vector2 texOffset = new Vector2(),texScale = new Vector2(1,1);
    protected Vector2 maskOffset = new Vector2(),maskScale = new Vector2(1,1);
    protected StringBitmap[] number;
    protected Bitmap mask;
    protected float one_width,one_height,one_aspect;
    protected int num;
    protected float mask_alpha = 1f;
    protected UIAlign.Align align_h = UIAlign.Align.CENTOR,align_v = UIAlign.Align.CENTOR;
    public NumberText(String fontName){
        if(numberFont == null)
            numberFont = new HashMap<>();
        if(numberFont.containsKey(fontName)){
            //フォントが既に登録されている
            number = numberFont.get(fontName);
        }else{
            //まだ登録されていない
            number = new StringBitmap[10];
            for(int n = 0;n < 10;n++){
                number[n] = GLES20Util.stringToStringBitmap(String.valueOf(n),fontName,50,255,255,255);
            }
            numberFont.put(fontName,number);
        }
        one_width = number[0].bitmap.getWidth();
        one_height = number[0].bitmap.getHeight();
        one_aspect = calcAspect(number[0].bitmap);
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void setWidth(float width){
        this.width = width;
        if(useAspect){
            height = width / (float)getNumOfDigit(num) /one_aspect;
            aspect = width / height;
        }
    }

    @Override
    public void setHeight(float height){
        this.height = height;
        if(useAspect){
            width = height * one_aspect;
            width *= (float)getNumOfDigit(num);
        }
    }

    @Override
    public void useAspect(boolean flag){
        useAspect = flag;
        if(useAspect)
            aspect = height / (height * one_aspect * (float)getNumOfDigit(num));
    }


    @Override
    public void draw(UiShader shader) {
        int l = getNumOfDigit(num);
        for(int n = 0;n < l;n++){

        }
    }




    protected int pow(int num, int count) {
        if (count < 0) {
            return 0;
        }
        else if (count < 1) {
            return 1;
        }
        int temp = num;
        for (; count > 1; count--) {
            temp *= num;
        }
        return temp;
    }

    protected  int getDigit(int num, int digit){
        if (digit <= 0)
            return 0;
        int c = pow(10, digit - 1);
        int d = pow(10, digit);
        int a = num / c;
        int b = num / d;

        return a - (b * 10);
    }

    protected int getNumOfDigit(int num){
        return (int) Math.log10(num);
    }

    public void setNumber(int num){
        this.num = num;
    }

    public int getNumber(){
        return num;
    }

    @Override
    public int getMaskWrapModeT() {
        return ts.getMask_warp_t();
    }

    @Override
    public int getMaskWrapModeS() {
        return ts.getMask_warp_s();
    }

    @Override
    public int getMaskFilterModeMin() {
        return ts.getMask_filter_min();
    }

    @Override
    public int getMaskFilterModeMag() {
        return ts.getMask_filter_mag();
    }

    @Override
    public Bitmap getMaskBitmap() {
        return mask;
    }

    @Override
    public void setMaskBitmap(Bitmap bitmap) {
        mask = bitmap;
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
        return ts.getFilter_min();
    }

    @Override
    public int getFilterModeMag() {
        return ts.getFilter_mag();
    }

    @Override
    public int getWrapModeT() {
        return ts.getWrap_t();
    }

    @Override
    public int getWrapModeS() {
        return ts.getWrap_s();
    }

    @Override
    public Bitmap getBitmap() {
        return number[0].bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

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
    public void setTexOffset(UV flag, float n) {

    }

    @Override
    public void setTexScale(UV flag, float n) {

    }
}
