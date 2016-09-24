package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.Player;

/**
 * Created by wark on 2016/09/25.
 */

public class PlayerRenderMediator extends RenderMediator {
    protected Player player;
    public PlayerRenderMediator(Player p){
        this.player = p;
    }
    @Override
    public void draw(){
        if(renderer == null)
            return;
        for(int n = 0;n < player.getParts().length;n++){
            if(player.getParts()[n].getRenderMediator().renderer == null)
                player.getParts()[n].getRenderMediator().renderer = renderer;
            player.getParts()[n].getRenderMediator().draw();
        }
        /*renderer.shader.draw(mesh
                ,gameObject.getPos().getX(),gameObject.getPos().getY(),gameObject.getPos().getZ()
                ,gameObject.getScl().getX(),gameObject.getScl().getY(),gameObject.getScl().getZ()
                ,gameObject.getRot().getX(),gameObject.getRot().getY(),gameObject.getRot().getZ()
                ,alpha,mode);*/
    }
}
