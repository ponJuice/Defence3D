package jp.ac.dendai.c.jtp.Game.Enemy;

import android.media.SoundPool;
import android.util.Log;

import jp.ac.dendai.c.jtp.Game.GameState.GameState;
import jp.ac.dendai.c.jtp.Game.Score.ScoreManager;
import jp.ac.dendai.c.jtp.Game.Screen.TestGameScreen;
import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.Animator;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.RenderMediator;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/09/24.
 */

public class Inveder extends GameObject {
    protected enum STATE{
        NON,
        DAMAGE,
        DEAD_EFFECT,
        DEAD,
    }
    protected STATE state = STATE.NON;
    private float timeBuffer = 0;
    protected Renderer alphaRenderer;
    protected GameObject effect,scor_object;
    protected Plane p,s;
    protected int hp = 100;
    protected int score = 100;
    protected float damageTime = 1.5f,damageTimeBuffer=0;
    protected float scoreTime = 1f,scoreTimeBuffer = 0;
    protected float attack_coffi = 100;
    protected float attack_interval_time = 5;
    private GameObject[] targetList;
    private EnemyBulletList bullet;
    private Animator anim;
    protected EnemyController ec;
    public Inveder(Physics3D physics,EnemyController _ec){
        OBBCollider col = new OBBCollider(0,3.5f,0   ,9f/2f ,5f/2f ,0.5f);
        col.setUseOBB(false);
        OBBCollider col2 = new OBBCollider(0,3.5f,0  ,2.5f  ,7f/2f ,0.5f);
        col2.setUseOBB(false);

        this.ec = _ec;

        col.setNext(col2);

        setCollider(col);
        super.init();
        init();
        po.useGravity = false;
        po.tag = Constant.COLLISION_ENEMY;
        po.mask = Constant.COLLISION_PLAYERBULLET;

        scl.setX(0.1f);
        scl.setY(0.1f);
        scl.setZ(0.1f);

        //rot.setY(90f);

        this.setCollisionListener(new CollisionListener() {
            @Override
            public void collEnter(ACollider col, GameObject owner) {
                //owner.getPhysicsObject().getPhysics3D().removeObject(owner.getPhysicsObject());
                //owner.getPhysicsObject().freeze = true;
                //owner.getRenderMediator().isDraw = false;
                ScoreManager.add(score);
                hp -= ((Bullet)col.getGameObject()).getDamage();
                if(hp <= 0) {
                    state = STATE.DEAD_EFFECT;
                    effect.getPos().copy(pos);
                    effect.getPos().setY(pos.getY() + 1f);
                    effect.getRenderMediator().isDraw = true;
                    rm.isDraw = false;
                    scor_object.getPos().copy(pos);
                    scor_object.getRenderMediator().isDraw = true;
                    if(ec != null)
                        ec.startExplode(1f,1f,0,1);
                }
                else {
                    state = STATE.DAMAGE;
                    effect.getPos().copy(col.getGameObject().getPos());
                    effect.getRenderMediator().isDraw = true;
                }

            }

            @Override
            public void collExit(ACollider col, GameObject owner) {

            }

            @Override
            public void collStay(ACollider col, GameObject owner) {

            }
        });

        effect = new GameObject();
        effect.getScl().setX(3f);
        //effect.getScl().setY(3f);
        effect.getScl().setZ(3f);
        p = new Plane();
        effect.getRot().setX(-90);
        effect.getRenderMediator().mesh = p;
        effect.getRenderMediator().isDraw = false;
        effect.getRenderMediator().mode = GLES20COMPOSITIONMODE.ADD;

        s = new Plane();
        s.setImage(GLES20Util.stringToBitmap("+"+String.valueOf(score),25,255,255,255));
        scor_object = new GameObject();
        scor_object.getRenderMediator().mesh = s;
        scor_object.getRenderMediator().isDraw = false;
        scor_object.getRenderMediator().mode = GLES20COMPOSITIONMODE.ALPHA;
        float aspect = (float)s.getFaces()[0].matelial.tex_diffuse.getWidth() / (float)s.getFaces()[0].matelial.tex_diffuse.getHeight();
        scor_object.getScl().setZ(2f);
        scor_object.getScl().setX(2f*aspect);
        scor_object.getRot().setX(-90);
        scor_object.getRot().setZ(180);

        attack_interval_time = Constant.getRandom().nextFloat() * attack_coffi + 5f;

        physics.addObject(po);
    }

