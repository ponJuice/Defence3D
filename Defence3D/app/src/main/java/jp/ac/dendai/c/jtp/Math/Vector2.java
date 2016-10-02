package jp.ac.dendai.c.jtp.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public class Vector2 extends Vector{
    float[] value;
    public Vector2(Vector vec){
        value = new float[2];
        value[0] = vec.getX();
        value[1] = vec.getY();
    }
    public Vector2(){
        value = new float[2];
        zeroReset();
    }
    public Vector2(float x,float y){
        this.value = new float[2];
        this.value[0] = x;
        this.value[1] = y;
    }
    @Override
    public void zeroReset(){
        value[0] = 0;
        value[1] = 0;
    }

    @Override
    public void setX(float value){
        this.value[0] = value;
    }
    @Override
    public void setY(float value){
        this.value[1] = value;
    }
    @Override
    public void setZ(float value){
    }
    @Override
    public float getX(){
        return value[0];
    }
    @Override
    public float getY(){
        return value[1];
    }
    @Override
    public float getZ(){
        return 0;
    }

    @Override
    public void add(Vector vec) {
        value[0] += vec.getX();
        value[1] += vec.getY();
    }

    @Override
    public void add(float scalar) {
        value[0] += scalar;
        value[1] += scalar;
    }

    @Override
    public void sub(Vector vec) {
        value[0] -= vec.getX();
        value[1] -= vec.getY();
    }

    @Override
    public void sub(float scalar) {
        value[0] -= scalar;
        value[1] -= scalar;
    }

    @Override
    public void scalarMult(float scalar) {
        value[0] *= scalar;
        value[1] *= scalar;
    }

    @Override
    public void scalarDiv(float scalar) {
        if(scalar != 0){
            value[0] /= scalar;
            value[1] /= scalar;
        }
    }

    @Override
    public float dot(Vector vec){
        return vec.getX()*value[0]+vec.getY()*value[1];
    }
    @Override
    public Vector getCross(Vector vec){
        Vector3 vec3 = new Vector3();
        vec3.setX(getCrossX(vec));
        vec3.setY(getCrossY(vec));
        vec3.setZ(getCrossZ(vec));
        return vec3;
    }
    @Override
    public void cross(Vector vec){
        float lx = getCrossX(vec);
        float ly = getCrossY(vec);
        float lz = getCrossZ(vec);
        value[0] = lx;
        value[1] = ly;
        value[0] = lz;
    }
    @Override
    public float getCrossX(Vector vec){
        return value[1]*vec.getZ();
    }
    @Override
    public float getCrossY(Vector vec){
        return -value[0]*vec.getZ();
    }
    @Override
    public float getCrossZ(Vector vec){
        return value[0]*vec.getY()-value[1]*vec.getX();
    }
    @Override
    public Vector getNormalize(){
        Vector2 vec2 = new Vector2(this);
        vec2.normalize();
        return vec2;
    }

    @Override
    public float getSqrMagnitude() {
        return value[0]*value[0] + value[1]*value[1];
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(getSqrMagnitude());
    }

    @Override
    public void normalize() {
        //ゼロベクトルなら何もせずに終了
        if(value[0] == 0 && value[1] == 0)
            return;
        float dim = (float)Math.sqrt(value[0]*value[0] + value[1]*value[1]);
        value[0] /= dim;
        value[1] /= dim;
    }
    @Override
    public Vector copy(){
        return new Vector2(this);
    }
    @Override
    public void copy(Vector vec){
        value[0] = vec.getX();
        value[1] = vec.getY();
    }

    @Override
    public float[] getRawValue() {
        return value;
    }

    @Override
    public String toString(){
        return "("+value[0]+","+value[1]+")";
    }
}
