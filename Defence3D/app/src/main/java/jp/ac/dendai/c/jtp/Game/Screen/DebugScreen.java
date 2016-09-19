package jp.ac.dendai.c.jtp.Game.Screen;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/19.
 */
public class DebugScreen extends Screenable {
    protected NumberText nt;

    public DebugScreen(){
        nt = new NumberText("メイリオ");
        nt.textSize = 0.1f;
        nt.x = 0.05f;
        nt.y = GLES20Util.getHeight_gl()-0.05f;
    }

    @Override
    public void Proc() {
        nt.setNumber(FpsController.getFps());
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        nt.draw((UiShader) Constant.getShader(Constant.SHADER.ui));
    }

    @Override
    public void Touch() {

    }

    @Override
    public void death() {

    }

    @Override
    public void init() {

    }
}
