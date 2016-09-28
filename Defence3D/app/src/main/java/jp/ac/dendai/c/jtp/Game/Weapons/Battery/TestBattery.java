package jp.ac.dendai.c.jtp.Game.Weapons.Battery;

import jp.ac.dendai.c.jtp.Game.Weapons.Battery.Battery;
import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Weapons.Turret.Turret;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;

/**
 * Created by テツヤ on 2016/09/25.
 */

public class TestBattery extends Battery {
    public TestBattery(int damage,Physics3D p, Renderer renderer, Mesh model, int tag, int mask) {
        super(p,renderer);
        turrets = new Turret[1];
        Bullet[] b = new Bullet[3];
        for(int n = 0;n < b.length;n++){
            b[n] = new Bullet(tag,mask,damage);
            b[n].setName("PlayerBullet");
            b[n].getPhysicsObject().freeze = true;
            b[n].getPhysicsObject().useGravity = true;
            b[n].setCollider(new OBBCollider(0,0,0,1,1,1));
            b[n].useOBB(false);
            b[n].getRenderMediator().isDraw = false;
            b[n].getRenderMediator().mesh = model;
            physics.addObject(b[n].getPhysicsObject());
            renderer.addItem(b[n]);
        }
        turrets[0] = new Turret(physics,b,2,30);
    }
}
