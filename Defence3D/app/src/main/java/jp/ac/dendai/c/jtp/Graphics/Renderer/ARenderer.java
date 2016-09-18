package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;

/**
 * Created by テツヤ on 2016/09/18.
 */
public abstract class ARenderer {
    class RenderItem{
        public RenderItem next,prev;
        public RenderMediator rm;
    }
    protected int registItemNum = 0;
    protected int maxItemNum = 0;
    protected RenderItem ite;

    //private RenderItem item;
    protected Shader shader;
    protected boolean enabled;

    public abstract void setShader(Shader shader);
    public abstract void addItem(GameObject object);
    public abstract void removeItem(GameObject object);
    public abstract void clear();
}
