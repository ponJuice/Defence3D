package jp.ac.dendai.c.jtp.Game.Enemy;

import java.lang.annotation.Target;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.Time;

/**
 * Created by wark on 2016/09/24.
 */

public class Inveder extends GameObject {
    public Physics3D physics;
    public GameObject bullet;
    public float timeBuffer;
    public GameObject target;
    public BulletTemplate bt;
    public Inveder(Physics3D physics, Renderer renderer,BulletTemplate bt,GameObject target){
        this.physics = physics;
        this.target = target;
        this.bt = bt;
        bullet = new GameObject();
        bullet.getRenderMediator().mesh = bt.getMesh();
        bullet.getRenderMediator().isDraw = false;
        bullet.setPhysicsObject(new PhysicsObject(bullet));
        bullet.getPhysicsObject().useGravity = true;
        bullet.getPhysicsObject().freeze = true;
        bullet.setCollider(bt.getCollider());
        //physics.addObject(bullet.getPhysicsObject());
        //renderer.addItem(bullet);
    }

    @Override
    public void update(){
        if(timeBuffer >= 30){
            timeBuffer = 0;
            bullet.getPos().copy(pos);
            bullet.getPhysicsObject().velocity.copy(target.getPos());
            bullet.getPhysicsObject().velocity.sub(pos);
            bullet.getPhysicsObject().velocity.normalize();
            bullet.getPhysicsObject().velocity.scalarMult(bt.getSpeed());
            bullet.getPhysicsObject().freeze = false;
            bullet.getRenderMediator().isDraw = true;
        }
        timeBuffer += Time.getDeltaTime();
    }
}
