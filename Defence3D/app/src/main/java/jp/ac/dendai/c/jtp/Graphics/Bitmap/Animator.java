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

    public Animator(Animator animator){
        reverse = animator.reverse;
        ab = animator.ab;
        this.mesh = animator.mesh;
    }

    public void setMesh(Mesh mesh){
        this.mesh = mesh;
        this.mesh.getFaces()[0].matelial.tex_diffuse = getBitmap(0);
    }

    public void setIndex(int index){
        index = Math.abs(index) % ab.animation.length;
        this.index = index;
        mesh.getFaces()[0].matelial.tex_diffuse = ab.animation[index];
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

    public int getAnimationLength(){
        return ab.animation.length;
    }

    public Bitmap getBitmap(int num){
        if(num < 0)
            return ab.animation[0];
        if(num >= ab.animation.length)
            return ab.animation[ab.animation.length-1];
        return ab.animation[num];
    }


}
