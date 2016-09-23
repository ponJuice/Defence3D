package jp.ac.dendai.c.jtp.Game;

import android.util.Log;

import jp.ac.dendai.c.jtp.Graphics.Renderer.RenderMediator;
import jp.ac.dendai.c.jtp.Graphics.Renderer.Renderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.AABBCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject implements FrameListener{
    protected Vector pos,rot,scl;
    protected OBBCollider collider;
    protected PhysicsObject po;
    protected RenderMediator rm;
    protected CollisionListener cl;
    protected String name;
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
    public OBBCollider getCollider(){
        return collider;
    }
    public void setCollider(OBBCollider col){
        OBBCollider ob = col;
        do{
            ob.setGameObject(this);
            ob = ob.getNext();
        }while(ob != null);
        collider = col;
    }
    public void setDebugColliderDraw(boolean flag){
        OBBCollider ob = collider;
        do{
            ob.setDebugDraw(flag);
            ob = ob.getNext();
        }while(ob != null);
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
    public CollisionListener getCollisionListener(){
        return cl;
    }
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    @Override
    public void update() {

    }
}
