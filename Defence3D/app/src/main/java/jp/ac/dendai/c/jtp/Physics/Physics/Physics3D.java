package jp.ac.dendai.c.jtp.Physics.Physics;

import android.os.Debug;
import android.provider.Settings;
import android.util.Log;

import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.AABBCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;

/**
 * Created by Goto on 2016/08/31.
 */
public class Physics3D implements Physics {
    private String name;
    class PhysicsItem{
        public Physics3D owner;
        public PhysicsItem next,prev;
        public PhysicsObject object;
        public String getInfo(){
            return "owner name:"+owner.name+"\n"+
                    "next:"+next+" prev:"+prev+"\n"+
                    "object name:"+object.getName()+"\n";
        }
    }
    private PhysicsItem ite;
    private PhysicsItem[] objects;                          //あたり判定の総当たりをやりやすくするためのもの
    private int objectCount;
    private PhysicsInfo info;
    private long time,timeBuffer,startTime;
    private float deltaTime,deltaTimeSum,currentTime;
    private Vector3 buffer;
    public Physics3D(PhysicsInfo info){
        this.info = info;
        objectCount = 0;
        buffer = new Vector3();
        ite = createRingBuffer();
        objects = new PhysicsItem[info.maxObject];
        PhysicsItem temp = ite;
        for(int n = 0;n < info.maxObject;n++){
            objects[n] = temp;
            temp = temp.next;
        }
    }
    public void addObject(PhysicsObject object){
        if(objectCount >= info.maxObject) {
            ite.object.regist = null;
        }
        else {
            objectCount++;
        }

        object.collisionMode = PhysicsObject.COLLISION.NON;
        ite.object = object;
        object.regist = ite;
        ite = ite.next;
    }
    public void removeObject(PhysicsObject object){
        if(object.regist == null)
            return;
        if(object.regist.owner != this)
            return;
        object.regist.prev.next = object.regist.next;
        object.regist.next.prev = object.regist.prev;
        object.regist.prev = ite;
        object.regist.next = ite.next;
        ite.next.prev = object.regist;
        ite.next = object.regist;
        object.regist.object = null;
        object.regist = null;
        objectCount--;
    }
    public void clearObjects(){
        PhysicsItem temp = ite;
        for(int n = 0;n < info.maxObject;n++){
            if(temp.object != null)
                temp.object.regist = null;
                temp.object = null;
            temp = temp.next;
        }
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public float getDeltaTime(){
        return deltaTime;
    }
    public float getCurrentTime(){
        return currentTime;
    }
    public float getDeltaTimeSum(){
        return deltaTimeSum;
    }
    private PhysicsItem createRingBuffer(){
        PhysicsItem start = new PhysicsItem();
        start.owner = this;
        PhysicsItem temp = start;
        for(int n = 1;n < info.maxObject;n++){
            temp.next = new PhysicsItem();
            temp.next.prev = temp;
            temp = temp.next;
            temp.owner = this;
        }
        start.prev = temp;
        temp.next = start;
        return start;
    }
    @Override
    public void preparation(){
        PhysicsItem temp = ite;
        do{
            if(temp.object != null &&!temp.object.freeze) {
                temp.object.bufferVelocity.zeroReset();
                temp.object.bufferPos.zeroReset();
                temp.object.bufferRot.zeroReset();
                temp.object.bufferScl.zeroReset();
                //OBBを回転させる
                OBBCollider t = temp.object.gameObject.getCollider();
                do{
                    t.calcRotate();
                    if(t.getDebugDraw()){
                        t.debugDraw();
                    }
                    t = t.getNext();
                }while(t != null);
            }
            temp = temp.prev;
        }while(temp != ite);
    }
    @Override
    public void externalForceProc(float deltaTime) {
        //重力
        PhysicsItem temp = ite;
        do{
            if(temp.object != null && temp.object.useGravity && !temp.object.freeze) {
                buffer.copy(info.gravity);
                buffer.scalarMult(deltaTime);
                temp.object.bufferVelocity.add(buffer);
            }
            temp = temp.next;
        }while(temp != ite);
    }

    @Override
    public void physicsProc(float deltaTime) {
        //あたり判定のみ今は実装
        for(int n = 0;n < info.maxObject;n++){
            for(int m = 1+n;m < info.maxObject;m++){
                if(objects[n].object == null)
                    break;
                if(objects[m].object == null)
                    continue;
                PhysicsObject A = objects[n].object;
                PhysicsObject B = objects[m].object;

                //衝突判定

                if(OBBCollider.isCollisionAABB(A.gameObject.getCollider(),B.gameObject.getCollider()) && OBBCollider.isCollision(A.gameObject.getCollider(),B.gameObject.getCollider())){
                    if(!A.freeze && (A.mask & B.tag) >= 1) {
                        if (A.collisionMode == PhysicsObject.COLLISION.NON) {
                            //始めて接触した→STAYに移行
                                A.gameObject.collEnter(B.gameObject.getCollider(),A.gameObject);
                                A.collisionMode = PhysicsObject.COLLISION.STAY;
                        } else if (A.collisionMode == PhysicsObject.COLLISION.STAY) {
                            //接触し続けている→そのまま
                            A.gameObject.collStay(B.gameObject.getCollider(),A.gameObject);
                        }
                    }
                    if(!B.freeze && (B.mask & A.tag) >= 1) {
                        if (B.collisionMode == PhysicsObject.COLLISION.NON) {
                            //始めて接触した→STAYに移行
                                B.gameObject.collEnter(A.gameObject.getCollider(),B.gameObject);
                                B.collisionMode = PhysicsObject.COLLISION.STAY;
                        } else if (B.collisionMode == PhysicsObject.COLLISION.STAY) {
                            //接触し続けている→そのまま
                            B.gameObject.collStay(A.gameObject.getCollider(),B.gameObject);
                        }
                    }
                }else{
                    if(A.collisionMode == PhysicsObject.COLLISION.STAY){
                        //離れた→NONに移行
                        A.gameObject.collExit(B.gameObject.getCollider(),A.gameObject);
                        A.collisionMode = PhysicsObject.COLLISION.NON;
                    }
                    if(B.collisionMode == PhysicsObject.COLLISION.STAY){
                        //離れた→NONに移行
                        B.gameObject.collExit(A.gameObject.getCollider(),B.gameObject);
                        B.collisionMode = PhysicsObject.COLLISION.NON;
                    }
                }
            }
        }
    }

    @Override
    public void updatePosProc(float deltaTime) {
        PhysicsItem temp = ite;
        do{
            if(temp.object != null && !temp.object.freeze) {
                temp.object.velocity.add(temp.object.bufferVelocity);
                temp.object.bufferPos.copy(temp.object.velocity);
                temp.object.bufferPos.scalarMult(deltaTime);
                temp.object.gameObject.getPos().add(temp.object.bufferPos);
            }
            temp = temp.next;
        }while(temp != ite);
    }

    protected long start,preparetion_end,external_end,physics_end,update_end;

    @Override
    public void simulate() {
        deltaTime = 0;
        timeBuffer = System.currentTimeMillis();
        if(time > 0)
            deltaTime = (timeBuffer - time)/1000f;
        else{
            startTime = timeBuffer;
        }
        currentTime = (timeBuffer - startTime)/1000f;
        time = timeBuffer;
        deltaTimeSum += deltaTime;
        start = System.currentTimeMillis();
        preparation();
        preparetion_end = System.currentTimeMillis();
        externalForceProc(deltaTime);
        external_end = System.currentTimeMillis();
        physicsProc(deltaTime);
        physics_end = System.currentTimeMillis();
        updatePosProc(deltaTime);
        update_end = System.currentTimeMillis();

        float pre = (float)(preparetion_end - start) /1000f;
        float ex = (float)(external_end - preparetion_end) /1000f;
        float ph = (float)(physics_end - external_end)/1000f;
        float up = (float)(update_end - physics_end)/1000f;

        Log.d("pipline","pre:"+pre+" ex:"+ex+" ph:"+ph+" up:"+up);
    }
}
