package jp.ac.dendai.c.jtp.Game.Weapons.Bullet;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;

/**
 * Created by wark on 2016/09/24.
 */

public class BulletTemplate {
    public int remainingBullets;
    public int damage;
    public Mesh mesh;
    public float scale_x = 1,scale_y = 1,scale_z = 1;
    public OBBCollider col;
    public int mask,tag;
    public BulletTemplate(Mesh mesh,OBBCollider col){
        this.mesh = mesh;
        this.col = col;
    }
}
