package jp.ac.dendai.c.jtp.Game.Screen;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class StageSelectScreen implements Screenable {
	private boolean isFreeze = false;

	@Override
	public void Proc() {
		if(isFreeze)
			return;

	}

	@Override
	public void Draw(float offsetX, float offsetY) {
		GLES20Util.DrawString("StageSelectScreen", 1, 255, 255, 255, 1f, offsetX, offsetY, GLES20COMPOSITIONMODE.ALPHA);
	}

	@Override
	public void Touch(MotionEvent event) {

	}

	@Override
	public void death() {

	}

	@Override
	public void freeze() {
		isFreeze = true;
	}

	@Override
	public void unFreeze() {
		isFreeze = false;
	}

}
