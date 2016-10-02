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
	public static float r = 0,g = 0,b = 0,a = 0;
	public static boolean debug = false;
	public static Screenable nowScreen;
	public static Screenable nextScreen;
	public static Screenable debugScreen;
	public static boolean isTransition = false;
	public static Transitionable transition;
	protected static Activity act;
	protected static boolean clearColor = true;

	public static void init(Activity _act){
		act = _act;
		GLES20Util.initGLES20Util();
		Constant.init();

	}

	public static void setClearColor(COLOR col,float value){
		if(col == R && r != value) {
			r = value;
			clearColor = true;
		}
		else if(col == G && g != value) {
			g = value;
			clearColor = true;
		}
		else if(col == B && b != value) {
			b = value;
			clearColor = true;
		}
		else if(col == A && a !=  value) {
			a = value;
			clearColor = true;
		}
	}
	public static Activity getAct(){
		return act;
	}

	public static void onPause(){
		if(nowScreen != null)
			nowScreen.onPause();
	}

	public static void onResume(){
		if(nowScreen != null)
			nowScreen.onResume();
	}

	public static void draw(){
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
