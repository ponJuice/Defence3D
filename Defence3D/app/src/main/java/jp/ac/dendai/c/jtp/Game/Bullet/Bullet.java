package jp.ac.dendai.c.jtp.Game.Bullet;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;

/**
 * Created by テツヤ on 2016/09/25.
 */

public class Bullet extends GameObject{
    protected boolean settled = false;
    protected float range_coefficient = 1;
    public Bullet(int tag,int mask){
        super();
        po.tag = tag;
        po.mask = mask;
        cl = new CollisionListener() {
            @Override
            public void collEnter(ACollider col, GameObject owner) {
                settled = false;
                po.freeze = true;
                rm.isDraw = false;
                Log.d("Bullet","Bullet Collision");
            }

            @Override
            public void collExit(ACollider col, GameObject owner) {

            }

            @Override
            public void collStay(ACollider col, GameObject owner) {

            }
        };
    }

    public boolean isSettled(){
        return settled;
    }

    //x,y,z : 発射する方向 range : 射程
    public void attack(GameObject owner,float range,float x,float y,float z){
        pos.copy(owner.getPos());
        rot.copy(owner.getRot());
        po.reset();
        po.velocity.setX(x);
        po.velocity.setY(y);
        po.velocity.setZ(z);
        po.velocity.normalize();
        po.velocity.scalarMult(range * range_coefficient);
        po.freeze = false;
        rm.isDraw = true;
        settled = true;
    }
}
