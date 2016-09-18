package jp.ac.dendai.c.jtp.Graphics.Renderer;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;

/**
 * Created by テツヤ on 2016/09/18.
 */
public class UiRenderer{
    protected ArrayList<UI> uis;
    protected UiShader shader;

    public UiRenderer(){
        uis = new ArrayList<>();
    }

    public void setShader(UiShader shader) {
        this.shader = shader;
    }

    public void addItem(UI ui) {
        uis.add(ui);
    }

    public void removeItem(UI ui) {
        uis.remove(ui);
    }

    public void clear() {
        uis.clear();
    }

    public void drawAll(){
        shader.useShader();
        shader.updateCamera();
        for(int n = 0;n < uis.size();n++){
            uis.get(n).draw(shader);
        }
    }
}
