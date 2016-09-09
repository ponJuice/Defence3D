package jp.ac.dendai.c.jtp.Graphics.Bitmap;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;

/**
 * Created by wark on 2016/09/09.
 */
public class Animator {
    protected AnimationBitmap ab;
    public boolean reverse = false;
    public int index = 0;
    protected Mesh mesh;

    public Animator(AnimationBitmap ab,Mesh target){
        this.ab = ab;
        mesh = target;
    }

    public void next(){
        mesh.getFaces()[0].matelial.tex_diffuse = getBitmapNext();
    }

    public Bitmap getBitmapNext(){
        Bitmap temp = ab.animation[index];
        if(reverse)
            index--;
        else
            index++;
        if(index < 0)
            index = ab.animation.length + index;
        else if(index >= ab.animation.length)
            index = 0;
        return temp;
    }

    public Bitmap getBitmap(int num){
        if(num < 0)
            return ab.animation[0];
        if(num >= ab.animation.length)
            return ab.animation[ab.animation.length-1];
        return ab.animation[num];
    }


}
