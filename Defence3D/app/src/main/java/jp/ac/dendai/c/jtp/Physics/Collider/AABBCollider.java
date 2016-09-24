package jp.ac.dendai.c.jtp.Physics.Collider;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;

/**
 * Created by テツヤ on 2016/09/17.
 */
public class AABBCollider extends ACollider {
    float width,height,depth;
    public AABBCollider(float width,float height,float depth){
        this.width = width;
        this.height = height;
        this.depth = depth;
    }


    @Override
    public void debugDraw() {

    }
}
