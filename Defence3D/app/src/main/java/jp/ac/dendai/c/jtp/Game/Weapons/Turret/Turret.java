package jp.ac.dendai.c.jtp.Game.Weapons.Turret;

import jp.ac.dendai.c.jtp.Game.Weapons.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;
import jp.ac.dendai.c.jtp.Time.Time;

/**
 * Created by テツヤ on 2016/09/25.
 */

public class Turret {
    protected int tag;
    protected Bullet[] bullets;
    protected int ite = 0;
    protected float loadSpeed,timeBuffer;
    protected float range;
    protected boolean isLoad = false;

    public Turret(Physics3D p,Bullet[] bullets,float loadSpeed,float range){
        this.bullets = bullets;
        /*for(int n = 0;n < bullets.length;n++){
            p.addObject(bullets[n].getPhysicsObject());
        }*/
        this.loadSpeed = loadSpeed;
        this.range = range;
    }

    public Turret(TurretTemplate tt){
        bullets = tt.bullets;
        loadSpeed = tt.loadSpeed;
        range = tt.range;
    }

    public int getTag(){
        return tag;
    }

    public void setTag(int t){
        tag = t;
    }

    public void setRange(float range){
        this.range = range;
    }

    //owner : 弾を発射する奴 x,y,z : 発射する方向
    public boolean attack(GameObject owner,float x,float y,float z){
        if(isLoad){
            return false;
        }
        bullets[ite].attack(owner,range,x,y,z);
        ite++;
        if(ite >= bullets.length){
            ite = ite %  bullets.length;
        }
        isLoad = true;
        return true;
    }

    public void proc(){
        if(!isLoad)
            return;
        timeBuffer += Time.getDeltaTime();
        if(timeBuffer >= loadSpeed){
            timeBuffer = 0;
            isLoad = false;
        }
    }

    public boolean isLoad(){
        return isLoad;
    }
}
