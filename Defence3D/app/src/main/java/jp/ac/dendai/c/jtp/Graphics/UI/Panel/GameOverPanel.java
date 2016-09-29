package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Game.ScoreManager;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/30.
 */

public class GameOverPanel extends Panel{
    protected StaticText gameover,scoreText,timeText;
    protected NumberText score;
    protected NumberText hour,minute,second;
    protected UiRenderer renderer;
    public float alpha = 0;
    public GameOverPanel(){
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
        scoreText.setHeight(0.08f);
        scoreText.setHorizontal(UIAlign.Align.RIGHT);
        scoreText.setVertical(UIAlign.Align.CENTOR);
        scoreText.setX(GLES20Util.getWidth_gl()/2f);
        scoreText.setY(GLES20Util.getHeight_gl() - 0.3f);
        renderer.addItem(scoreText);

        timeText = new StaticText("TIME");
        timeText.useAspect(true);
        timeText.setHeight(0.08f);
        timeText.setHorizontal(UIAlign.Align.RIGHT);
        timeText.setVertical(UIAlign.Align.CENTOR);
        timeText.setX(GLES20Util.getWidth_gl()/2f);
        timeText.setY(GLES20Util.getHeight_gl() - 0.4f);
        timeText.setR(0);
        timeText.setG(0);
        timeText.setB(0);
        renderer.addItem(timeText);

        score = new NumberText("メイリオ");
        score.setHorizontal(UIAlign.Align.LEFT);
        score.setVertical(UIAlign.Align.TOP);
        score.setHeight(0.08f);
        score.setX(GLES20Util.getWidth_gl()/2f);
        score.setY(GLES20Util.getHeight_gl() - 0.27f);
        score.setR(0);
        score.setG(0);
        score.setB(0);
        score.setNumber(100);
        renderer.addItem(score);

        hour = new NumberText("メイリオ");
        hour.setHorizontal(UIAlign.Align.LEFT);
        hour.setVertical(UIAlign.Align.CENTOR);
        hour.setX(GLES20Util.getWidth_gl()/2f);
        hour.setY(GLES20Util.getHeight_gl() - 0.4f);
        hour.setR(0);
        hour.setG(0);
        hour.setB(0);
        renderer.addItem(hour);

        minute = new NumberText("メイリオ");
        minute.setHorizontal(UIAlign.Align.LEFT);
        minute.setVertical(UIAlign.Align.CENTOR);
        minute.setX(hour.getX() + hour.getWidth() + 0.05f);
        minute.setY(GLES20Util.getHeight_gl() - 0.3f);
        minute.setR(0);
        minute.setG(0);
        minute.setB(0);
        renderer.addItem(minute);

        second = new NumberText("メイリオ");
        second.setHorizontal(UIAlign.Align.LEFT);
        second.setVertical(UIAlign.Align.CENTOR);
        second.setX(minute.getX() + minute.getWidth() + 0.05f );
        second.setY(GLES20Util.getHeight_gl() - 0.3f);
        second.setR(0);
        second.setG(0);
        second.setB(0);
        renderer.addItem(second);
    }

    @Override
    public void proc() {
        gameover.setAlpha(alpha);
        scoreText.setAlpha(alpha);
        timeText.setAlpha(alpha);
        score.setAlpha(alpha);
        hour.setAlpha(alpha);
        minute.setAlpha(alpha);
        second.setAlpha(alpha);
    }

    @Override
    public boolean touch(boolean flag, Touch touch) {
        return false;
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
