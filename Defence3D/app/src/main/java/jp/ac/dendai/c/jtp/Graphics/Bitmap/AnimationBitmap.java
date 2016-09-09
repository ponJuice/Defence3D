package jp.ac.dendai.c.jtp.Graphics.Bitmap;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/09/09.
 */
public class AnimationBitmap {
    Bitmap[] animation;
    public AnimationBitmap(Bitmap[] anim){
        animation = anim;
    }

    public static AnimationBitmap createAnimation(int res_id,int max_x,int max_y,int count_x,int count_y){
        Bitmap[] temp = new Bitmap[count_x * count_y];
        int deltaX = max_x / count_x;
        int deltaY = max_y / count_y;
        int startX = 0;
        int startY = 0;
        for(int n = 0;n < count_y;n++){
            startY = deltaY * n;
            startX = 0;
            for(int m = 0;m < count_x;m++){
                int index = count_x * n + m;
                temp[index] = GLES20Util.loadBitmap(startX,startY,startX + deltaX,startY + deltaY,res_id);
                startX += deltaX;
            }
        }
        return new AnimationBitmap(temp);
    }
}
