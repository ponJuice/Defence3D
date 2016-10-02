package jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Transition.Transitionable;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UI;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Math.Clamp;
import jp.ac.dendai.c.jtp.Time;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class LoadingTransition implements Transitionable {
	private enum LOAD_STATE{
		NON,
		START,
		LOAD_START,
		LOAD_STAY,
		END
	}
	private LOAD_STATE state = LOAD_STATE.NON;
	private static LoadingTransition instance;
	private Class<?> nextScreenClass;
	private Image image;
	private StaticText loading;
	private int r = 0,g = 255, b = 0;
	private LoadingThread thread;
	private Object lock;
	private float timeBuffer = 0;
	public static LoadingTransition getInstance(){
		if(instance == null)
			instance = new LoadingTransition();
		return instance;
	}
	private LoadingTransition(){
		lock = new Object();
		loading = new StaticText("Loading...");
		loading.setMaskBitmap(Constant.getBitmap(Constant.BITMAP.bilinear));
		loading.setWidth(0.5f);
		loading.setHorizontal(UIAlign.Align.RIGHT);
		loading.setVertical(UIAlign.Align.BOTTOM);
		loading.setX(GLES20Util.getWidth_gl());
		loading.setY(0);
		loading.setDelta_u(0.05f);
		image = new Image(GLES20Util.createBitmap(r,g,b,255));
		image.setHeight(GLES20Util.getHeight_gl());
		image.setWidth(GLES20Util.getWidth_gl());
		image.setHorizontal(UIAlign.Align.LEFT);
		image.setVertical(UIAlign.Align.BOTTOM);
		image.setX(0);
		image.setY(0);
	}
	public void initTransition(Class<?> nextScreenClass){
		thread = new LoadingThread(lock);
		thread.initThread(nextScreenClass);
		timeBuffer = 0;
		loading.init();
		state = LOAD_STATE.START;
	}
	@Override
	public boolean Transition() {
		loading.proc();
		if(state == LOAD_STATE.START){
			GameManager.nowScreen.freeze();
			GameManager.nowScreen.Draw(0,0);
			Constant.getLoadingShader().useShader();
			Constant.getLoadingShader().updateCamera();
			//ロードの開始
			//ロード画面のトランジョン
			float a = Clamp.clamp(0f, 1f, 1f, timeBuffer);
			//Constant.getLoadingShader().useShader();
			image.setAlpha(a);
			image.draw(Constant.getLoadingShader());
			loading.setAlpha(a);
			loading.draw(Constant.getLoadingShader());
			if(timeBuffer >= 1) {
				state = LOAD_STATE.LOAD_START;
				timeBuffer = 0;
			}
		}else if(state == LOAD_STATE.LOAD_START){
			Constant.getLoadingShader().useShader();
			Constant.getLoadingShader().updateCamera();
			image.setAlpha(1);
			image.draw(Constant.getLoadingShader());
			loading.setAlpha(1);
			loading.draw(Constant.getLoadingShader());
			thread.start();
			GameManager.nowScreen.death();
			state = LOAD_STATE.LOAD_STAY;
		}else if(state == LOAD_STATE.LOAD_STAY){
			Constant.getLoadingShader().useShader();
			Constant.getLoadingShader().updateCamera();
			image.setAlpha(1);
			image.draw(Constant.getLoadingShader());
			loading.setAlpha(1);
			loading.draw(Constant.getLoadingShader());
			if(timeBuffer > 2f) {
				//ロード中
				if (thread.isEnd()) {
					GameManager.nowScreen = thread.getScreen();
					GameManager.nowScreen.init();
					GameManager.nowScreen.freeze();
					thread = null;
					state = LOAD_STATE.END;
					timeBuffer = 0;
				}
			}
		}else if(state == LOAD_STATE.END){
			GameManager.nowScreen.Draw(0, 0);
			Constant.getLoadingShader().useShader();
			Constant.getLoadingShader().updateCamera();
			float a = Clamp.clamp(1f, 0f, 1f, timeBuffer);
			image.setAlpha(a);
			image.draw(Constant.getLoadingShader());
			loading.setAlpha(a);
			loading.draw(Constant.getLoadingShader());
			if(timeBuffer >= 1f) {
				//終了処理
				state = LOAD_STATE.NON;
				GameManager.nowScreen.unFreeze();
				timeBuffer = 0;
				return false;
			}
		}
		timeBuffer += Time.getDeltaTime();
		return true;
	}

}
