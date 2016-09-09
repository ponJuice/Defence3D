package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/03.
 */
public class RenderMediator {
    public float alpha;
    public GLES20COMPOSITIONMODE mode = GLES20COMPOSITIONMODE.ALPHA;
    public Mesh mesh;
    public GameObject gameObject;
    public Renderer renderer;
    public Renderer.RenderItem item;
    public boolean isDraw = true;
    public void draw(){
        if(mesh == null || renderer == null)
            return;
        if(gameObject.getCollider() != null && gameObject.getCollider().getDebugDraw()){
            gameObject.getCollider().debugDraw(renderer.shader,gameObject);
        }
        renderer.shader.draw(mesh
                ,gameObject.getPos().getX(),gameObject.getPos().getY(),gameObject.getPos().getZ()
                ,gameObject.getScl().getX(),gameObject.getScl().getY(),gameObject.getScl().getZ()
                ,gameObject.getRot().getX(),gameObject.getRot().getY(),gameObject.getRot().getZ()
                ,alpha,mode);
    }
}
