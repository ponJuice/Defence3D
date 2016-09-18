package jp.ac.dendai.c.jtp.Game.Transition.LoadingTransition;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Screen.Screenable;

/**
 * Created by テツヤ on 2016/09/15.
 */
public class LoadingThread extends Thread{
    protected Class<?> nextScreenClass;
    protected Screenable nextScreenInstance;
    protected Object lock;
    protected boolean isEnd = false;
    public LoadingThread(Object lock){
        this.lock = lock;
    }
    public void initThread(Class<?> nextScreenClass){
        synchronized (lock) {
            this.nextScreenClass = nextScreenClass;
        }
    }
    public boolean isEnd(){
        synchronized (lock) {
            return isEnd;
        }
    }
    public Screenable getScreen(){
        return nextScreenInstance;
    }
    @Override
    public void run() {
        try {
            nextScreenInstance = (Screenable)nextScreenClass.newInstance();
        } catch (InstantiationException e) {
            Log.d("LoadingThread",nextScreenClass.getName()+"のインスタンス化に失敗しました。");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("LoadingThread",nextScreenClass.getName()+"のインスタンス化に失敗しました。");
            e.printStackTrace();
        }finally {
            synchronized (lock) {
                nextScreenInstance.freeze();
                isEnd = true;
            }
        }
    }
}
