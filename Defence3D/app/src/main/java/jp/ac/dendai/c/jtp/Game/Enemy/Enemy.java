package jp.ac.dendai.c.jtp.Game.Enemy;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;

/**
 * Created by wark on 2016/09/11.
 */
public class Enemy extends GameObject{
    protected Physics3D physics;
    protected GameObject bullet;
    protected Renderer renderer;
    protected float timeBuffer = 0;
    public Enemy(Physics3D physics,Renderer renderer,Mesh bulletMesh){
        super();
        this.renderer = renderer;
        po.velocity.setZ(0.05f);
        this.physics = physics;
        bullet = new GameObject();
        bullet.getScl().setX(0.5f);
        bullet.getScl().setY(0.5f);
        bullet.getScl().setZ(0.5f);
        //bullet.setCollider(new CircleCollider(0.25f));
        bullet.getPhysicsObject().freeze = true;
        bullet.getPhysicsObject().mask = 0;
        bullet.getPhysicsObject().tag = 0;
        bullet.getRenderMediator().isDraw = false;
        bullet.getRenderMediator().mesh = bulletMesh;

        bullet.getPhysicsObject().useGravity = true;
        renderer.addItem(bullet);

        physics.addObject(bullet.getPhysicsObject());
    }

    @Override
    public void update(){
        if(timeBuffer >= 5f){
            bullet.getPos().copy(this.getPos());

            bullet.getPhysicsObject().velocity.setZ(1.414f);
            bullet.getPhysicsObject().velocity.setY(1.414f);
            bullet.getPhysicsObject().velocity.scalarMult(11f);
            bullet.getPhysicsObject().freeze = false;

            bullet.getRenderMediator().isDraw = true;
            timeBuffer = 0;
        }else{
            timeBuffer += physics.getDeltaTime();
        }
    }
}
