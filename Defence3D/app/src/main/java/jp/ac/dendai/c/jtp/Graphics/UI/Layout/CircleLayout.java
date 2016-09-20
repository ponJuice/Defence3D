package jp.ac.dendai.c.jtp.Graphics.UI.Layout;

import java.util.ArrayList;

import jp.ac.dendai.c.jtp.Graphics.UI.UI;

/**
 * Created by Goto on 2016/09/20.
 */
public class CircleLayout {
    protected float radius = 1;
    protected float cx = 0,cy = 0;
    protected float time = 0;
    protected ArrayList<UI> ui;
    public void addItem(UI u){
        ui.add(u);
    }
    public void draw(){
        for(int n = 0;n < ui.size();n++){
            //ui.get(n).
        }
    }
}
