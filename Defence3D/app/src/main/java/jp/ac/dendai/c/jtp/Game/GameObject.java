package jp.ac.dendai.c.jtp.Game;

import jp.ac.dendai.c.jtp.Graphics.Renderer.RenderMediator;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Listener.CollisionListener;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;

/**
 * Created by Goto on 2016/08/31.
 */
public class GameObject implements FrameListener{
    protected Vector pos,rot,scl;
    protected boolean debugDraw = false;
    protected OBBCollider collider;
    protected PhysicsObject po;
    protected RenderMediator rm;
    protected CollisionListener cl;
    protected String name;
    public GameObject(){
        pos = new Vector3();
        rot = new Vector3();
        scl = new Vector3(1,1,1);

        init();
    }

    protected void init(){
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

    public void setDebugDraw(boolean flag){
        if(collider == null)
            return;
        OBBCollider o = collider;
        while(o != null){
            o.setDebugDraw(flag);
            o = o.getNext();
        };
        debugDraw = flag;
    }
    public boolean isDebugDraw(){
        return debugDraw;
    }
    public void useOBB(boolean flag){
        if(collider == null)
            return;
        OBBCollider o = collider;
        do{
            o.setUseOBB(flag);
            o = o.getNext();
        }while(o != null);
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
    public void collEnter(ACollider col,GameObject owner){
        if(cl != null)
            cl.collEnter(col,owner);
    }
    public void collExit(ACollider col,GameObject owner){
        if(cl != null)
            cl.collExit(col,owner);
    }
    public void collStay(ACollider col,GameObject owner){
        if(cl != null)
            cl.collStay(col,owner);
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
