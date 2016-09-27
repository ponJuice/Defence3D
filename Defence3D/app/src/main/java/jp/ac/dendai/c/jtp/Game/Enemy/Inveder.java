package jp.ac.dendai.c.jtp.Game.Enemy;

import android.util.Log;

import java.lang.annotation.Target;
import java.sql.ParameterMetaData;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by wark on 2016/09/24.
 */

public class Inveder extends GameObject {
    private float timeBuffer = 0;
    private GameObject[] targetList;
    private GameObject bullet;
    public Inveder(Physics3D physics){
        OBBCollider col = new OBBCollider(0,0.49273f,0,1.6282f/2f,0.7141f/2f,0.13568f/2f);
        col.setUseOBB(false);
        OBBCollider col2 = new OBBCollider(0,0.474895f,0,1.08548f/2f,0.94979f/2f,0.13568f/2f);
        col2.setUseOBB(false);

        col.setNext(col2);

        setCollider(col);
        setPhysicsObject(new PhysicsObject(this));
        po.useGravity = false;
        po.tag = Constant.COLLISION_ENEMY;
        po.mask = Constant.COLLISION_PLAYERBULLET;

        this.setCollisionListener(new CollisionListener() {
            @Override
            public void collEnter(ACollider col, GameObject owner) {
                owner.getPhysicsObject().getPhysics3D().removeObject(owner.getPhysicsObject());
                owner.getPhysicsObject().freeze = true;
                owner.getRenderMediator().isDraw = false;
            }

            @Override
            public void collExit(ACollider col, GameObject owner) {

            }

            @Override
            public void collStay(ACollider col, GameObject owner) {

            }
        });

        physics.addObject(po);
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
        if(timeBuffer >= 5){
            timeBuffer = 0;
            if(Constant.getRandom().nextInt(100) % 80 == 0){
                float time = 2;
                float max_y = Math.abs(getPos().getY()) + Math.abs(targetList[0].getPos().getY());
                bullet.getPos().copy(this.getPos());
                bullet.getPhysicsObject().velocity.setX((targetList[0].getPos().getX() - getPos().getX())/time);
                float g = -Constant.getPhysicsInfo().gravity.getY();
                bullet.getPhysicsObject().velocity.setY(0.5f*g*time
                        + max_y/time);
                bullet.getPhysicsObject().velocity.setZ((targetList[0].getPos().getZ() - pos.getZ())/time);
                bullet.getPhysicsObject().freeze = false;
                bullet.getRenderMediator().isDraw = true;
                Log.d("String", "attack!");
            }
        }
        timeBuffer += Time.getDeltaTime();
    }
}
