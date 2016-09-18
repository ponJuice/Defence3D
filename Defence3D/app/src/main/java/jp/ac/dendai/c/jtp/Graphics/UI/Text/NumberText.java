package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import java.util.HashMap;


import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StringBitmap;
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
    protected StringBitmap[] number;
    protected int num;
    public float x = 0,y = 0,z = 0;
    public float lx = 1,ly = 1,lz = 1;
    public float textSize = 1f;
    protected int align_h = UI_CENTOR,align_v = UI_CENTOR;
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
    }

    public void setNumber(int num){
        this.num = num;
    }
    public int getNumber(){
        return num;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public float getAlpha() {
        return 0;
    }

    @Override
    public void setAlpha(float a) {

    }

    @Override
    public Vector2 getTexOffset() {
        return null;
    }

    @Override
    public Vector2 getTexScale() {
        return null;
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
    public float getMaskOffset(MASK flag) {
        return 0;
    }

    @Override
    public float getMaskScale(MASK flag) {
        return 0;
    }

    @Override
    public void setMaskOffset(MASK flag, float n) {

    }

    @Override
    public void setMaskScale(MASK flag, float n) {

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
    public GLES20COMPOSITIONMODE getBlendMode() {
        return null;
    }

    @Override
    public float getColor(COLOR col) {
        return 0;
    }

    @Override
    public int getVBO() {
        return 0;
    }

    @Override
    public int getIBO() {
        return 0;
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shadeer) {
        float _lx = lx * textSize,_ly = ly * textSize;
        int line = (int)Math.log10(num) + 1;
        float x_offset = getHolizon(line,number[0].getBitmap().getHeight(),number[0].getBitmap().getWidth());
        int m = 0;
        for(int n = line;n > 0;n--){
            int digit = getDigit(num,n);
            float bottom = number[digit].fm.bottom / (float)number[digit].bitmap.getHeight();
            float scaleX = (float)number[digit].getBitmap().getWidth() / (float)number[digit].getBitmap().getHeight();
            GLES20Util.DrawGraph(scaleX*_lx * (float)m + x,y-(bottom*_ly),scaleX*_lx,_ly,number[digit].getBitmap(),1,GLES20COMPOSITIONMODE.ALPHA);
            m++;
        }
    }

    public float getHolizon(int line,int h,int w){
        if(align_h == UI_CENTOR)
            return (float)(w*line)/2f/(float)h;
        else if(align_h == UI.UI_LEFT)
            return (float)w/2f;
        else if(align_h == UI_RIGHT)
            return -(float)(w*line)+(float)w/2f;
        return 0;
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
}
