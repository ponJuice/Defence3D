package jp.ac.dendai.c.jtp.Physics.Physics;


import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;

/**
 * Created by Goto on 2016/08/31.
 */
public class PhysicsInfo {
    public Vector gravity;
    public int maxObject;
    public boolean enabled;
    public PhysicsInfo(){
        gravity = new Vector3();
        maxObject = 10;
        enabled = true;
    }
}
