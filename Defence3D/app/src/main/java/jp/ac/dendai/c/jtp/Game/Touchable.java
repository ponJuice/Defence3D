package jp.ac.dendai.c.jtp.Game;

import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by Goto on 2016/09/23.
 */

public interface Touchable {
    public boolean touch(Touch touch);
    public boolean getTouchThrough();
    public void setTouchThrough(boolean flag);
}
