package jp.ac.dendai.c.jtp.openglesutil.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.res.AssetManager;
/**
 * ファイル読み込み用クラス
 *　作り掛け
 * @author
 *
 */
public class FileManager {
    public static Activity _act;
    public static void initFileManager(Activity act){
        _act = act;
    }
    public static String readTextFile(String fileName){
        return readShaderFile(fileName);
    }
	/**
	 * assetsフォルダに入っているシェーダファイルを読み込みます
	 * 正直シェーダファイルでなくても読み込める
	 * 読み込み文字コードはUTF-8
	 * @return　成功:読み込んだ文字列_失敗:”読み込み失敗”
	 */
	public static String readShaderFile(String fileName){
        if(_act == null)
            throw new RuntimeException("FileManagerが初期化されていません");
		//ファイル読み込みなど操作するときはtry{}catch{}で囲む
		try{
            //元からあるassetsフォルダに入れたtextファイルを読み込む方法
            AssetManager assets = _act.getResources().getAssets();
            InputStream in = assets.open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str;
            StringBuilder strs = new StringBuilder();
            while ((str = br.readLine()) != null) {
                strs.append(str);
                strs.append('\n');
            }
            br.close();
            return strs.toString();
        }catch (Exception e){
            return "読み込み失敗";
        }
	}

    public static void writeTextFile(String fileName,String data,int mode){
        OutputStream out;
        try{
            out = _act.openFileOutput(fileName,mode);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

            writer.append(data);
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
