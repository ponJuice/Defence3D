package jp.ac.dendai.c.jtp.Graphics.UI;

import java.util.ArrayList;

import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by テツヤ on 2016/09/24.
 */

public class UIObserver {
    private ArrayList<UI> uis;

    public UIObserver(){
        uis = new ArrayList<>();
    }

    public boolean touch(Touch[] touchArray,boolean flag){
        for(int n = 0;n < touchArray.length;n++){
            for(int m = 0;m < uis.size();m++) {
                if (flag || touchArray[n].getTouchID() == -1)
                    flag =  flag && uis.get(m).touch(touchArray[n]);
            }
        }
        return flag;
    }

    public boolean touch(Touch touch,boolean flag){
        for(int m = 0;m < uis.size();m++) {
            if (flag || touch.getTouchID() == -1)
                flag = flag && uis.get(m).touch(touch);
        }
        return flag;
    }

    public void proc(){
        for(int m = 0;m < uis.size();m++) {
            uis.get(m).proc();
        }
    }

    public void addItem(UI ui){
        uis.add(ui);
    }
    public void removeItem(UI ui){
        uis.remove(ui);
    }
}
