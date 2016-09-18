package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
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
public class StreamText extends Image {
    protected UIAlign.Align holizontal = UIAlign.Align.CENTOR,vertical = UIAlign.Align.CENTOR;
    protected String[] string;
    protected int max_x_length,offset;
    protected int char_x = 0,char_y = 0;
    public StreamText(String[] string,Bitmap text,Bitmap mask,int max_x_length,int offset){
        super(text);
        this.string = string;
        this.image = text;
        this.max_x_length = max_x_length;
        this.mask = mask;
        aspect = (float)text.getWidth() / (float)text.getHeight();
        height = 1;
        width = aspect;
        this.offset = offset;

        mask_filter_mag = GLES20.GL_NEAREST;
        mask_filter_min = GLES20.GL_NEAREST;
        mask_warp_s = GLES20.GL_CLAMP_TO_EDGE;
        mask_warp_t = GLES20.GL_CLAMP_TO_EDGE;
        filter_mag = GLES20.GL_LINEAR;
        filter_min = GLES20.GL_LINEAR;
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
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        float mojisuu = (float)max_x_length/2f - char_x;
        float pos_x = (float)image.getWidth()/(float)max_x_length * mojisuu /(float)image.getWidth();
        getTexOffset().setX(0);
        getTexOffset().setY(0);
        getTexScale().setX(1);
        getTexScale().setY(1);
        mask_offset_u = pos_x;
        mask_offset_v = -(float)char_y + ((float)(offset*(char_y)) / (float)image.getHeight());
        mask_scale_u = 1;//string.length;
        mask_scale_v = string.length;

        shader.drawUi(this
                ,x + UIAlign.convertAlign(width,holizontal)
                ,y + UIAlign.convertAlign(height,vertical)
                ,width,height
                ,0,alpha);
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
        //canvas.drawARGB(255,126,126,126);
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
