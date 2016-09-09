package jp.ac.dendai.c.jtp.Physics.Collider;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.ModelObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Math.Vector;

/**
 * Created by Goto on 2016/08/31.
 */
public class CircleCollider extends ACollider{
    protected float radius;
    protected ModelObject debug;
    public CircleCollider(float radius){
        this.radius  = radius;
    }
    public void setDebugModel(ModelObject model){
        debug = model;
    }
    @Override
    public Vector[] getDirect() {
        return new Vector[0];
    }

    @Override
    public void debugDraw(Shader shader, GameObject pos) {
        shader.draw(debug
                ,pos.getPos().getX()
                ,pos.getPos().getY()
                ,pos.getPos().getZ()
                ,radius
                ,radius
                ,radius
                ,0,0,0
                ,0.5f);
    }
}
