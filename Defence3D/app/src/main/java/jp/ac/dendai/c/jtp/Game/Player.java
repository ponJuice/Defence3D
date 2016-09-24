package jp.ac.dendai.c.jtp.Game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Graphics.Renderer.PlayerRenderMediator;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.PhysicsObject;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by テツヤ on 2016/09/04.
 */
public class Player extends GameObject implements Touchable{
    protected GameObject[] parts;
    protected Vector3 direct;
    protected GameObject bullet;
    protected float radius = 1f;
    protected float lookRadius = 10f;
    protected float[] p = {0,0,0,1f};
    protected float[] l = {0,0,0,1f};
    protected float[] t = new float[16];
    protected boolean through;
    protected Touch touch;
    protected Camera camera;
    protected float speed = 10f;

    public Player(GameObject[] parts){
        this.parts = parts;
        direct = new Vector3(0,1f,-5f);
        direct.normalize();
        for(int n = 0;n < parts.length;n++){
            parts[n].pos = this.pos;
            parts[n].scl = this.scl;
        }

        rm = new PlayerRenderMediator(this);
        rm.gameObject = this;

        po = new PhysicsObject(this);
        po.useGravity = false;
        po.tag = Constant.COLLISION_PLAYDER;
        po.mask = Constant.COLLISION_ENEMYBULLET;
        po.freeze = false;
        setCollider(new OBBCollider(0 ,0.2706f ,0 ,0.5412f ,0.46234f,1.07303f));
    }

    @Override
    protected void init(){
    }

    public GameObject[] getParts(){
        return parts;
    }

    public void testSetBulletSpeed(float s){
        speed = s;
    }

    public void setRadius(float r){
        radius = r;
    }

    public float getRadius(){
        return radius;
    }

    public boolean touch(Touch touch){
        if(this.touch == null)
            this.touch = touch;
        if(this.touch.getTouchID() == -1) {
            this.touch = null;
            return true;
        }
        if(this.touch != touch)
            return true;

        rot.setY(rot.getY() + this.touch.getDelta(Touch.Pos_Flag.X)/10f);
        rot.setX(rot.getX() + this.touch.getDelta(Touch.Pos_Flag.Y)/10f);

        parts[0].getRot().setY(rot.getY());
        parts[1].getRot().setY(rot.getY());
        parts[1].getRot().setX(rot.getX());
        return through;
    }

    public void setBullte(GameObject bullet){
        this.bullet = bullet;
    }

    public void attack(){
        if(bullet == null)
            return;
        bullet.getPos().copy(pos);
        bullet.getRot().copy(rot);
        bullet.getPhysicsObject().velocity.setX(l[0]);
        bullet.getPhysicsObject().velocity.setY(l[1]);
        bullet.getPhysicsObject().velocity.setZ(l[2]);
        bullet.getPhysicsObject().velocity.sub(pos);
        bullet.getPhysicsObject().velocity.normalize();
        bullet.getPhysicsObject().velocity.scalarMult(speed);
        bullet.getPhysicsObject().freeze = false;
        bullet.getRenderMediator().isDraw = true;
    }

    @Override
    public boolean getTouchThrough() {
        return through;
    }

    @Override
    public void setTouchThrough(boolean flag) {
        through = flag;
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }
    public void proc(){
        p[0] = direct.getX()*radius;
        p[1] = direct.getY()*radius;
        p[2] = direct.getZ()*radius;
        p[3] = 1f;

        l[0] = 0;
        l[1] = 0;
        l[2] = lookRadius - pos.getZ();
        l[3] = 1f;

        Matrix.setIdentityM(t,0);
        Matrix.rotateM(t,0,rot.getY(),0,1,0);
        Matrix.rotateM(t,0,rot.getX(),1,0,0);

        Matrix.multiplyMV(p,0,t,0,p,0);
        Matrix.multiplyMV(l,0,t,0,l,0);

        l[0] += pos.getX();
        l[1] += pos.getY();

        p[0] += pos.getX();
        p[1] += pos.getY();
        p[2] += pos.getZ();

        camera.setPosition(p[0],p[1],p[2]);
        camera.setLookPosition(l[0],l[1],l[2]);
    }
}
