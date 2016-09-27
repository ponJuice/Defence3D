package jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap;

import android.graphics.Bitmap;

/**
 * Created by wark on 2016/09/09.
 */
public class Animator {
    protected AnimationBitmap ab;
    public boolean reverse = false;
    public int index = 0;

    public Animator(AnimationBitmap ab){
        this.ab = ab;
    }

    public void next(){
        index++;
        clampIndex();
    }
    public void prev(){
        index--;
        clampIndex();
    }

    public int getIndex(){
        return index;
    }

    public int getBitmapCount(){
        return ab.animation.length;
    }

    private void clampIndex(){
        if(index < 0)
            index = ab.animation.length + index;
        else if(index >= ab.animation.length)
            index = 0;
    }

    public Bitmap getBitmap(int num){
        if(num < 0)
            return ab.animation[0];
        if(num >= ab.animation.length)
            return ab.animation[ab.animation.length-1];
        return ab.animation[num];
    }

    public Bitmap getBitmap(){
        return ab.animation[index];
    }
}
