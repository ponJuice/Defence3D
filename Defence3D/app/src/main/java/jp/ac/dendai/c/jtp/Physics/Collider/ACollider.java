package jp.ac.dendai.c.jtp.Physics.Collider;


import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;

/**
 * Created by Goto on 2016/08/31.
 */
public abstract class ACollider {
    protected GameObject gameObject;
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
    public abstract Vector[] getDirect();
    public abstract void debugDraw(Shader shader,GameObject pos);

    public static boolean isCollision(CircleCollider A,CircleCollider B) {
        if (A.radius + B.radius <= Vector.distanceAtoB(A.gameObject.getPos(), B.gameObject.getPos())) {
            return false;
        } else {
            return true;
        }
    }
}
