package jp.ac.dendai.c.jtp.Graphics.UI.Text;

import android.graphics.Bitmap;

import java.util.HashMap;

import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/09.
 */
public class NumberText implements UI {
    protected static HashMap<String,StringBitmap[]> numberFont;
    protected StringBitmap[] number;
    protected int num;
    protected Texture tex;
    public float x = 0,y = 0,z = 0;
    public float lx = 1,ly = 1,lz = 1;
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
                number[n] = GLES20Util.stringToBitmap(String.valueOf(n),1,255,255,255);
            }
            numberFont.put(fontName,number);
        }
        tex = new Texture(number[0].getBitmap(), GLES20COMPOSITIONMODE.ALPHA);
    }

    public void setNumber(int num){
        this.num = num;
    }
    public int getNumber(){
        return num;
    }

    @Override
    public void touch(Touch touch) {

    }

    @Override
    public void proc() {

    }

    @Override
    public void draw(UiShader shader) {
        int line = (int)Math.log10(num) + 1;
        float x_offset = getHolizon(line,number[0].getBitmap().getHeight(),number[0].getBitmap().getWidth());
        int m = 0;
        for(int n = line;n > 0;n--){
            int digit = getDigit(num,n);
            tex.setTexture(number[digit].getBitmap());
            float bottom = number[digit].fm.bottom / (float)number[digit].bitmap.getHeight();
            float scaleX = (float)tex.getTexture().getWidth() / (float)tex.getTexture().getHeight();
            shader.draw(tex,scaleX*lx * (float)m + x,y-(bottom*ly),scaleX*lx,ly,0,1);
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
