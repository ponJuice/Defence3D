package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;

/**
 * Created by wark on 2016/09/24.
 */

public class BulletTemplate {
    protected Mesh mesh;
    protected float speed;
    protected OBBCollider col;
    public BulletTemplate(Mesh mesh,OBBCollider col,float speed){
        this.mesh = mesh;
        this.speed = speed;
        this.col = col;
    }
    public Mesh getMesh(){
        return mesh;
    }
    public float getSpeed(){
        return speed;
    }
    public OBBCollider getCollider(){
        return col;
    }
}
