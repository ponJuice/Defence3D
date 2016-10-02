package jp.ac.dendai.c.jtp.Game.Enemy;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.EffectObject;
import jp.ac.dendai.c.jtp.Game.FrameListener;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.AnimationBitmap;
import jp.ac.dendai.c.jtp.Graphics.Effects.Bitmap.Animator;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.defence3d.R;


/**
 * Created by wark on 2016/10/01.
 */

public class EnemyBulletList {
    protected EffectObject[] list;
    public EnemyBulletList(int length, Physics3D physics, Renderer objectRender,Renderer effectRender,Mesh mesh){
        Animator anim = new Animator(AnimationBitmap.createAnimation(R.mipmap.exp_alpha,256,64,8,2));
        list = new EffectObject[length];
        EnemyBulletFrameListener ebfl = new EnemyBulletFrameListener();
        for(int n = 0;n < list.length;n++){
            list[n] = new EffectObject(0.5f);
            list[n].getRenderMediator().isDraw = false;
            list[n].getRenderMediator().mesh = mesh;
            list[n].getPhysicsObject().mask = Constant.COLLISION_PLAYDER | Constant.COLLISION_PLAYERBULLET;
            list[n].getPhysicsObject().tag = Constant.COLLISION_ENEMYBULLET;
            list[n].getPhysicsObject().useGravity = true;
            list[n].getPhysicsObject().freeze = true;
            list[n].getPhysicsObject().useGravity = true;
            list[n].setCollisionListener(new CollisionListener() {
                @Override
                public void collEnter(ACollider col, GameObject owner) {
                    owner.getRenderMediator().isDraw = false;
                    owner.getPhysicsObject().freeze = true;
                    ((EffectObject)owner).animationStart();
                    Log.d("Enemy Bullet","Enemy Bullet Collision!! "+col.getGameObject().getClass());
                }

                @Override
                public void collExit(ACollider col, GameObject owner) {

                }

                @Override
                public void collStay(ACollider col, GameObject owner) {

                }
            });
            list[n].setCollider(new OBBCollider(0,0,0,1,1,1));
            list[n].setDebugDraw(false);
            list[n].useOBB(false);
            list[n].getScl().setX(0.2f);
            list[n].getScl().setY(0.2f);
            list[n].getScl().setZ(0.2f);
            list[n].setName("Enemy Bullet");
            list[n].setFrameListener(ebfl);
            list[n].setAnim(anim,effectRender);
            physics.addObject(list[n].getPhysicsObject());
            objectRender.addItem(list[n]);
        }
    }
    public GameObject get(){
        for(int n =0;n < list.length;n++){
            if(list[n].getPhysicsObject().freeze){
                return list[n];
            }
        }
        return null;
    }
    public GameObject[] getList(){
        return list;
    }
}
