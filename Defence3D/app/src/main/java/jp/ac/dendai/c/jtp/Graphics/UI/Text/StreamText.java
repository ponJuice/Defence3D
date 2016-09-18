package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.MaskInfo;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Vector2;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/09/15.
 */
public class StreamText extends UI {
    protected UIAlign.Align holizontal = UIAlign.Align.CENTOR,vertical = UIAlign.Align.CENTOR;
    protected String[] string;
    protected Bitmap text;
    protected Bitmap mask;
    protected int max_x_length,offset;
    protected int char_x = 0,char_y = 0;
    protected float aspect; //縦：横 = 1 : aspect
    protected float length_x = 0,length_y = 0;
    protected float x = 0,y = 0;
    public StreamText(String[] string,Bitmap text,Bitmap mask,int max_x_length,int offset){
        this.string = string;
        this.text = text;
        this.max_x_length = max_x_length;
        this.mask = mask;
        aspect = (float)text.getWidth() / (float)text.getHeight();
        length_y = 1;
        length_x = aspect;
        this.offset = offset;
    }

    public void setHeight(float n){
        length_y = n;
        length_x = n * aspect;
    }

    public void setChar_x(int n){
        char_x = n;
    }
    public void setChar_y(int n){
        char_y = n;
    }
    public int getChar_x(){
        return char_x;
    }
    public int getChar_y(){
        return  char_y;
    }
    public void nextCharX(){
        char_x++;
        if(string[char_y].length() < char_x){
            char_x = 1;
            nextCharY();
        }
    }
    public void nextCharY(){
        char_y++;
        if(string.length <= char_y){
            char_y = string.length-1;
            char_x = string[char_y].length();
        }
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
        float mojisuu = (float)max_x_length/2f - char_x;
        float pos_x = (float)text.getWidth()/(float)max_x_length * mojisuu /(float)text.getWidth();
        //GLES20Util.DrawString(x + UIAlign.convertAlign(length_x,holizontal),y + UIAlign.convertAlign(length_y,vertical)
         ///       ,length_x,length_y,0,0,1,1
          //      ,pos_x,-(float)char_y + ((float)(offset*(char_y)) / (float)text.getHeight()),string.length,0,text,mask,1, GLES20COMPOSITIONMODE.ALPHA);
    }

    public static StreamText createStreamText(String text,Bitmap mask,int size,int r,int g,int b){
        String[] line = text.split("\n");
        int height_offset = 10;
        //描画するテキスト
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(r, g, b));
        paint.setTextSize(size);
        int maxTextLength = 0;
        int textWidth = 0;
        int textHeight = 0;
        for(int n = 0;n < line.length;n++){
            //Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(), fontName);
            //paint.setTypeface(type);
            Paint.FontMetrics fm = paint.getFontMetrics();
            //テキストの表示範囲を設定
            maxTextLength = Math.max(line[n].length(),maxTextLength);
            textWidth = Math.max((int) paint.measureText(line[n]),textWidth);
            textHeight += (int) (Math.abs(fm.top) + fm.bottom);
        }
        Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight+height_offset*line.length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //canvas.drawARGB(255,0,0,0);
        for(int n = 0;n < line.length;n++) {
            //paint.getTextBounds(line[n], 0, line[n].length(), new Rect());
            //Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(), fontName);
            //paint.setTypeface(type);
            Paint.FontMetrics fm = paint.getFontMetrics();
            //キャンバスからビットマップを取得
            canvas.drawText(line[n], 0, Math.abs(fm.top)+textHeight/line.length*n + height_offset*n, paint);
        }

        return new StreamText(line,bitmap,mask,maxTextLength,height_offset);
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
}
