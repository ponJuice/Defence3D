package jp.ac.dendai.c.jtp.Math;

/**
 * Created by Goto on 2016/06/28.
 */
public abstract class Vector {
    public abstract void zeroReset();
    public abstract void setX(float value);
    public abstract void setY(float value);
    public abstract void setZ(float value);
    public abstract float getX();
    public abstract float getY();
    public abstract float getZ();
    public abstract void add(Vector vec);
    public abstract void add(float scalar);
    public void add(float[] scalar){
        setX(getX() + scalar[0]);
        setY(getY() + scalar[1]);
        setZ(getZ() + scalar[2]);
    }
    public abstract void sub(Vector vec);
    public abstract void sub(float scalar);
    public abstract void scalarMult(float scalar);
    public abstract void scalarDiv(float scalar);
    public abstract float dot(Vector vec);
    public abstract Vector getCross(Vector vec);
    public abstract void cross(Vector vec);
    public abstract float getCrossX(Vector vec);
    public float getCrossX(float[] a){
        return getY()*a[2] - getZ()*a[1];
    }
    public float getCrossY(float[] a){
        return getZ()*a[0] -getX()*a[2];
    }
    public float getCrossZ(float[] a){
        return getX()*a[1]-getY()*a[0];
    }
    public abstract float getCrossY(Vector vec);
    public abstract float getCrossZ(Vector vec);
    public abstract Vector getNormalize();
    public abstract float getSqrMagnitude();
    public abstract float getMagnitude();
    public abstract void normalize();
    public abstract Vector copy();
    public abstract void copy(Vector vec);
    public void copy(float[] a){
        setX(a[0]);
        setY(a[1]);
        if(a.length >= 3)
            setZ(a[2]);
        else
            setZ(0);
    }
    public void cross(float[] a){
        float lx = getCrossX(a);
        float ly = getCrossY(a);
        float lz = getCrossZ(a);
        setX(lx);
        setY(ly);
        setZ(lz);
    }
    public static double distanceAtoB(Vector a,Vector b){
        return Math.sqrt((b.getX()-a.getX())*(b.getX()-a.getX())
                            +(b.getY()-a.getY())*(b.getY()-a.getY())
                            +(b.getZ()-a.getZ())*(b.getZ()-a.getZ()));
    }

    public static float dot_axis(float[] vec,float x,float y,float z){
        return vec[0] * x + vec[1] * y + vec[2] * z;
    }

    public static float dot(Vector a,Vector b){
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }

    public static float dot(Vector a,float[] b){
        return a.getX() * b[0] + a.getY() * b[1] + a.getZ() * b[2];
    }

    public static float dot(float[] a,Vector b){
        return b.getX() * a[0] + b.getY() * a[1] + b.getZ() * a[2];
    }

    public static float dot(float[] a,float[] b){
        return b[0] * a[0] + b[1] * a[1] + b[2] * a[2];
    }

    public static void rotateX(float rad,Vector vec,Vector out){
        float x = vec.getX();
        float y = vec.getY() * (float)Math.cos(rad) - vec.getZ() * (float)Math.sin(rad);
        float z = vec.getY() * (float)Math.sin(rad) + vec.getZ() * (float)Math.cos(rad);
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
    public static void rotateY(float rad,Vector vec,Vector out){
        float x = vec.getX() * (float)Math.cos(rad) + vec.getZ() * (float)Math.sin(rad);
        float y = vec.getY();
        float z = vec.getX() * (float)Math.sin(rad) + vec.getZ() * (float)Math.cos(rad);
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
    public static void rotateZ(float rad,Vector vec,Vector out){
        float x = vec.getX() * (float)Math.cos(rad) - vec.getY() * (float)Math.sin(rad);
        float y = vec.getX() * (float)Math.sin(rad) + vec.getY() * (float)Math.cos(rad);
        float z = vec.getZ();
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
    public static void rotateX(double rad,Vector vec,Vector out){
        float x = vec.getX();
        float y = vec.getY() * (float)Math.cos(rad) - vec.getZ() * (float)Math.sin(rad);
        float z = vec.getY() * (float)Math.sin(rad) + vec.getZ() * (float)Math.cos(rad);
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
    public static void rotateY(double rad,Vector vec,Vector out){
        float x = vec.getX() * (float)Math.cos(rad) + vec.getZ() * (float)Math.sin(rad);
        float y = vec.getY();
        float z = vec.getX() * (float)Math.sin(rad) + vec.getZ() * (float)Math.cos(rad);
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
    public static void rotateZ(double rad,Vector vec,Vector out){
        float x = vec.getX() * (float)Math.cos(rad) - vec.getY() * (float)Math.sin(rad);
        float y = vec.getX() * (float)Math.sin(rad) + vec.getY() * (float)Math.cos(rad);
        float z = vec.getZ();
        out.setX(x);
        out.setY(y);
        out.setZ(z);
    }
}
