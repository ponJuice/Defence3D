package jp.ac.dendai.c.jtp.Physics.Collider;


import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;

/**
 * Created by Goto on 2016/08/31.
 */
public abstract class ACollider {
    //protected ACollider nextCol;
    protected GameObject gameObject;
    protected Vector3 offset = new Vector3();
    protected boolean isDebugDraw = false;
    public boolean getDebugDraw(){
        return isDebugDraw;
    }
    public void setDebugDraw(boolean flag){
        isDebugDraw = flag;
    }
    public GameObject getGameObject(){
        return gameObject;
    }
    public void setGameObject(GameObject gameObject){
        this.gameObject = gameObject;
    }
    /*public ACollider getNextCol(){
        return nextCol;
    }*/
    public abstract void debugDraw(Shader shader,GameObject pos);

    public static boolean isCollision(CircleCollider A,CircleCollider B) {
        if (A.radius + B.radius <= Vector.distanceAtoB(A.gameObject.getPos(), B.gameObject.getPos())) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isCollision(AABBCollider A,AABBCollider B){
        float a_to_b_x = Math.abs(A.gameObject.getPos().getX() - B.gameObject.getPos().getX());
        float a_to_b_y = Math.abs(A.gameObject.getPos().getY() - B.gameObject.getPos().getY());
        float a_to_b_z = Math.abs(A.gameObject.getPos().getZ() - B.gameObject.getPos().getZ());
        boolean x_flag = (A.width/2f + B.width/2f) >= a_to_b_x;
        boolean y_flag = (A.height/2f + B.height/2f) >= a_to_b_y;
        boolean z_flag = (A.depth/2f + B.depth/2f) > a_to_b_z;
        return x_flag && y_flag & z_flag;
    }
}
