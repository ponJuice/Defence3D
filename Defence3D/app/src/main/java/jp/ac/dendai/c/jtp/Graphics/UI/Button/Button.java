package jp.ac.dendai.c.jtp.Graphics.UI.Button;

import android.util.Log;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.Graphics.UI.Image.Image;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StaticText;
import jp.ac.dendai.c.jtp.Graphics.UI.UIAlign;
import jp.ac.dendai.c.jtp.Graphics.UI.Util.Figure.Rect;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by Goto on 2016/09/06.
 */
public class Button extends Image {
    private enum BUTTON_STATE{
        NON,
        DOWN,
        HOVER,
        UP,
    }
    //テキストの大きさをボタンのどの方向を基準にするか
    public enum CRITERIA{
        WIDTH,
        HEIGHT,
        NON
    }
    protected CRITERIA criteria = CRITERIA.NON;
    protected BUTTON_STATE state = BUTTON_STATE.NON;
    protected ButtonListener listener;
    protected Touch touch;
    protected StaticText text;
    protected float hover_alpha = 0.5f;
    protected float non_hover_alpha = 1f;
    protected float padding = 0;
    protected Rect rect;
    public Button(float cx,float cy,float width,float height,String string){
        useAspect(false);
        rect = new Rect(cx-width/2f,cy+height/2f,cx+width/2f,cy-height/2f);
        this.x = cx;
        this.y = cy;
        this.width = rect.getWidth();
        this.height = rect.getHeight();
        if(string != null) {
            this.text = new StaticText(string);
            this.text.setVertical(UIAlign.Align.CENTOR);
            this.text.setHorizontal(UIAlign.Align.CENTOR);
            updateTextPos();
        }
        setX(cx);
        setY(cy);
        setBitmap(Constant.getBitmap(Constant.BITMAP.white));
    }
    public void setButtonListener(ButtonListener listener){
        this.listener = listener;
    }
    private void updateTextPos(){
        if(text != null){
            text.setX(rect.getCx());
            text.setY(rect.getCy());
            if(criteria == CRITERIA.WIDTH)
                text.setWidth(rect.getWidth() - padding);
            else if(criteria == CRITERIA.HEIGHT)
                text.setHeight(rect.getHeight() - padding);
        }
    }

    public void setCriteria(CRITERIA c){
        this.criteria = c;
        if(criteria == CRITERIA.NON) {
            text.useAspect(false);
        }else{
            text.useAspect(true);
        }
        updateTextPos();
    }

    public void setPadding(float n){
        padding = n;
        updateTextPos();
    }

    @Override
    public void setHorizontal(UIAlign.Align align){
        horizontal = align;
        setX(getX());
    }

    @Override
    public void setVertical(UIAlign.Align align){
        vertical = align;
        setY(getY());
    }

    @Override
    public void setX(float x){
        this.x = x;
        x = x + UIAlign.convertAlign(rect.getWidth(),horizontal);
        rect.setCx(x);
        //this.x = rect.getCx();
        if(text != null)
            text.setX(x);
    }

    @Override
    public void setY(float y){
        this.y = y;
        y = y + UIAlign.convertAlign(rect.getHeight(),vertical);
        rect.setCy(y);
        //this.y = rect.getCy();
        if(text != null)
            text.setY(y);
    }

    @Override
    public void setWidth(float width){
        //rect.setCx((width - rect.getWidth())/2f);
        rect.setWidth(width);
        if(useAspect){
            rect.setCy((width/aspect - rect.getHeight()/2f));
            rect.setHeight(width/aspect);
        }
        if(text != null) {
            text.setX(rect.getCx());
            text.setY(rect.getCy());
            if(criteria == CRITERIA.WIDTH){
                text.setWidth(width - padding);
            }else if(criteria == CRITERIA.HEIGHT){
                text.setHeight(rect.getHeight()-padding);
            }
        }
        setX(x);
        setY(y);
    }
    @Override
    public void setHeight(float height){
        //rect.setCy((height - rect.getHeight())/2f);
        rect.setHeight(height);
        if(useAspect){
            rect.setCy((height*aspect - rect.getWidth()/2f));
            rect.setWidth(height*aspect);
        }
        if(text != null) {
            text.setY(rect.getCy());
            text.setX(rect.getCx());
            if(criteria == CRITERIA.HEIGHT){
                text.setHeight(height - padding);
            }else if(criteria == CRITERIA.WIDTH){
                text.setWidth(rect.getWidth() - padding);
            }
        }
        setX(x);
        setY(y);
    }

    @Override
    public boolean touch(Touch touch) {
        if(this.touch != null && this.touch != touch)
            return true;
        float x = Constant.getActiveUiCamera().convertTouchPosToGLPosX(touch.getPosition(Touch.Pos_Flag.X));
        float y = Constant.getActiveUiCamera().convertTouchPosToGLPosY(touch.getPosition(Touch.Pos_Flag.Y));
        if(touch.getTouchID() == -1){
            //指が離された
            if(state != BUTTON_STATE.NON && rect.contains(x,y)){
                state = BUTTON_STATE.UP;
            }else{
                this.touch = null;
            }
            return true;
        }
        //Log.d("button touch pos", "device pos:" + "(" + touch.getPosition(Touch.Pos_Flag.X) + "," + touch.getPosition(Touch.Pos_Flag.Y) + ")" + "camera pos:(" + x + "," + y + ") delta:"+touch.getDelta(Touch.Pos_Flag.X)+","+touch.getDelta(Touch.Pos_Flag.Y)+")");
        if(touch.getTouchID() != -1 && rect.contains(x,y)){
            if(state == BUTTON_STATE.NON) {
                state = BUTTON_STATE.DOWN;
                this.touch = touch;
            }
            else if(state == BUTTON_STATE.DOWN)
                state = BUTTON_STATE.HOVER;
        }else{
            if(state == BUTTON_STATE.DOWN || state == BUTTON_STATE.HOVER) {
                state = BUTTON_STATE.NON;
                this.touch = null;
            }
            else if(state == BUTTON_STATE.UP) {
                state = BUTTON_STATE.NON;
                this.touch = touch;
            }
        }
        if(state == BUTTON_STATE.NON)
            return true;
        else if(touch.getTouchID() == -1)
            return true;
        else
            return through;
    }

    @Override
    public void proc() {
        if(state == BUTTON_STATE.UP){
            if(listener != null)
                listener.touchUp(this);
            state = BUTTON_STATE.NON;
        }else if(state == BUTTON_STATE.DOWN){
            if(listener != null)
                listener.touchDown(this);
            state = BUTTON_STATE.HOVER;
        }else if(state == BUTTON_STATE.HOVER){
            if(listener != null)
                listener.touchHover(this);
        }
    }

    @Override
    public void draw(UiShader shader) {
        if (state == BUTTON_STATE.NON) {
            alpha = non_hover_alpha;
            shader.drawUi(this
                    ,rect.getCx()
                    ,rect.getCy()
                    ,rect.getWidth()
                    ,rect.getHeight()
                    ,0,non_hover_alpha);
        } else {
            alpha = hover_alpha;
            shader.drawUi(this
                    , rect.getCx()
                    , rect.getCy()
                    , rect.getWidth()
                    , rect.getHeight()
                    , 0, hover_alpha);
        }
        if(text != null)
            text.draw(shader);
    }
}
