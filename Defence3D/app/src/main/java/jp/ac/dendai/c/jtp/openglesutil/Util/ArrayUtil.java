package jp.ac.dendai.c.jtp.openglesutil.Util;

/**
 * Created by Goto on 2016/09/14.
 */
public class ArrayUtil {
    public static Character[] toObjects( String s ) {

        if ( s == null ) {
            return null;
        }

        int len = s.length();
        Character[] array = new Character[len];
        for (int i = 0; i < len ; i++) {
            array[i] = new Character(s.charAt(i));
        }

        return array;
    }
}
