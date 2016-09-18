package jp.ac.dendai.c.jtp.Graphics.Model.Model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/21.
 */
public class ModelObject extends Mesh {
    protected Face[] face;
    protected int vertexBufferObject = -1,indexBufferObject = -1;
    public ModelObject(FloatBuffer vertex,IntBuffer v_indices,Face[] faces){
        this.vertex = vertex;
        this.index = v_indices;
        this.face = faces;
    }
    public ModelObject(Float[] vertex,Integer[] v_indices,Face[] faces) {
        this.vertex = Model.makeFloatBuffer(vertex);
        this.index = Model.makeIntBuffer(v_indices);
        this.face = faces;
    }

    @Override
    public int getVBO() {
        return vertexBufferObject;
    }

    @Override
    public int getIBO() {
        return indexBufferObject;
    }

    @Override
    protected void setVBO(int o) {
        vertexBufferObject = o;
    }

    @Override
    protected void setIBO(int i) {
        indexBufferObject = i;
    }

    @Override
    public Face[] getFaces() {
        return face;
    }
}
