package jp.ac.dendai.c.jtp.Physics.Listener;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;

/**
 * Created by wark on 2016/09/10.
 */
public interface CollisionListener {
    public void collEnter(GameObject owner,ACollider col);
    public void collExit(GameObject owner);
    public void collStay(GameObject owner);
}
