package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import jp.ac.dendai.c.jtp.Game.ScoreManager;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by wark on 2016/09/30.
 */

public abstract class Panel {
    protected boolean enabled = true;
    public void setEnabled(boolean flag){
        enabled = flag;
    }
    public boolean getEnabled(){
        return enabled;
    }
    public abstract void proc();
    public abstract boolean touch(boolean flag,Touch touch);
    public abstract void draw();
    public abstract UiRenderer getUiRenderer();
}
