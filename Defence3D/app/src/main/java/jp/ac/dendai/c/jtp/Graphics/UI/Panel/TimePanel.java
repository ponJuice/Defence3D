package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/09/30.
 */

public class TimePanel extends Panel{
    protected StaticText a;
    protected NumberText hour,minute,second;
    protected UiRenderer timeRenderer;
    protected float timeBuffer = 0;
    public TimePanel(){
        timeRenderer = new UiRenderer();
        timeRenderer.setShader((UiShader) Constant.getShader(Constant.SHADER.ui));

        a = new StaticText(":      :");
        a.useAspect(true);
        a.setHeight(0.1f);
        a.setHorizontal(UIAlign.Align.CENTOR);
        a.setVertical(UIAlign.Align.CENTOR);
        a.setX(GLES20Util.getWidth_gl()/2f - 0.4f);
        a.setY(GLES20Util.getHeight_gl()-0.05f);
        a.setR(0.5f);
        a.setG(0.5f);
        a.setB(0.5f);
        timeRenderer.addItem(a);

        hour = new NumberText("メイリオ");
        hour.setHorizontal(UIAlign.Align.RIGHT);
        hour.setVertical(UIAlign.Align.TOP);
        hour.setX(GLES20Util.getWidth_gl()/2 -0.4f-0.08f);
        hour.setY(GLES20Util.getHeight_gl() - 0.02f);
        hour.setHeight(0.1f);
        hour.setR(0.5f);
        hour.setG(0.5f);
        hour.setB(0.5f);
        hour.setStuffing(2);
        timeRenderer.addItem(hour);

        minute = new NumberText("メイリオ");
        minute.setHorizontal(UIAlign.Align.CENTOR);
        minute.setVertical(UIAlign.Align.TOP);
        minute.setX(GLES20Util.getWidth_gl()/2f - 0.4f);
        minute.setY(GLES20Util.getHeight_gl()- 0.02f);
        minute.setHeight(0.1f);
        minute.setR(0.5f);
        minute.setG(0.5f);
        minute.setB(0.5f);
        minute.setStuffing(2);
        minute.setNumber(59);
        timeRenderer.addItem(minute);

        second = new NumberText("メイリオ");
        second.setHorizontal(UIAlign.Align.LEFT);
        second.setVertical(UIAlign.Align.TOP);
        second.setX(GLES20Util.getWidth_gl()/2f - 0.4f + 0.08f);
        second.setY(GLES20Util.getHeight_gl()- 0.02f);
        second.setHeight(0.1f);
        second.setNumber(20);
        second.setR(0.5f);
        second.setG(0.5f);
        second.setB(0.5f);
        second.setStuffing(2);
        timeRenderer.addItem(second);
    }
    @Override
    public void proc() {
        float clamp = Clamp.clamp(0,1,timeBuffer/0.2f);
        a.setY(GLES20Util.getHeight_gl() - 0.75f - 0.05f* (1f - clamp));
        a.setAlpha(clamp);

        hour.setY(GLES20Util.getHeight_gl() - 0.92f - 0.05f*(1f -clamp));
        hour.setAlpha(clamp);

        minute.setY(GLES20Util.getHeight_gl() - 0.92f - 0.05f*(1f - clamp));
        minute.setAlpha(clamp);

        second.setY(GLES20Util.getHeight_gl() - 0.92f - 0.05f*(1f - clamp));
        second.setAlpha(clamp);

        timeBuffer += Time.getDeltaTime();
    }

    public void changeResult(){
        a.useAspect(true);
        a.setHeight(0.2f);
        a.setHorizontal(UIAlign.Align.CENTOR);
        a.setVertical(UIAlign.Align.CENTOR);
        a.setX(GLES20Util.getWidth_gl()/2f);
        a.setY(GLES20Util.getHeight_gl() - 0.75f);
        a.setR(0);
        a.setG(0);
        a.setB(0);

        hour.setHorizontal(UIAlign.Align.RIGHT);
        hour.setVertical(UIAlign.Align.CENTOR);
        hour.setX(GLES20Util.getWidth_gl()/2 -0.14f);
        hour.setY(GLES20Util.getHeight_gl() - 0.92f);
        hour.setHeight(0.1f);
        hour.setR(0);
        hour.setG(0);
        hour.setB(0);
        hour.setStuffing(2);

        minute.setHeight(0.1f);
        minute.setHorizontal(UIAlign.Align.CENTOR);
        minute.setVertical(UIAlign.Align.CENTOR);
        minute.setX(GLES20Util.getWidth_gl()/2f);
        minute.setY(GLES20Util.getHeight_gl() - 0.92f);
        minute.setR(0);
        minute.setG(0);
        minute.setB(0);
        minute.setStuffing(2);

        second.setHorizontal(UIAlign.Align.LEFT);
        second.setVertical(UIAlign.Align.CENTOR);
        second.setX(minute.getX() + minute.getWidth() + 0.06f);
        second.setY(GLES20Util.getHeight_gl() - 0.92f);
        second.setHeight(0.1f);
        second.setR(0);
        second.setG(0);
        second.setB(0);
        second.setStuffing(2);
    }

    public void setTime(float time){
        float t = Time.getHour(time);
        hour.setNumber((int)t);
        t = Time.getMinute(time);
        minute.setNumber((int)t);
        t = Time.getSecond(time);
        second.setNumber((int)t);
    }

    @Override
    public boolean touch(boolean flag, Touch touch) {
        return false;
    }

    @Override
    public void draw() {
        if(enabled)
            timeRenderer.drawAll();
    }

    @Override
    public UiRenderer getUiRenderer() {
        return null;
    }
}
