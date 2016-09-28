package jp.ac.dendai.c.jtp.Game.Weapons.Battery.BatteryListener;

import jp.ac.dendai.c.jtp.Game.Weapons.Battery.Battery;
import jp.ac.dendai.c.jtp.Game.Weapons.Turret.Turret;

/**
 * Created by Goto on 2016/09/28.
 */

public interface BatteryListener {
    public void nowReloading(Turret t,int index);
    public void finishReloading(Turret t,int index);
    public void attack(Turret t);
    public void emptyBullet(Battery b);
}
