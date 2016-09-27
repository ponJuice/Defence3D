package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Textrue.TextureInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by Goto on 2016/09/27.
 */

public class DynamicNumberText extends UI{
    protected NumberText numberText;
    protected float timeBuffer;
    protected int num,numBuffer;
    protected float effectTime;
    protected boolean animate = true;

    public DynamicNumberText(String fontName,float effectTime){
        numberText = new NumberText(fontName);
        this.effectTime = effectTime;
    }

    public void setEffectTime(float time){
        effectTime = time;
        numBuffer = numberText.getNumber();
        timeBuffer = 0;
    }

    public void setNumber(int num){
        if(num == this.num)
            return;
        this.num = num;
        numBuffer = numberText.getNumber();
        timeBuffer = 0;
    }

    public void setAnimate(boolean flag){
        animate = flag;
    }

    public void setOrientation(NumberText.ORIENTATION o){
        numberText.setOrientation(o);
    }

    public NumberText.ORIENTATION getOrientation(){
        return numberText.getOrientation();
    }

    @Override
    public void setX(float x){
        numberText.setX(x);
    }

    @Override
    public void setY(float y){
        numberText.setY(y);
    }

    @Override
    public boolean touch(Touch touch) {
        return numberText.touch(touch);
    }

    /**
     * 横の長さを設定します
     * @param width
     */
    @Override
    public void setWidth(float width){
        numberText.setWidth(width);
    }

    @Override
    public void setHeight(float height){
        numberText.setHeight(height);
    }

    @Override
    public void useAspect(boolean flag){
        useAspect = flag;
    }


    @Override
    public void draw(UiShader shader) {
        numberText.draw(shader);
    }


    @Override
    public void setHorizontal(UIAlign.Align align){
        numberText.setHorizontal(align);
    }

    @Override
    public void setVertical(UIAlign.Align align){
        numberText.setVertical(align);
    }

    @Override
    public void proc() {
        if(!animate || timeBuffer >= effectTime) {
            numberText.setNumber(num);
            return;
        }
        numberText.setNumber((int)Clamp.clamp(numBuffer,num,effectTime,timeBuffer));
        timeBuffer += Time.getDeltaTime();
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
