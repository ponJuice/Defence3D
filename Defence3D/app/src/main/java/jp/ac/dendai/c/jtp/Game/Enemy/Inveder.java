package jp.ac.dendai.c.jtp.Game.Enemy;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.ScoreManager;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.Animator;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Time.Time;
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
    protected GameObject effect;
    protected Plane p;
    protected int hp = 100;
    protected int score = 100;
    protected float damageTime=0,damageTimeBuffer=0;
    private GameObject[] targetList;
    private GameObject bullet;
    private Animator anim;
    public Inveder(Physics3D physics){
        OBBCollider col = new OBBCollider(0,3.5f,0   ,9f/2f ,5f/2f ,0.5f);
        col.setUseOBB(false);
        OBBCollider col2 = new OBBCollider(0,3.5f,0  ,2.5f  ,7f/2f ,0.5f);
        col2.setUseOBB(false);

        damageTime = 1;

        col.setNext(col2);

        setCollider(col);
        setPhysicsObject(new PhysicsObject(this));
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
        effect.getScl().setY(3f);
        effect.getScl().setZ(3f);
        p = new Plane();
        effect.getRot().setX(-90);
        effect.getRenderMediator().mesh = p;
        effect.getRenderMediator().isDraw = false;
        effect.getRenderMediator().mode = GLES20COMPOSITIONMODE.ADD;

        physics.addObject(po);
    }

    public void setAlphaRenderer(Renderer a){
        alphaRenderer = a;
        alphaRenderer.addItem(effect);
    }

    public void setAnim(Animator anim){
        this.anim = new Animator(anim);
        p.setImage(anim.getBitmap(0));
    }

    public void setBullets(GameObject bullet){
        this.bullet = bullet;
    }

    public void setTargetList(GameObject[] targetList){
        this.targetList = targetList;
    }

    @Override
    public void update(){
        if(po.freeze)
            return;
        if(state == STATE.NON) {
            if (timeBuffer >= 5) {
                timeBuffer = 0;
                if (Constant.getRandom().nextInt(100) % 80 == 0) {
                    float time = 2;
                    float max_y = Math.abs(getPos().getY()) + Math.abs(targetList[0].getPos().getY());
                    bullet.getPos().copy(this.getPos());
                    bullet.getPhysicsObject().velocity.setX((targetList[0].getPos().getX() - getPos().getX()) / time);
                    float g = -Constant.getPhysicsInfo().gravity.getY();
                    bullet.getPhysicsObject().velocity.setY(0.5f * g * time
                            + max_y / time);
                    bullet.getPhysicsObject().velocity.setZ((targetList[0].getPos().getZ() - pos.getZ()) / time);
                    bullet.getPhysicsObject().freeze = false;
                    bullet.getRenderMediator().isDraw = true;
                    Log.d("String", "attack!");
                }
            }
            timeBuffer += Time.getDeltaTime();
        }
        else if(anim != null){
            if(damageTimeBuffer > damageTime) {
                effect.getRenderMediator().isDraw = false;
                if (state == STATE.DEAD_EFFECT) {
                    if (alphaRenderer != null)
                        alphaRenderer.removeItem(effect);
                    po.getPhysics3D().removeObject(po);
                    rm.renderer.removeItem(this);
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
    }
}
