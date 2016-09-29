package jp.ac.dendai.c.jtp.Game;


import android.app.Activity;
import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Game.Screen.Screenable;
import jp.ac.dendai.c.jtp.Game.Transition.Transitionable;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

import static jp.ac.dendai.c.jtp.Game.GameManager.COLOR.A;
import static jp.ac.dendai.c.jtp.Game.GameManager.COLOR.B;
import static jp.ac.dendai.c.jtp.Game.GameManager.COLOR.G;
import static jp.ac.dendai.c.jtp.Game.GameManager.COLOR.R;


public class GameManager {
	public enum COLOR{
		R,G,B,A
	}
	public static boolean debug = false;
	public static Screenable nowScreen;
	public static Screenable nextScreen;
	public static Screenable debugScreen;
	public static boolean isTransition = false;
	public static Transitionable transition;
	public static float r = 0,g = 0,b = 0,a = 0;
	protected static boolean clearColor = true;
	protected static Activity act;

	public static void init(Activity _act){
		act = _act;
		GLES20Util.initGLES20Util();
		Constant.init();

	}

	public static void setClearColor(COLOR col,float value){
		if(col == R)
			r = value;
		else if(col == G)
			g = value;
		else if(col == B)
			b = value;
		else if(col == A)
			a = value;
	}

	public static Activity getAct(){
		return act;
	}

	public static void draw(){
		if(clearColor)
			GLES20.glClearColor(r, g, b, a); // 画面をクリアする色を設定する

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
