package jp.ac.dendai.c.jtp.Graphics.Renderer;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Physics.Collider.OBBCollider;

/**
 * Created by Goto on 2016/09/01.
 */
public class Renderer extends ARenderer {
    public Renderer(){
        enabled = true;
    }
    public boolean getEnabled(){
        return enabled;
    }
    public void setEnabled(boolean e){
        enabled = e;
    }
    public void setShader(Shader shader){
        this.shader = shader;
    }
    public void addItem(GameObject object){
        if(ite == null) {
            //一番最初
            ite = new RenderItem();
            object.getRenderMediator().item = ite;
            ite.rm = object.getRenderMediator();
            ite.next = ite;
            ite.prev = ite;
            maxItemNum++;
        }else if(ite.next.rm != null){
            //空きがない→新しく空きを作成
            RenderItem temp = new RenderItem();
            object.getRenderMediator().item = temp;
            temp.rm = object.getRenderMediator();
            temp.prev = ite;
            temp.next = ite.next;
            ite.next.prev = temp;
            ite.next = temp;
            ite = temp;
            maxItemNum++;
        }else{
            //空きがある→そのまま代入
            object.getRenderMediator().item = ite.next;
            ite.next.rm = object.getRenderMediator();
            ite = ite.next;
        }
        object.getRenderMediator().renderer = this;
        registItemNum++;
    }
    public void removeItem(GameObject object){
        if(object.getRenderMediator().renderer == null || object.getRenderMediator().renderer != this)
            return;
        if(ite != object.getRenderMediator().item) {
            object.getRenderMediator().item.prev.next = object.getRenderMediator().item.next;
            object.getRenderMediator().item.next.prev = object.getRenderMediator().item.prev;

            object.getRenderMediator().item.prev = ite;
            object.getRenderMediator().item.next = ite.next;
            ite.next.prev = object.getRenderMediator().item;
            ite.next = object.getRenderMediator().item;
        }else{
            ite = ite.prev;
        }
        object.getRenderMediator().renderer = null;
        object.getRenderMediator().item = null;
        registItemNum--;
    }
    public void clear(){
        RenderItem temp = ite;
        do{
            if(ite.rm != null) {
                ite.rm.item = null;
                ite.rm = null;
            }
            ite = ite.next;
        }while(temp != ite);
        while(ite != null && ite.next != null){
            ite.prev.next = null;
            ite.prev = null;
            ite = ite.next;
        }
        ite = null;
    }
    public void drawAll(){
        if(ite == null)
            return;
        shader.useShader();
        shader.updateCamera();
        RenderItem temp = ite;
        long start = System.currentTimeMillis();
        do{
            if(temp.rm != null && temp.rm.isDraw) {
                temp.rm.draw();
            }
            if(temp.rm != null && temp.rm.gameObject.getDebugDraw()){
                if(temp.rm.gameObject.getCollider() != null){
                    OBBCollider o = temp.rm.gameObject.getCollider();
                    do{
                        o.debugDraw();
                        o = o.getNext();
                    }while(o != null);
                }
            }
            temp = temp.prev;
        }while(temp != null && temp != ite);
        float t = (float)(System.currentTimeMillis() - start)/1000f;
        Log.d("Renderer","rendering time:"+t);
    }
}
