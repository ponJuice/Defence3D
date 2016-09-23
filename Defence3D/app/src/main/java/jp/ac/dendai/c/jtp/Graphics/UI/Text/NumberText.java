package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.widget.ImageView;

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
public class NumberText extends Image {
    public enum ORIENTATION{
        horizontal,
        vertical
    }
    protected ORIENTATION orientation = ORIENTATION.horizontal;
    protected static HashMap<String,StringBitmap[]> numberFont;
    protected TextureSetting ts = new TextureSetting();
    protected Vector2 texOffset = new Vector2(),texScale = new Vector2(1,1);
    protected Vector2 maskOffset = new Vector2(),maskScale = new Vector2(1,1);
    protected StringBitmap[] number;
    protected float offset_x = 0,offset_y = 0;
    protected float top_margin,bottom_margin,base_margin;
    //width,height は　一文字の横,縦の長さ aspectは一文字のアスペクト比
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
        //aspect = (float)number[0].bitmap.getWidth() / (float)(-number[0].fm.ascent+number[0].fm.descent);
        float fm_height = number[0].fm.bottom - number[0].fm.top;
        //float fm_height = number[0].bitmap.getHeight();
        top_margin = ( fm_height - (number[0].fm.bottom - number[0].fm.ascent))*1.5f/fm_height;
        bottom_margin = (fm_height - (- number[0].fm.top))/fm_height;
        base_margin =(fm_height - (number[0].fm.bottom))/fm_height;
        aspect = calcAspect(number[0].bitmap);
        height = 1;
        width = height * aspect;
        updatePosition();

    }

    public void setOrientation(ORIENTATION o){
        orientation = o;
        updatePosition();
    }

    public ORIENTATION getOrientation(){
        return orientation;
    }

    @Override
    public void setX(float x){
        this.x = x;
    }

    @Override
    public void setY(float y){
        this.y = y;
    }

    @Override
    public boolean touch(Touch touch) {
        return through;
    }

    @Override
    public void proc() {

    }

    /**
     * 横の長さを設定します
     * @param width
     */
    @Override
    public void setWidth(float width){
        float numDigit = (float) getNumOfDigit(num);
        if(orientation == ORIENTATION.horizontal) {
            this.width = width / numDigit;
        }else{
            this.width = width;
        }
        if(useAspect){
            //アスペクトを使用する場合はオリエンテーションによって処理が異なる
            if(orientation == ORIENTATION.horizontal){
                //横書き
                height = this.width / aspect;
            }else{
                //縦書き
                height = this.width / aspect * numDigit;
            }
        }
    }

    @Override
    public void setHeight(float height){
        float numDigit = (float) getNumOfDigit(num);
        if(orientation == ORIENTATION.horizontal) {
            this.height = height * numDigit;
        }else{
            this.height = height;
        }
        if(useAspect){
            //アスペクトを使用する場合はオリエンテーションによって処理が異なる
            if(orientation == ORIENTATION.horizontal){
                //横書き
                width = this.height * aspect;
            }else{
                //縦書き
                width = this.height * aspect * numDigit;
            }
        }
    }

    @Override
    public void useAspect(boolean flag){
        useAspect = flag;
    }


    @Override
    public void draw(UiShader shader) {
        int l = getNumOfDigit(num);
        float _x = x + offset_x;
        float _y = y + offset_y;
        for(int n = 0;n < l;n++){
            bitmap = number[getDigit(num,l-n)].bitmap;
            shader.drawUi(this,_x,_y,width,height,0,alpha);
            if(orientation == ORIENTATION.horizontal) {
                _x += width;
            }else {
                _y -= height;
            }
        }
    }

    @Override
    public void setHorizontal(UIAlign.Align align){
        horizontal = align;
        updatePosition();
    }

    @Override
    public void setVertical(UIAlign.Align align){
        vertical = align;
        updatePosition();
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
        if(num <= 0){
            return 1;
        }
        return (int) Math.log10(num) + 1;
    }

    public void setNumber(int num){
        this.num = num;
        updatePosition();
    }

    protected void updatePosition(){
        int digit = getNumOfDigit(num);
        offset_y = base_margin * height;
        offset_x = 0;
        if(orientation == ORIENTATION.horizontal) {
            if (horizontal == UIAlign.Align.LEFT) {
                offset_x = width / 2f;
            } else if (horizontal == UIAlign.Align.CENTOR) {
                offset_x = - width * (float) digit / 2f + width / 2f;
            } else {
                offset_x = - width * (float) digit + width / 2f;
            }
            if(vertical == UIAlign.Align.TOP){
                offset_y = -height /2f + top_margin * height;
            }else if(vertical == UIAlign.Align.BOTTOM){
                offset_y = height /2f - bottom_margin * height;
            }
        }
        else {
            if (vertical == UIAlign.Align.TOP) {
                offset_y = -height / 2f + top_margin*height;
            } else if (vertical == UIAlign.Align.CENTOR) {
                offset_y = height * (float) digit / 2f - height/2f;
            } else {
                offset_y = height * (float) digit - height / 2f -bottom_margin * height;
            }
            if(horizontal == UIAlign.Align.LEFT){
                offset_x = width/2f;
            }else if(horizontal == UIAlign.Align.RIGHT){
                offset_x = -width/2f;
            }
        }
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
        return bitmap;
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
