package jp.ac.dendai.c.jtp.Physics.Collider.Util;

/**
 * Created by テツヤ on 2016/09/17.
 */

/**
 * Created by Goto on 2016/09/06.
 */
public class Rect3D {
    protected float cx,cy,cz;
    protected float top,left,bottom,right,near,far;
    public Rect3D(float centerX,float centerY,float centerZ,float width,float height,float depth){
        setRectLength(centerX, centerY,centerZ, width,height,depth);
    }

    public void setRectLength(float centerX,float centerY,float centerZ,float width,float height,float depth){
        top = centerY+height/2f;
        left = centerX-width/2f;
        bottom = centerY-height/2f;
        right = centerX+width/2f;
        near = centerZ - depth/2f;
        far = centerZ + depth/2f;
        calcCenter();
    }

    public void setRect(float left,float top,float right,float bottom,float near,float far){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.near = near;
        this.far = far;
        calcCenter();
    }

    public float getRight() {
        return right;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getTop() {
        return top;
    }

    public float getLeft() {
        return left;
    }

    public float getBottom() {
        return bottom;
    }

    public void setTop(float value){
        top = value;
        calcCenter();
    }

    public void setLeft(float value){
        left = value;
        calcCenter();
    }

    public void setBottom(float value){
        bottom = value;
        calcCenter();
    }

    public void setRight(float value){
        right = value;
        calcCenter();
    }

    public void calcCenter(){
        cx = this.left - this.right;
        cy = this.top - this.bottom;
        cz = this.far - this.near;
    }
    public boolean contains(float x,float y){
        return left <= x && x <= right && bottom <= y && y <= top;
    }
}
