package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/30.
 */

public class GameOverPanel extends Panel{
    protected StaticText gameover,scoreText,timeText,a,b;
    protected NumberText score;
    protected NumberText hour,minute,second;
    protected UiRenderer renderer;
    protected TimePanel t_panel;
    protected boolean first = true;
    protected float timeBuffer = 0;
    protected Button backToTitle,retry;
    public float alpha = 0;
    public GameOverPanel(TimePanel panel){
        t_panel = panel;
        renderer = new UiRenderer();
        renderer.setShader((UiShader) Constant.getShader(Constant.SHADER.ui));

        gameover = new StaticText("GAME OVER");
        gameover.setR(0);
        gameover.setG(0);
        gameover.setB(0);
        gameover.useAspect(true);
        gameover.setHeight(0.3f);
        gameover.setHorizontal(UIAlign.Align.CENTOR);
        gameover.setVertical(UIAlign.Align.CENTOR);
        gameover.setX(GLES20Util.getWidth_gl()/2f);
        gameover.setY(GLES20Util.getHeight_gl() - 0.2f);
        renderer.addItem(gameover);

        scoreText = new StaticText("SCORE");
        scoreText.setR(0);
        scoreText.setG(0);
        scoreText.setB(0);
        scoreText.useAspect(true);
        scoreText.setHeight(0.1f);
        scoreText.setHorizontal(UIAlign.Align.CENTOR);
        scoreText.setVertical(UIAlign.Align.CENTOR);
        scoreText.setX(GLES20Util.getWidth_gl()/2f);
        scoreText.setY(GLES20Util.getHeight_gl() - 0.35f);
        renderer.addItem(scoreText);

        timeText = new StaticText("TIME");
        timeText.useAspect(true);
        timeText.setHeight(0.1f);
        timeText.setHorizontal(UIAlign.Align.CENTOR);
        timeText.setVertical(UIAlign.Align.CENTOR);
        timeText.setX(GLES20Util.getWidth_gl()/2f);
        timeText.setY(GLES20Util.getHeight_gl() - 0.63f);
        timeText.setAlpha(0);
        timeText.setR(0);
        timeText.setG(0);
        timeText.setB(0);
        renderer.addItem(timeText);

        score = new NumberText("メイリオ");
        score.setHorizontal(UIAlign.Align.CENTOR);
        score.setVertical(UIAlign.Align.TOP);
        score.setHeight(0.2f);
        score.setX(GLES20Util.getWidth_gl()/2f);
        score.setY(GLES20Util.getHeight_gl() - 0.4f);
        score.setR(0);
        score.setG(0);
        score.setB(0);
        score.setNumber(100);
        renderer.addItem(score);

        backToTitle = new Button(0,0,1,1,"TITLE");
        backToTitle.setBitmap(Constant.getBitmap(Constant.BITMAP.system_button));
        backToTitle.useAspect(true);
        backToTitle.setCriteria(Button.CRITERIA.HEIGHT);
        backToTitle.setHorizontal(UIAlign.Align.LEFT);
        backToTitle.setVertical(UIAlign.Align.BOTTOM);
        backToTitle.setWidth(0.2f);
        backToTitle.setX(0);
        backToTitle.setY(0);
        backToTitle.setAlpha(0);

        renderer.addItem(backToTitle);
    }

    @Override
    public void proc() {
        if(first && enabled){
            first = false;
            t_panel.changeResult();
        }

        if(timeBuffer >= 0.4f) {
            float clamp = Clamp.clamp(0,1,(timeBuffer-0.4f)/0.2f);
            t_panel.setEnabled(true);
            timeText.setY(GLES20Util.getHeight_gl() - 0.63f - 0.05f*(1f - clamp));
            timeText.setAlpha(clamp);
            t_panel.proc();
        }else{
            t_panel.setEnabled(false);
        }

        float clamp = Clamp.clamp(0,1,timeBuffer/0.2f);
        gameover.setY(GLES20Util.getHeight_gl() - 0.2f - 0.05f*(1f-clamp));
        gameover.setAlpha(clamp);

        clamp = Clamp.clamp(0,1,(timeBuffer-0.2f)/0.2f);
        scoreText.setY(GLES20Util.getHeight_gl() - 0.35f - 0.05f*(1f - clamp));
        scoreText.setAlpha(clamp);

        score.setY(GLES20Util.getHeight_gl() - 0.4f - 0.05f*(1f - clamp));
        score.setAlpha(clamp);

        clamp = Clamp.clamp(0,1,(timeBuffer-0.6f)/0.2f);
        backToTitle.setAlpha(clamp);
        //hour.setAlpha(alpha);
        //minute.setAlpha(alpha);
        //second.setAlpha(alpha);

        timeBuffer += Time.getDeltaTime();
    }

    @Override
    public boolean touch(boolean flag, Touch touch) {
        return flag;
    }

    @Override
    public void draw() {
        if(enabled)
            renderer.drawAll();
    }

    @Override
    public UiRenderer getUiRenderer() {
        return renderer;
    }
}
