package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import android.media.SoundPool;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Score.ScoreManager;
import jp.ac.dendai.c.jtp.Game.Score.ScorePacage;
import jp.ac.dendai.c.jtp.Game.Screen.StartScreen;
import jp.ac.dendai.c.jtp.Game.Screen.TestGameScreen;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Graphics.UI.UIObserver;
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
    protected UIObserver uiObserver;
    protected TimePanel t_panel;
    protected boolean first = true;
    protected float timeBuffer = 0;
    protected Button backToTitle,retry;
    public float alpha = 0;
    protected SoundPool sp;
    protected int button_sound;
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
        backToTitle.setWidth(0.4f);
        backToTitle.setX(0);
        backToTitle.setY(0);
        backToTitle.setAlpha(0);
        backToTitle.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                sp.play(button_sound,1,1,0,0,1);
                ScoreManager.saveScore();
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(StartScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });

        retry = new Button(0,0,1,1,"RETRY");
        retry.setBitmap(Constant.getBitmap(Constant.BITMAP.system_button));
        retry.useAspect(true);
        retry.setCriteria(Button.CRITERIA.HEIGHT);
        retry.setHorizontal(UIAlign.Align.RIGHT);
        retry.setVertical(UIAlign.Align.BOTTOM);
        retry.setWidth(0.4f);
        retry.setX(GLES20Util.getWidth_gl());
        retry.setY(0);
        retry.setAlpha(0);
        retry.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                sp.play(button_sound,1,1,0,0,1);
                ScoreManager.saveScore();
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(TestGameScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });

        uiObserver = new UIObserver();
        uiObserver.addItem(backToTitle);
        uiObserver.addItem(retry);

        renderer.addItem(retry);
        renderer.addItem(backToTitle);
    }

    public void setSoundPool(SoundPool sp,int button){
        this.sp = sp;
        button_sound = button;
    }

    @Override
    public void proc() {
        if(first && enabled){
            first = false;
            score.setNumber(ScoreManager.getScore());
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
        retry.setAlpha(clamp);

        if(timeBuffer >= 0.6f){
            uiObserver.proc();
        }

        timeBuffer += Time.getDeltaTime();
    }

    @Override
    public boolean touch(boolean flag, Touch touch) {
        if(timeBuffer >= 0.6f)
            uiObserver.touch(touch,flag);
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