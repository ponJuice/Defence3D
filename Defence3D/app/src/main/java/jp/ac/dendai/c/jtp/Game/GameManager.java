package jp.ac.dendai.c.jtp.Game;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.Screen.Screenable;
import jp.ac.dendai.c.jtp.Game.Transition.Transitionable;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;


public class GameManager {
	public static UiShader uiShader;
	public static boolean debug = false;
	public static Screenable nowScreen;
	public static Screenable nextScreen;
	public static boolean isTransition = false;
	public static Transitionable transition;

	public static void init(){
		uiShader = new UiShader();
	}

	public static void draw(){
		if(isTransition && transition != null){
			isTransition = transition.Transition();
		}
		else{
			nowScreen.Draw(0, 0);
		}
	}
	public static void proc(){
		if(isTransition && nextScreen != null)
			nextScreen.Proc();
		nowScreen.Proc();
	}
	public static void touch(MotionEvent touch){
		if(!isTransition){
			nowScreen.Touch(touch);
		}
	}

}
