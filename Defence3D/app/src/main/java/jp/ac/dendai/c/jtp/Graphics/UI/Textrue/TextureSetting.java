package jp.ac.dendai.c.jtp.Graphics.UI.Textrue;

import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Graphics.Model.Texture;

/**
 * Created by Goto on 2016/09/20.
 */
public class TextureSetting {
    protected int wrap_s = GLES20.GL_REPEAT
            ,wrap_t = GLES20.GL_REPEAT
            ,filter_min = GLES20.GL_NEAREST
            ,filter_mag = GLES20.GL_NEAREST
            ,mask_warp_s = GLES20.GL_CLAMP_TO_EDGE
            ,mask_warp_t = GLES20.GL_CLAMP_TO_EDGE
            ,mask_filter_min = GLES20.GL_NEAREST
            ,mask_filter_mag = GLES20.GL_NEAREST;
    public TextureSetting(){}
    public TextureSetting(TextureSetting ts){
        this.copy(ts);
    }
    public void copy(TextureSetting ts){
        wrap_s = ts.wrap_s;
        wrap_t = ts.wrap_t;
        filter_mag = ts.filter_mag;
        filter_min = ts.filter_min;
        mask_warp_s = ts.mask_warp_s;
        mask_warp_t = ts.mask_warp_t;
        mask_filter_min = ts.mask_filter_min;
        mask_filter_mag = ts.mask_filter_mag;
    }
    public int getWrap_s() {
        return wrap_s;
    }

    public void setWrap_s(int wrap_s) {
        this.wrap_s = wrap_s;
    }

    public int getWrap_t() {
        return wrap_t;
    }

    public void setWrap_t(int wrap_t) {
        this.wrap_t = wrap_t;
    }

    public int getFilter_min() {
        return filter_min;
    }

    public void setFilter_min(int filter_min) {
        this.filter_min = filter_min;
    }

    public int getFilter_mag() {
        return filter_mag;
    }

    public void setFilter_mag(int filter_mag) {
        this.filter_mag = filter_mag;
    }

    public int getMask_warp_s() {
        return mask_warp_s;
    }

    public void setMask_warp_s(int mask_warp_s) {
        this.mask_warp_s = mask_warp_s;
    }

    public int getMask_warp_t() {
        return mask_warp_t;
    }

    public void setMask_warp_t(int mask_warp_t) {
        this.mask_warp_t = mask_warp_t;
    }

    public int getMask_filter_min() {
        return mask_filter_min;
    }

    public void setMask_filter_min(int mask_filter_min) {
        this.mask_filter_min = mask_filter_min;
    }

    public int getMask_filter_mag() {
        return mask_filter_mag;
    }

    public void setMask_filter_mag(int mask_filter_mag) {
        this.mask_filter_mag = mask_filter_mag;
    }
}
