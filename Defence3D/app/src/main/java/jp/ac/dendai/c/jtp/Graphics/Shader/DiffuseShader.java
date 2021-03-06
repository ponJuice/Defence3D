package jp.ac.dendai.c.jtp.Graphics.Shader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.Model.Mesh;
import jp.ac.dendai.c.jtp.Graphics.Model.Model.Model;
import jp.ac.dendai.c.jtp.Graphics.Model.Texture;
import jp.ac.dendai.c.jtp.openglesutil.Util.FileManager;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/08/29.
 */
public class DiffuseShader extends Shader{
    public DiffuseShader(){
        super(FileManager.readTextFile("DiffuseShaderVertex.txt")
                ,FileManager.readTextFile("DiffuseShaderFragment.txt"));
    }

    @Override
    protected void createTexture() {
        textures = new int[1];
        // テクスチャオブジェクトを作成する
        GLES20.glGenTextures(1, textures, 0);
    }

    @Override
    void loadShaderVariable() {
        u_Sampler = GLES20Util.getUniformLocation(program,"u_Sampler");
    }

    @Override
    void setMaterial(Face face) {
        setOnTexture(face.matelial.tex_diffuse, u_Sampler);
    }

    @Override
    public void draw(Model model, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ,GLES20COMPOSITIONMODE mode) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, x, y, z);
        if(degreeZ != 0)
            Matrix.rotateM(modelMatrix, 0, degreeZ, 0, 0, 1);
        if(degreeY != 0)
            Matrix.rotateM(modelMatrix, 0, degreeY, 0, 1, 0);
        if(degreeX != 0)
            Matrix.rotateM(modelMatrix, 0, degreeX, 1, 0, 0);
        if(scaleX != 0 || scaleY != 0 || scaleZ != 0)
            Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        mode.setBlendMode();
        setShaderModelMatrix(modelMatrix);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, model.vertexBufferObject[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, model.indexBufferObject[0]);
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, GLES20Util.FSIZE * 6);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        /*for(int n = 0;n < model.models[0].faces.length;n++) {
            setMaterial(model.models[0].faces[n]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.models[0].faces[n].end-model.models[0].faces[n].offset+1, GLES20.GL_UNSIGNED_INT, GLES20Util.ISIZE*model.models[0].faces[n].offset);
        }*/
        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }

    @Override
    public void draw(Mesh mesh, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ,float alpha,GLES20COMPOSITIONMODE mode) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, x, y, z);
        if(degreeZ != 0)
            Matrix.rotateM(modelMatrix, 0, degreeZ, 0, 0, 1);
        if(degreeY != 0)
            Matrix.rotateM(modelMatrix, 0, degreeY, 0, 1, 0);
        if(degreeX != 0)
            Matrix.rotateM(modelMatrix, 0, degreeX, 1, 0, 0);
        if(scaleX != 1 || scaleY != 1 || scaleZ != 1)
            Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        mode.setBlendMode();
        setShaderModelMatrix(modelMatrix);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.getVBO());
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化



        //テクスチャの有効化
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, GLES20Util.FSIZE * 8, GLES20Util.FSIZE * 6);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        setMaterial(mesh.getFaces()[0]);
        GLES20.glUniform1f(u_alpha,alpha);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getFaces()[0].end - mesh.getFaces()[0].offset + 1, GLES20.GL_UNSIGNED_INT, GLES20Util.ISIZE * mesh.getFaces()[0].offset);

        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }

    @Override
    public void draw(Texture tex, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float degreeX, float degreeY, float degreeZ, float alpha) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, -10f);
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 1.0f);
        Matrix.rotateM(modelMatrix, 0, degreeZ, 0, 0, 1);
        setShaderModelMatrix(modelMatrix);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        setOnTexture(tex.getTexture(), u_Sampler);

        tex.getBlendMode().setBlendMode();
        GLES20.glUniform1f(u_alpha,alpha);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getVertexBufferObject());
        GLES20.glVertexAttribPointer(ma_Position, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化

        //テクスチャの有効化
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tex.getTextureBufferObject());
        GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);	//描画

        GLES20.glDisableVertexAttribArray(ma_Position);
        GLES20.glDisableVertexAttribArray(ma_texCoord);
    }

    @Override
    protected void _clear() {

    }
}
