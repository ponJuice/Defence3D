package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import java.lang.ref.PhantomReference;

import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.Animator;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/10/02.
 */

public class EffectObject extends GameObject {
    protected Animator anim;
    protected float timeBuffer = 0,effectTime = 1.5f;
    protected boolean start = false;
    protected int effectCount = 0;
    protected GameObject effect;
    public EffectObject(float effectTime){
        super();
        effect = new GameObject();
        this.effectTime = effectTime;
    }

    public void animationStart(){
        start = true;
        effect.getPos().zeroReset();
        effect.getPos().copy(pos);
        effect.getRot().setX(-90f);
        //effect.getScl().setX(0.1f);
        //effect.getScl().setY(0.1f);
        //effect.getScl().setZ(0.1f);
        effect.getRenderMediator().isDraw = true;
    }

    public void setAnim(Animator anim,Renderer rend){
        this.anim = new Animator(anim);
        effect.getRenderMediator().isDraw = false;
        effect.getRenderMediator().mesh = new Plane();
        effect.getRenderMediator().mode = GLES20COMPOSITIONMODE.ADD;
        effect.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = this.anim.getBitmap(0);

        rend.addItem(effect);
    }

    @Override
    public void update(){
        if(start){
            if(timeBuffer > effectTime){
                timeBuffer = 0;
                effectCount = 0;
                if(anim != null)
                    anim.setIndex(0);
                start = false;
                rm.isDraw = false;
                effect.getRenderMediator().isDraw = false;
            }
            if(timeBuffer > (effectTime/(float)anim.getAnimationLength())*(float)effectCount){
                if(anim != null) {
                    effect.getRenderMediator().mesh.getFaces()[0].matelial.tex_diffuse = anim.getBitmap();
                    anim.next();
                    effectCount++;
                }
            }
            timeBuffer += Time.getDeltaTime();
            Log.d("Effect Object","Effect!! count : "+ effectCount);
        }
    }
}
