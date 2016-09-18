package jp.ac.dendai.c.jtp.Physics.Listener;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Physics.Collider.ACollider;

/**
 * Created by wark on 2016/09/10.
 */
public interface CollisionListener {
    public void collEnter(ACollider col,GameObject owner);
    public void collExit(ACollider col,GameObject owner);
    public void collStay(ACollider col,GameObject owner);
}
