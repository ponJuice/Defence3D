package jp.ac.dendai.c.jtp.Game.Weapons.Battery;

import jp.ac.dendai.c.jtp.Game.Weapons.Battery.BatteryListener.BatteryListener;
import jp.ac.dendai.c.jtp.Game.Weapons.Turret.Turret;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics3D;

/**
 * Created by テツヤ on 2016/09/25.
 */

public class Battery {
    protected BatteryListener batteryListener;
    protected int remainingBullets = 0;
    protected int ite = 0;
    protected Turret[] turrets;
    protected Physics3D physics;

    public Battery(Physics3D p, Renderer renderer){
        this.physics = p;
    }

    public void proc(){
        boolean flag;
        for(int n = 0;n < turrets.length;n++){
            if((flag = turrets[n].isLoad()))
                batteryListener.nowReloading(turrets[n],n);
            turrets[n].proc();
            if(!turrets[n].isLoad() && flag)
                batteryListener.finishReloading(turrets[n],n);

        }
    }

    public void setRange(float range){
        for(int n = 0;n < turrets.length;n++){
            turrets[n].setRange(range);
        }
    }

    public void attack(GameObject object,float x,float y,float z){
        if(remainingBullets <= 0) {
            batteryListener.emptyBullet(this);
            return;
        }
        for(int n =0;n < turrets.length;n++){
            if(turrets[n].attack(object,x,y,z)){
                batteryListener.attack(turrets[n]);
                return;
            }
        }
    }

    public boolean isLoad(){
        for(int n = 0;n < turrets.length;n++) {
            if(turrets[n].isLoad()){
                return true;
            }
        }
        return false;
    }
}
