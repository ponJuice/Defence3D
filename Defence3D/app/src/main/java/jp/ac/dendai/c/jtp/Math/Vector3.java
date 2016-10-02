package jp.ac.dendai.c.jtp.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public class Vector3 extends Vector {
    private int dimention = 4;
    private float[] value;

    public Vector3(){
        value = new float[dimention];
        value[0] = 0;
        value[1] = 0;
        value[2] = 0;
    }

    public Vector3(Vector vec) {
        value = new float[dimention];
        value[0] = vec.getX();
        value[1] = vec.getY();
        value[2] = vec.getZ();
    }

    public Vector3(float x,float y,float z){
        value = new float[dimention];
        this.value[0] = x;
        this.value[1] = y;
        this.value[2] = z;
    }

    @Override
    public void zeroReset() {
        value[0] = 0;
        value[1] = 0;
        value[2] = 0;
    }

    @Override
    public void setX(float value) {
        this.value[0] = value;
    }

    @Override
    public void setY(float value) {
        this.value[1] = value;
    }

    @Override
    public void setZ(float value) {
        this.value[2] = value;
    }

    @Override
    public float getX() {
        return this.value[0];
    }

    @Override
    public float getY() {
        return this.value[1];
    }

    @Override
    public float getZ() {
        return this.value[2];
    }

    @Override
    public void add(Vector vec) {
        this.value[0] += vec.getX();
        this.value[1] += vec.getY();
        this.value[2] += vec.getZ();
    }

    @Override
    public void add(float scalar) {
        this.value[0] += scalar;
        this.value[1] += scalar;
        this.value[2] += scalar;
    }

    @Override
    public void sub(Vector vec) {
        this.value[0] -= vec.getX();
        this.value[1] -= vec.getY();
        this.value[2] -= vec.getZ();
    }

    @Override
    public void sub(float scalar) {
        value[0] -= scalar;
        value[1] -= scalar;
        value[2] -= scalar;
    }

    @Override
    public void scalarMult(float scalar) {
        value[0] = value[0] * scalar;
        value[1] = value[1] * scalar;
        value[2] = value[2] * scalar;
    }

    @Override
    public void scalarDiv(float scalar) {
        if(scalar != 0){
            value[0] /= scalar;
            value[1] /= scalar;
            value[2] /= scalar;
        }
    }

    @Override
    public float dot(Vector vec) {
        return value[0]*vec.getX() + value[1]*vec.getY() + value[2]*vec.getZ();
    }

    @Override
    public Vector getCross(Vector vec) {
        Vector3 vec3 = new Vector3();
        vec3.setX(getCrossX(vec));
        vec3.setY(getCrossY(vec));
        vec3.setZ(getCrossZ(vec));
        return vec3;
    }

    @Override
    public void cross(Vector vec) {
        float lx = getCrossX(vec);
        float ly = getCrossY(vec);
        float lz = getCrossZ(vec);
        value[0] = lx;
        value[1] = ly;
        value[2] = lz;
    }

    @Override
    public float getCrossX(Vector vec) {
        return value[1]*vec.getZ() - value[2]*vec.getY();
    }

    @Override
    public float getCrossY(Vector vec) {
        return value[2]*vec.getX() -value[0]*vec.getZ();
    }

    @Override
    public float getCrossZ(Vector vec) {
        return value[2]*vec.getY()-value[1]*vec.getX();
    }

    @Override
    public Vector getNormalize() {
        Vector3 vec3 = new Vector3(this);
        vec3.normalize();
        return vec3;
    }

    @Override
    public float getSqrMagnitude() {
        return value[0]*value[0] + value[1]*value[1] + value[2]*value[2];
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(getSqrMagnitude());
    }

    @Override
    public void normalize() {
        //ゼロベクトルなら何もせずに終了
        if(value[0] == 0 && value[1] == 0 && value[2] == 0)
            return;
        float dim = getMagnitude();
        value[0] /= dim;
        value[1] /= dim;
        value[2] /= dim;
    }

    @Override
    public Vector copy() {
        return new Vector3(this);
    }

    @Override
    public void copy(Vector vec) {
        value[0] = vec.getX();
        value[1] = vec.getY();
        value[2] = vec.getZ();
    }

    @Override
    public float[] getRawValue() {
        return value;
    }

    @Override
    public String toString(){
        return "("+value[0]+","+value[1]+","+value[2]+")";
    }
}
