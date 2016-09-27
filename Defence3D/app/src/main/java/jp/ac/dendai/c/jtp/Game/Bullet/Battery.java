package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;

/**
 * Created by テツヤ on 2016/09/25.
 */

public class Battery {
    protected int ite = 0;
    protected Turret[] turrets;
    protected Physics3D physics;

    public Battery(Physics3D p, Renderer renderer){
        this.physics = p;
    }

    public void setRange(float range){
        for(int n = 0;n < turrets.length;n++){
            turrets[n].setRange(range);
        }
    }

    public void attack(GameObject object,float x,float y,float z){
        for(int n =0;n < turrets.length;n++){
            if(turrets[n].attack(object,x,y,z)){
                return;
            }
        }
    }

    public boolean isLoad(){
        for(int n = 0;n < turrets.length;n++) {
            if(turrets[n].isLoad){
                return true;
            }
        }
        return false;
    }
}
