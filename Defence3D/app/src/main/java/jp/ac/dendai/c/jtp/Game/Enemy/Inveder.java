package jp.ac.dendai.c.jtp.Game.Enemy;

import android.util.Log;

import java.lang.annotation.Target;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
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
        po.mask = Constant.COLLISION_ENEMY;
        /*cl = new CollisionListener() {
            @Override
            public void collEnter(ACollider col, GameObject owner) {
                po.freeze = true;
                rm.isDraw = false;
            }

            @Override
            public void collExit(ACollider col, GameObject owner) {

            }

            @Override
            public void collStay(ACollider col, GameObject owner) {

            }
        };*/

        physics.addObject(po);
    }
    @Override
    public void update(){
        if(timeBuffer >= 30){
            timeBuffer = 0;
            Log.d("String", "attack!");
        }
        timeBuffer += Time.getDeltaTime();
    }
}
