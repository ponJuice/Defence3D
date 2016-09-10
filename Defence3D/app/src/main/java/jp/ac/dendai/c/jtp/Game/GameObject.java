package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Renderer.RenderMediator;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject{
    protected Vector pos,rot,scl;
    protected CircleCollider collider;
    protected PhysicsObject po;
    protected RenderMediator rm;
    protected CollisionListener cl;
    public GameObject(){
        pos = new Vector3();
        rot = new Vector3();
        scl = new Vector3(1,1,1);

        //物理オブジェクト
        po = new PhysicsObject(this);
        po.mass = 1;
        po.freeze = false;
        po.useGravity = false;

        //レンダーメディエイターの設定
        rm = new RenderMediator();
        rm.gameObject = this;
        rm.alpha = 1.0f;
    }
    public void setPhysicsObject(PhysicsObject object){
        this.po = object;
    }
    public PhysicsObject getPhysicsObject(){
        return po;
    }
    public RenderMediator getRenderMediator(){
        return rm;
    }
    public Vector getPos(){
        return pos;
    }
    public CircleCollider getCollider(){
        return collider;
    }
    public void setCollider(CircleCollider col){
        collider = col;
        collider.setGameObject(this);
    }
    public void setCollisionListener(CollisionListener cl){
        this.cl = cl;
    }
    public Vector getRot(){
        return rot;
    }
    public Vector getScl(){
        return scl;
    }
    public void collEnter(ACollider col){
        if(cl == null)
            return;
        cl.collEnter(this,col);
    };
    public void collExit(){
        if(cl == null)
            return;
        cl.collExit(this);
    };
    public void collStay(){
        if(cl == null)
            return;
        cl.collStay(this);
    };
}
