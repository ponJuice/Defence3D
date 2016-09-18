package jp.ac.dendai.c.jtp.Game;


import jp.ac.dendai.c.jtp.Game.Screen.Screenable;
import jp.ac.dendai.c.jtp.Game.Transition.Transitionable;
import jp.ac.dendai.c.jtp.Graphics.Camera.Camera;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;


public class GameManager {
	public static UiShader uiShader;
	public static boolean debug = false;
	public static Screenable nowScreen;
	public static Screenable nextScreen;
	public static boolean isTransition = false;
	public static Transitionable transition;

	public static void init(){
		uiShader = new UiShader();
		uiShader.setCamera(new Camera(Camera.CAMERA_MODE.ORTHO,0,0,-5,0,0,0));
		Constant.init();
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
	public static void touch(){
		if(!isTransition){
			nowScreen.Touch();
		}
	}

}
