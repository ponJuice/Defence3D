package jp.ac.dendai.c.jtp.Graphics.UI.Panel;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Game.ScoreManager;
import jp.ac.dendai.c.jtp.Game.Screen.StageSelectScreen;
import jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition.LoadingTransition;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Renderer.UiRenderer;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.Button;
import jp.ac.dendai.c.jtp.Graphics.UI.Button.ButtonListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.Slider;
import jp.ac.dendai.c.jtp.Graphics.UI.Slider.SliderChangeValueListener;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.DynamicNumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Graphics.UI.UIObserver;
import jp.ac.dendai.c.jtp.SlopeUtil.SlopeUtil;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.defence3d.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/28.
 */

public class UIPanel {
    protected UiRenderer uiRenderer;
    private Button button,attackButton,leftButton,rightButton,rotateResetButton;
    private Slider angle;
    private NumberText speedText;
    private DynamicNumberText scoreText;
    private StaticText attack;
    private Bitmap buttonImage;
    private Player player;
    private Camera mainCamera;
    private UIObserver uiObserver;

    public UIPanel(Player p,Camera camera){
        this.player = p;
        this.mainCamera = camera;

        uiRenderer = new UiRenderer();
        uiRenderer.setShader((UiShader) Constant.getShader(Constant.SHADER.ui));

        buttonImage = GLES20Util.loadBitmap(R.mipmap.button);

        uiObserver = new UIObserver();

        button = new Button(0,0,0.3f,0.1f,"Back");
        button.setCriteria(Button.CRITERIA.HEIGHT);
        button.setHorizontal(UIAlign.Align.LEFT);
        button.setVertical(UIAlign.Align.TOP);
        button.setWidth(0.2f);
        button.setX(0);
        button.setY(GLES20Util.getHeight_gl());
        button.setBitmap(buttonImage);
        button.useAspect(true);
        button.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                LoadingTransition lt = LoadingTransition.getInstance();
                lt.initTransition(StageSelectScreen.class);
                GameManager.transition = lt;
                GameManager.isTransition = true;
            }
        });
        button.setTouchThrough(false);

        leftButton = new Button(0,0,0.1f,0.1f,"<");
        leftButton.setBitmap(buttonImage);
        leftButton.setCriteria(Button.CRITERIA.HEIGHT);
        leftButton.setHorizontal(UIAlign.Align.RIGHT);
        leftButton.setVertical(UIAlign.Align.BOTTOM);
        leftButton.setX(GLES20Util.getWidth_gl() - 0.205f);
        leftButton.setY(0);
        leftButton.setWidth(0.2f);
        leftButton.useAspect(true);
        leftButton.setTouchThrough(false);
        leftButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {
                player.getPos().setX(player.getPos().getX() + 0.1f);
            }

            @Override
            public void touchUp(Button button) {
            }
        });

        rightButton = new Button(0,0,0.1f,0.1f,">");
        rightButton.setBitmap(buttonImage);
        rightButton.setCriteria(Button.CRITERIA.HEIGHT);
        rightButton.setHorizontal(UIAlign.Align.RIGHT);
        rightButton.setVertical(UIAlign.Align.BOTTOM);
        rightButton.setX(GLES20Util.getWidth_gl());
        rightButton.setY(0);
        rightButton.setWidth(0.2f);
        rightButton.useAspect(true);
        rightButton.setTouchThrough(false);
        rightButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {
                player.getPos().setX(player.getPos().getX() - 0.1f);
            }

            @Override
            public void touchUp(Button button) {

            }
        });

        rotateResetButton = new Button(0,0,0.1f,0.1f,"ROT");
        rotateResetButton.setBitmap(buttonImage);
        rotateResetButton.setCriteria(Button.CRITERIA.HEIGHT);
        rotateResetButton.setHorizontal(UIAlign.Align.LEFT);
        rotateResetButton.setVertical(UIAlign.Align.BOTTOM);
        rotateResetButton.setX(0);
        rotateResetButton.setY(0);
        rotateResetButton.setWidth(0.2f);
        rotateResetButton.useAspect(true);
        rotateResetButton.setTouchThrough(false);
        rotateResetButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                SlopeUtil.correct();
            }
        });


        angle = new Slider(0,0,0.025f,0.5f,0.1f,0.05f, Slider.SLIDER_ORIENT.portrait);
        angle.setHorizontal(UIAlign.Align.RIGHT);
        angle.setVertical(UIAlign.Align.TOP);
        angle.setX(GLES20Util.getWidth_gl());
        angle.setY(GLES20Util.getHeight_gl());
        angle.setMin(5);
        angle.setMax(50);
        angle.setValue(40);
        angle.setChangeListener(new SliderChangeValueListener() {
            @Override
            public void changeValue(float value) {
                value = 50f / value * 5f;
                mainCamera.setAngleOfView(value);
            }
        });
        angle.setTouchThrough(false);

        attackButton = new Button(0,0,0.3f,0.1f,"Attack");
        attackButton.useAspect(true);
        attackButton.setCriteria(Button.CRITERIA.HEIGHT);
        attackButton.setHorizontal(UIAlign.Align.LEFT);
        attackButton.setVertical(UIAlign.Align.BOTTOM);
        attackButton.setX(0);
        attackButton.setY(0.1f);
        attackButton.setBitmap(buttonImage);
        attackButton.setButtonListener(new ButtonListener() {
            @Override
            public void touchDown(Button button) {

            }

            @Override
            public void touchHover(Button button) {

            }

            @Override
            public void touchUp(Button button) {
                player.attack();
            }
        });
        attackButton.setTouchThrough(false);

        speedText = new NumberText("メイリオ");
        speedText.setHeight(0.15f);
        speedText.setX(0.1f);
        speedText.setVertical(UIAlign.Align.CENTOR);
        speedText.setHorizontal(UIAlign.Align.LEFT);
        speedText.setY(GLES20Util.getHeight_gl()/2f);

        scoreText = new DynamicNumberText("メイリオ",1);
        scoreText.setHeight(0.15f);
        scoreText.setVertical(UIAlign.Align.TOP);
        scoreText.setHorizontal(UIAlign.Align.CENTOR);
        scoreText.setY(GLES20Util.getHeight_gl());
        scoreText.setX(GLES20Util.getWidth_gl()/2f);
        scoreText.setNumber(0);


        attack = new StaticText("Inveder Damaged!!");
        attack.useAspect(true);
        attack.setHorizontal(UIAlign.Align.CENTOR);
        attack.setVertical(UIAlign.Align.TOP);
        attack.setHeight(0.1f);
        attack.setX(GLES20Util.getWidth_gl()/2f);
        attack.setY(GLES20Util.getHeight_gl());
        attack.setAlpha(0);


        uiRenderer.addItem(button);
        uiRenderer.addItem(attackButton);
        uiRenderer.addItem(angle);
        uiRenderer.addItem(leftButton);
        uiRenderer.addItem(rightButton);
        uiRenderer.addItem(speedText);
        uiRenderer.addItem(attack);
        uiRenderer.addItem(scoreText);
        uiRenderer.addItem(rotateResetButton);

        uiObserver.addItem(button);
        uiObserver.addItem(attackButton);
        uiObserver.addItem(angle);
        uiObserver.addItem(leftButton);
        uiObserver.addItem(rightButton);
        uiObserver.addItem(speedText);
        uiObserver.addItem(attack);
        uiObserver.addItem(scoreText);
        uiObserver.addItem(rotateResetButton);
    }
    public void proc() {
        scoreText.setNumber(ScoreManager.getScore());
        scoreText.proc();
        button.proc();
        attackButton.proc();
        uiObserver.proc();

        if (attack.getAlpha() > 0) {
            attack.setAlpha(attack.getAlpha() - 0.01f);
        }
    }
    public boolean touch(boolean flag,Touch touch){
        flag = flag && uiObserver.touch(touch,flag);
        return flag;
    }
    public void draw(){
        uiRenderer.drawAll();
    }

    public UiRenderer getUiRenderer(){
        return uiRenderer;
    }
}
