package jp.ac.dendai.c.jtp.Physics.Collider;

import android.opengl.Matrix;
import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Math.Vector;
import jp.ac.dendai.c.jtp.Math.Vector3;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/09/23.
 */

public class OBBCollider extends ACollider{
    protected OBBCollider next;
    protected boolean useOBB = true;
    protected float[][] directs = new float[3][4];      //0:x 1:y 2:z
    protected float[] base_length = new float[3];
    protected float[] rotate_chash = new float[3];
    protected static Vector3 interval = new Vector3();
    protected static Vector3 crossBuffer = new Vector3();
    protected static float[] rotateMatrix = new float[16];

    public OBBCollider(float ox,float oy,float oz,float lx,float ly,float lz){
        //this.gameObject = target;
        base_length[0] = lx;
        base_length[1] = ly;
        base_length[2] = lz;
        offset[0] = ox;
        offset[1] = oy;
        offset[2] = oz;
        base_offset[0] = ox;
        base_offset[1] = oy;
        base_offset[2] = oz;
        directs[0] = new float[]{1,0,0,0};
        directs[1] = new float[]{0,1,0,0};
        directs[2] = new float[]{0,0,1,0};
    }

    public void setUseOBB(boolean flag){
        useOBB = flag;
    }

    public boolean getUseOBB(){
        return useOBB;
    }

    public OBBCollider getNext(){
        return next;
    }
    public void setNext(OBBCollider obb){
        next = obb;
    }

    public void calcRotate(){
        if(gameObject.getRot().getX() == rotate_chash[0]
                && gameObject.getRot().getY() == rotate_chash[1]
                && gameObject.getRot().getZ() == rotate_chash[2]) {
            return;
        }

        Matrix.setIdentityM(rotateMatrix,0);
        if(gameObject.getRot().getZ() != 0)
            Matrix.rotateM(rotateMatrix, 0, gameObject.getRot().getZ(), 0, 0, 1);
        if(gameObject.getRot().getY() != 0)
            Matrix.rotateM(rotateMatrix, 0, gameObject.getRot().getY(), 0, 1, 0);
        if(gameObject.getRot().getX() != 0)
            Matrix.rotateM(rotateMatrix, 0, gameObject.getRot().getX(), 1, 0, 0);

        directs[0][0] = 1;
        directs[0][1] = 0;
        directs[0][2] = 0;
        Matrix.multiplyMV(directs[0], 0, rotateMatrix, 0, directs[0], 0);
        directs[1][0] = 0;
        directs[1][1] = 1;
        directs[1][2] = 0;
        Matrix.multiplyMV(directs[1], 0, rotateMatrix, 0, directs[1], 0);
        directs[2][0] = 0;
        directs[2][1] = 0;
        directs[2][2] = 1;
        Matrix.multiplyMV(directs[2], 0, rotateMatrix, 0, directs[2], 0);

        Matrix.multiplyMV(offset,0,rotateMatrix,0,base_offset,0);

    }

    public static boolean isCollisionAABB(OBBCollider A,OBBCollider B){
        float a_to_b_x = Math.abs((A.gameObject.getPos().getX()+A.offset[0]) - (B.gameObject.getPos().getX()+B.offset[0]));
        float a_to_b_y = Math.abs((A.gameObject.getPos().getY()+A.offset[1]) - (B.gameObject.getPos().getY()+B.offset[1]));
        float a_to_b_z = Math.abs((A.gameObject.getPos().getZ()+A.offset[2]) - (B.gameObject.getPos().getZ()+B.offset[2]));

        Log.d("AABB","offset x:"+A.offset[0]+" y:"+A.offset[1]+" z:"+A.offset[2]);

        float width_mul_2_A = Math.abs(Vector.dot_axis(A.directs[0],1,0,0)) + Math.abs(Vector.dot_axis(A.directs[1],1,0,0)) + Math.abs(Vector.dot_axis(A.directs[2],1,0,0));
        width_mul_2_A *= A.gameObject.getScl().getX() * A.base_length[0];
        float width_mul_2_B = Math.abs(Vector.dot_axis(B.directs[0],1,0,0)) + Math.abs(Vector.dot_axis(B.directs[1],1,0,0)) + Math.abs(Vector.dot_axis(B.directs[2],1,0,0));
        width_mul_2_B *= B.gameObject.getScl().getX() * B.base_length[0];

        float height_mul_2_A = Math.abs(Vector.dot_axis(A.directs[0],0,1,0)) + Math.abs(Vector.dot_axis(A.directs[1],0,1,0)) + Math.abs(Vector.dot_axis(A.directs[2],0,1,0));
        height_mul_2_A *= A.gameObject.getScl().getY() * A.base_length[1];
        float height_mul_2_B = Math.abs(Vector.dot_axis(B.directs[0],0,1,0)) + Math.abs(Vector.dot_axis(B.directs[1],0,1,0)) + Math.abs(Vector.dot_axis(B.directs[2],0,1,0));
        height_mul_2_B *= B.gameObject.getScl().getY() * B.base_length[1];

        float depth_mul_2_A = Math.abs(Vector.dot_axis(A.directs[0],0,0,1)) + Math.abs(Vector.dot_axis(A.directs[1],0,0,1)) + Math.abs(Vector.dot_axis(A.directs[2],0,0,1));
        depth_mul_2_A *= A.gameObject.getScl().getZ() * A.base_length[2];
        float depth_mul_2_B = Math.abs(Vector.dot_axis(B.directs[0],0,0,1)) + Math.abs(Vector.dot_axis(B.directs[1],0,0,1)) + Math.abs(Vector.dot_axis(B.directs[2],0,0,1));
        depth_mul_2_B *= B.gameObject.getScl().getZ() * B.base_length[2];

        boolean x_flag = (width_mul_2_A + width_mul_2_B) >= a_to_b_x;
        boolean y_flag = (height_mul_2_A + height_mul_2_B) >= a_to_b_y;
        boolean z_flag = (depth_mul_2_A + depth_mul_2_B) > a_to_b_z;
        return x_flag && y_flag & z_flag;
    }

