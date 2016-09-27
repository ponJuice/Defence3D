package jp.ac.dendai.c.jtp.Graphics.Effects;

import jp.ac.dendai.c.jtp.UIs.Effects.Bitmap.Animator;

/**
 * Created by Goto on 2016/09/16.
 */
public abstract class Effect {
    protected float x = 0,y = 0;
    protected float sx = 0,sy = 0;
    protected float degree = 0;
    protected Animator[] anims;
    protected int counter = 0;
    protected int[] offsets;
    public abstract void proc();
    public abstract void draw();
}