    public void setHp(int h){
        hp = h;
    }

    @Override
    public void init(){
        hp = 100;
        damageTime = 1.5f;
        damageTimeBuffer = 0;
        scoreTime = 1f;
        scoreTimeBuffer = 0;
        timeBuffer = 0;
        state = STATE.NON;
        if(anim != null)
            anim.index = 0;
    }

    public void setAlphaRenderer(Renderer a){
        alphaRenderer = a;
        alphaRenderer.addItem(effect);
        alphaRenderer.addItem(scor_object);
    }

    public void setAnim(Animator anim){
        this.anim = new Animator(anim);
        p.setImage(anim.getBitmap(0));
    }

    public void setBullets(EnemyBulletList bullet){
        this.bullet = bullet;
    }

    public void setTargetList(GameObject[] targetList){
        this.targetList = targetList;
    }

    @Override
    public void update(){
        if(po.freeze || GameState.getState() != GameState.GAME_STATE.PLAYING)
            return;
        if(state == STATE.NON) {
            if (timeBuffer >= attack_interval_time) {
                timeBuffer = 0;
                attack_interval_time = Constant.getRandom().nextFloat() * attack_coffi;
                GameObject b = bullet.get();
                if(b != null) {
                    float time = 2;
                    float max_y = Math.abs(getPos().getY()) + Math.abs(targetList[0].getPos().getY());
                    b.getPos().copy(this.getPos());
                    b.getPhysicsObject().velocity.setX((targetList[0].getPos().getX() - getPos().getX()) / time);
                    float g = -Constant.getPhysicsInfo().gravity.getY();
                    b.getPhysicsObject().velocity.setY(0.5f * g * time
                            + max_y / time);
                    b.getPhysicsObject().velocity.setZ((targetList[0].getPos().getZ() - pos.getZ()) / time);
                    b.getPhysicsObject().freeze = false;
                    b.getRenderMediator().isDraw = true;
                    ec.startCannon(1,1,0,1);
                    Log.d("String", "attack!");
                }
            }
            timeBuffer += Time.getDeltaTime();
        }
        else if(anim != null){
            if(damageTimeBuffer > damageTime) {
                effect.getRenderMediator().isDraw = false;
                if (state == STATE.DEAD_EFFECT) {
                    if (alphaRenderer != null) {
                        //alphaRenderer.removeItem(effect);
                        //alphaRenderer.removeItem(scor_object);
                        effect.getRenderMediator().isDraw = false;
                        scor_object.getRenderMediator().isDraw = false;
                    }
                    //po.getPhysics3D().removeObject(po);
                    po.freeze = true;
                    //rm.renderer.removeItem(this);
                    state = STATE.DEAD;
                } else if (state == STATE.DAMAGE) {
                    state = STATE.NON;
                }
            }else if(damageTimeBuffer > (damageTime/(float)anim.getBitmapCount()*(float)anim.getIndex())) {
                p.setImage(anim.getBitmap());
                anim.next();
            }
            damageTimeBuffer += Time.getDeltaTime();
        }
        if(state == STATE.DEAD_EFFECT){
            float clamp = Clamp.clamp(0,1,scoreTimeBuffer/scoreTime);
            scor_object.getRenderMediator().alpha = 1f - clamp;
            scor_object.getPos().setY(pos.getY() + 0.5f +  clamp);
            scoreTimeBuffer += Time.getDeltaTime();
        }
    }
}