    @Override
    public void debugDraw(){
        Constant.debugDraw(gameObject.getPos().getX() + offset[0],gameObject.getPos().getY() + offset[1],gameObject.getPos().getZ() + offset[2]
                        ,base_length[0] * gameObject.getScl().getX(),base_length[1] * gameObject.getScl().getY(),base_length[2] * gameObject.getScl().getZ()
                        ,gameObject.getRot().getX(),gameObject.getRot().getY(),gameObject.getRot().getZ()
                        ,1);
    }

    public float[] getOffset(){
        return offset;
    }

    public static boolean isCollision(OBBCollider A,OBBCollider B){
        //どちらもOBBでの判定をしないのであれば常にtrueを返す
        if(!A.useOBB && !B.useOBB)
            return true;

        //２点間の距離
        //いったんバッファとして使用
        crossBuffer.copy(B.gameObject.getPos());
        crossBuffer.add(B.offset);
        interval.copy(A.gameObject.getPos());
        interval.add(A.offset);

        interval.sub(crossBuffer);

        //使い終わったらゼロリセット
        crossBuffer.zeroReset();

        float rA = A.gameObject.getScl().getX() * A.base_length[0];
        float rB = calcLengthOfAxis(B,A.directs[0]);
        float L = calcLength(interval,A.directs[0]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        rA = A.gameObject.getScl().getY() * A.base_length[1];
        rB = calcLengthOfAxis(B,A.directs[1]);
        L = calcLength(interval,A.directs[1]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        rA = A.gameObject.getScl().getZ() * A.base_length[2];
        rB = calcLengthOfAxis(B,A.directs[2]);
        L = calcLength(interval,A.directs[2]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        rA = calcLengthOfAxis(A,B.directs[0]);
        rB = B.gameObject.getScl().getX() * B.base_length[0];
        L = calcLength(interval,B.directs[0]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        rA = calcLengthOfAxis(A,B.directs[1]);
        rB = B.gameObject.getScl().getY() * B.base_length[1];
        L = calcLength(interval,B.directs[1]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        rA = calcLengthOfAxis(A,B.directs[2]);
        rB = B.gameObject.getScl().getZ() * B.base_length[2];
        L = calcLength(interval,B.directs[2]);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[0]);
        crossBuffer.cross(B.directs[0]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,1,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,1,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[0]);
        crossBuffer.cross(B.directs[1]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,1,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[0]);
        crossBuffer.cross(B.directs[2]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,1,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,1);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[1]);
        crossBuffer.cross(B.directs[0]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,1,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[1]);
        crossBuffer.cross(B.directs[1]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[1]);
        crossBuffer.cross(B.directs[2]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,2);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,1);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[2]);
        crossBuffer.cross(B.directs[0]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,1);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,1,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[2]);
        crossBuffer.cross(B.directs[1]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,1);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,2);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        crossBuffer.copy(A.directs[2]);
        crossBuffer.cross(B.directs[2]);
        rA = calcLengthOfAxis2(A,B,A,crossBuffer,0,1);
        rB = calcLengthOfAxis2(A,B,B,crossBuffer,0,1);
        L = calcLength(interval,crossBuffer);
        if(L > (rA + rB)){
            //衝突していない
            return false;
        }

        return true;
    }

    protected static float calcLength(Vector3 inter,Vector3 axis){
        return Math.abs(Vector.dot(inter,axis));
    }

    protected static float calcLength(Vector3 inter,float[] axis){
        return Math.abs(Vector.dot(inter,axis));
    }

    protected static float calcLengthOfAxis(OBBCollider target,float[] axis){
        return Math.abs(Vector.dot(target.directs[0],axis) * target.gameObject.getScl().getX() * target.base_length[0])
                + Math.abs(Vector.dot(target.directs[1],axis) * target.gameObject.getScl().getY() * target.base_length[1])
                + Math.abs(Vector.dot(target.directs[2],axis) * target.gameObject.getScl().getZ() * target.base_length[2]);
    }

    protected static float calcLengthOfAxis2(OBBCollider A,OBBCollider B,OBBCollider C,Vector3 cross,int num1,int num2){
        float c1,c2;
        if(num1 == 0)
            c1 = C.gameObject.getScl().getX();
        else if(num1 == 1)
            c1 = C.gameObject.getScl().getY();
        else
            c1 = C.gameObject.getScl().getZ();

        if(num2 == 0)
            c2 = C.gameObject.getScl().getX();
        else if(num1 == 2)
            c2 = C.gameObject.getScl().getY();
        else
            c2 = C.gameObject.getScl().getZ();
        return Math.abs(Vector.dot(C.directs[num1],cross) * c1 * C.base_length[num1])
                +Math.abs(Vector.dot(C.directs[num2],cross) * c2 * C.base_length[num2]);
    }
}
