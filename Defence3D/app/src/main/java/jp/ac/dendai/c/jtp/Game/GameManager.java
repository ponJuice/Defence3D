package jp.ac.dendai.c.jtp.Game;


import android.app.Activity;

import jp.ac.dendai.c.jtp.Game.Screen.Screenable;
import jp.ac.dendai.c.jtp.Game.Transition.Transitionable;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;


public class GameManager {
	public static boolean debug = false;
	public static Screenable nowScreen;
	public static Screenable nextScreen;
	public static Screenable debugScreen;
	public static boolean isTransition = false;
	public static Transitionable transition;
	protected static Activity act;

	public static void init(Activity _act){
		act = _act;
		GLES20Util.initGLES20Util();
		Constant.init();

	}
	public static Activity getAct(){
		return act;
	}

	public static void draw(){
		if(isTransition && transition != null){
			isTransition = transition.Transition();
		}
		else{
			nowScreen.Draw(0, 0);
		}
		if(debugScreen != null && debug){
			debugScreen.Draw(0,0);
		}
	}
	public static void proc(){
		if(isTransition && nextScreen != null)
			nextScreen.Proc();
		nowScreen.Proc();
		if(debugScreen != null && debug){
			debugScreen.Proc();
		}
	}
	public static void touch(){
		if(!isTransition){
			nowScreen.Touch();
		}
	}

}
