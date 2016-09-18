package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import java.util.HashMap;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.CharactorsMap;
import jp.ac.dendai.c.jtp.Math.Vector2;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/09/14.
 */
public class DynamicText extends UI {
    protected Character[] text;
    protected float x = 0,y = 0;
    protected int m = 0,n = 0;
    protected float aspect = 0;
    protected float textSize = 1;
    public void setText(Character[] c){
        text = c;
    }
    public void setTextSize(float n){
        textSize = n;
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
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
    public void draw(UiShader shader) {
        n = 0;
        m = 0;
        aspect = 0;
        for(int index = 0;index < text.length;index++) {
            if(text[index] == '\n') {
                m++;
                n = 0;
                continue;
            }
            Bitmap chara = CharactorsMap.getChara(text[n]);
            aspect = (float)chara.getWidth() / (float)chara.getHeight();
            GLES20Util.DrawGraph(x + n * aspect * textSize, y - m * textSize, aspect*textSize, textSize, chara, 1, GLES20COMPOSITIONMODE.ALPHA);
            n++;
        }
    }
}
