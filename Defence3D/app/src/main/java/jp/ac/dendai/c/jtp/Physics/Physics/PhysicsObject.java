package jp.ac.dendai.c.jtp.Physics.Physics;


import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;

/**
 * Created by Goto on 2016/08/31.
 */
public class PhysicsObject {
    enum COLLISION{
        STAY,
        NON
    }
    COLLISION collisionMode = COLLISION.NON;
    public int mask;
    public int tag;
    public String name;
    public float mass;
    public GameObject gameObject;
    public Vector velocity;
    Vector impulseVelocity;
    Vector bufferVelocity;
    Vector bufferPos;
    Vector bufferRot;
    Vector bufferScl;
    public boolean freeze;
    public boolean useGravity;
    Physics3D.PhysicsItem regist;

    public PhysicsObject(GameObject gameObject){
        this.gameObject = gameObject;
        this.gameObject.setPhysicsObject(this);
        name = "";
        velocity = new Vector3();
        impulseVelocity = new Vector3();
        bufferVelocity = new Vector3();
        bufferPos = new Vector3();
        bufferRot = new Vector3();
        bufferScl = new Vector3();
        useGravity = true;
        freeze = false;
    }

    public void reset(){
        velocity.zeroReset();
        bufferPos.zeroReset();
        bufferRot.zeroReset();
        bufferScl.zeroReset();
        bufferVelocity.zeroReset();
    }

    public boolean isRegist(){
        return regist != null;
    }

    public Vector getBufferVelocity(){
        return bufferVelocity;
    }
    public Physics3D getPhysics3D(){
        if(isRegist())
            return regist.owner;
        return null;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getRegistInfo(){
        return "object name:"+name+" regist"+regist+"\n regist info:"+regist.getInfo();
    }
    public String toInfo(){
        return "owner name:"+gameObject.getName()+" freeze:"+freeze;
    }
    @Override
    public String toString(){
        return "name:["+gameObject.getName()+"]";
    }
}
