package jp.ac.dendai.c.jtp.openglesutil.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Graphics.Model.Material.Face;
import jp.ac.dendai.c.jtp.Graphics.UI.Text.StringBitmap;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;


public class GLES20Util extends abstractGLES20Util {
	private static Paint paint;
	private static Canvas canvas;
	private static Rect rect = new Rect(0, 0, 0, 0);
	private static float[] line = new float[]{
			0, 0, 0, 0, 0, 1, 0, 0,
			1, 1, 1, 0, 0, 1, 0, 0
	};

	public enum GLES20UTIL_MODE {
		POSX,
		POSY
	}

	public GLES20Util() {
		Log.d("GLES20Util", "Constract");
	}

	public static int getUniformLocation(int program, String name) {
		int temp = GLES20.glGetUniformLocation(program, name);
		if (temp == -1) {
			throw new RuntimeException(name + "の格納場所の取得に失敗");
		}
		return temp;
	}

	public static int getAttributeLocation(int program, String name) {
		int temp = GLES20.glGetAttribLocation(program, name);
		if (temp == -1) {
			throw new RuntimeException(name + "の格納場所の取得に失敗");
		}
		return temp;
	}

	public static float screenToInnerPosition(float value, GLES20UTIL_MODE mode) {
		if (value == 0)
			return 0;
		if (mode == GLES20UTIL_MODE.POSX) {
			return GLES20Util.getWidth_gl() / GLES20Util.getWidth() * value;
		} else if (mode == GLES20UTIL_MODE.POSY) {
			return GLES20Util.getHeight_gl() / GLES20Util.getHight() * (GLES20Util.getHight() - value);
		}
		return 0;
	}

	public static Bitmap stringToBitmap(String text, String fontName, float size, int r, int g, int b) {
		String[] line = text.split("\n");

		//描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size);
		try {
			if(fontName != null) {
				Typeface type = Typeface.createFromAsset(GameManager.getAct().getAssets(), fontName);
				paint.setTypeface(type);
			}

		}catch(Exception e){
			Log.d("stringToBitmap","フォントファイル["+fontName+"]が見つかりませんでした");
		}
		int textWidth = 0;
		int textHeight = 0;
		for (int n = 0; n < line.length; n++) {
			paint.getTextBounds(line[n], 0, line[n].length(), new Rect());
			FontMetrics fm = paint.getFontMetrics();
			//テキストの表示範囲を設定

			textWidth = Math.max((int) paint.measureText(line[n]), textWidth);
			textHeight += (int) (Math.abs(fm.top) + fm.bottom);
		}
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		for (int n = 0; n < line.length; n++) {
			paint.getTextBounds(line[n], 0, line[n].length(), new Rect());
			//Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(), fontName);
			//paint.setTypeface(type);
			FontMetrics fm = paint.getFontMetrics();
			//キャンバスからビットマップを取得
			canvas.drawText(line[n], 0, Math.abs(fm.top) + textHeight / line.length * n, paint);
		}

		return bitmap;
	}

	public static Bitmap stringToBitmap(String text, float size, int r, int g, int b) {
		String[] line = text.split("\n");

		//描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size);
		int textWidth = 0;
		int textHeight = 0;
		for (int n = 0; n < line.length; n++) {
			paint.getTextBounds(line[n], 0, line[n].length(), new Rect());
			FontMetrics fm = paint.getFontMetrics();
			//テキストの表示範囲を設定

			textWidth = Math.max((int) paint.measureText(line[n]), textWidth);
			textHeight += (int) (Math.abs(fm.top) + fm.bottom);
		}
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		for (int n = 0; n < line.length; n++) {
			paint.getTextBounds(line[n], 0, line[n].length(), new Rect());
			//Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(), fontName);
			//paint.setTypeface(type);
			FontMetrics fm = paint.getFontMetrics();
			//キャンバスからビットマップを取得
			canvas.drawText(line[n], 0, Math.abs(fm.top) + textHeight / line.length * n, paint);
		}

		return bitmap;
	}

	//文字列描画
	public static StringBitmap stringToStringBitmap(String text, String fontName, float size, int r, int g, int b) {
		//描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size);
		paint.getTextBounds(text, 0, text.length(), new Rect());
		//Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(),fontName);
		//paint.setTypeface(type);
		FontMetrics fm = paint.getFontMetrics();
		//テキストの表示範囲を設定

		int textWidth = (int) paint.measureText(text);
		int textHeight = (int) (Math.abs(fm.ascent) + fm.descent);
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);

		//キャンバスからビットマップを取得
		canvas = new Canvas(bitmap);
		//canvas.drawColor(Color.BLUE);
		canvas.drawText(text, 0, Math.abs(fm.ascent), paint);

		return new StringBitmap(bitmap, paint.getFontMetrics(), textWidth);
	}

	//文字列描画
	public static Bitmap stringToBitmap(String text, String fontName, float size, int r, int g, int b, int br, int bg, int bb) {
		//描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size);
		paint.getTextBounds(text, 0, text.length(), new Rect());
		//Typeface type = Typeface.createFromAsset(GameManager.act.getAssets(), fontName);
		//paint.setTypeface(type);
		FontMetrics fm = paint.getFontMetrics();
		//テキストの表示範囲を設定

		int textWidth = (int) paint.measureText(text);
		int textHeight = (int) (Math.abs(fm.top) + fm.bottom);
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);

		//キャンバスからビットマップを取得
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.argb(255, br, bg, bb));
		canvas.drawText(text, 0, Math.abs(fm.top), paint);

		return bitmap;
	}
}