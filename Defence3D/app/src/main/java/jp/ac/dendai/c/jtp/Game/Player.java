package jp.ac.dendai.c.jtp.Game;

import android.opengl.Matrix;

import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Model.Primitive.Plane;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by テツヤ on 2016/09/04.
 */
public class Player extends GameObject{
    protected GameObject[] parts;
    protected Vector3 direct;
    protected float radius = 1f;
    protected float[] p = {0,0,0,1f};
    protected float[] l = {0,0,0,1f};
    protected float[] t = new float[16];
    protected Touch touch;
    protected Camera camera;

    public Player(GameObject[] parts){
        direct = new Vector3(0,1f,-5f);
        direct.normalize();
        this.parts = parts;
        for(int n = 0;n < parts.length;n++){
            parts[n].pos = this.pos;
            parts[n].scl = this.scl;
        }
    }

    public void setRadius(float r){
        radius = r;
    }

    public float getRadius(){
        return radius;
    }

    public void touch(Touch touch){
        if(this.touch == null && touch.getTouchID() == -1)
            return;
        if(this.touch == null)
            this.touch = touch;
        else if(this.touch != touch || this.touch.getTouchID() == -1)
            return;
        rot.setY(rot.getY() + this.touch.getDelta(Touch.Pos_Flag.X)/10f);
        rot.setX(rot.getX() + this.touch.getDelta(Touch.Pos_Flag.Y)/10f);

        parts[0].getRot().setY(rot.getY());
        parts[1].getRot().setY(rot.getY());
        parts[1].getRot().setX(rot.getX());

    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }
    public void proc(){
        p[0] = pos.getX() + direct.getX()*radius;
        p[1] = pos.getY() + direct.getY()*radius;
        p[2] = pos.getZ() + direct.getZ()*radius;
        p[3] = 1f;

        l[0] = pos.getX();
        l[1] = pos.getY() + direct.getY()*radius;
        l[2] = pos.getZ();
        l[3] = 1f;

        Matrix.setIdentityM(t,0);
        Matrix.rotateM(t,0,rot.getY(),0,1,0);
        Matrix.rotateM(t,0,rot.getX(),1,0,0);

        Matrix.multiplyMV(p,0,t,0,p,0);
        Matrix.multiplyMV(l,0,t,0,l,0);

        camera.setPosition(p[0],p[1],p[2]);
        camera.setLookPosition(l[0],l[1],l[2]);
    }
}
