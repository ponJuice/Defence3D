package jp.ac.dendai.c.jtp.Game.Weapons.Battery;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Weapons.Battery.BatteryListener.BatteryListener;
import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Weapons.Turret.Turret;
import jp.ac.dendai.c.jtp.Game.Weapons.Turret.TurretTemplate;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;

/**
 * Created by Goto on 2016/09/28.
 */

public class SoSlowBattery extends Battery {
    protected int damage;
    public SoSlowBattery(Physics3D p, Renderer renderer, BulletTemplate bt) {
        super(p, renderer);
        this.damage = bt.damage;
        this.remainingBullets = bt.remainingBullets;
        this.batteryListener = new SoSlowBatteryListener();
        this.remainingBullets = 5;
        turrets = new Turret[1];
        Bullet[] b = new Bullet[1];
        for(int n = 0;n < b.length;n++){
            b[n] = new Bullet(bt.tag,bt.mask,bt.damage);
            b[n].setName("PlayerBullet");
            b[n].getPhysicsObject().freeze = true;
            b[n].getPhysicsObject().useGravity = true;
            b[n].setCollider(new OBBCollider(0,0,0,1,1,1));
            b[n].useOBB(false);
            b[n].getRenderMediator().isDraw = false;
            b[n].getRenderMediator().mesh = bt.mesh;
            b[n].getScl().setX(bt.scale_x);
            b[n].getScl().setY(bt.scale_y);
            b[n].getScl().setZ(bt.scale_z);
            physics.addObject(b[n].getPhysicsObject());
            renderer.addItem(b[n]);
        }
        TurretTemplate tt = new TurretTemplate();
        tt.bullets = b;
        tt.loadSpeed = 5;
        tt.range = 100;
        turrets[0] = new Turret(tt);
    }

    protected class SoSlowBatteryListener implements BatteryListener{

        @Override
        public void nowReloading(Turret t,int index) {
            Log.d("Battery Listener","turret now reloading : "+index);
        }

        @Override
        public void finishReloading(Turret t,int index) {
            Log.d("Battery Listener","turret reload finished : "+index);
        }

        @Override
        public void attack(Turret t) {
            Log.d("Battery Listener","turret attack");
        }

        @Override
        public void emptyBullet(Battery b) {
            Log.d("Battery Listener","empty bullets");
        }
    }
}
