package jp.ac.dendai.c.jtp.Game.Screen;

import android.view.MotionEvent;

public abstract class Screenable {
	protected boolean freeze = true;
	public abstract void Proc();
	public abstract void Draw(float offsetX, float offsetY);
	public abstract void Touch();
	public abstract void death();
	public void freeze(){freeze = true;};
	public void unFreeze(){freeze = false;};
}
