package cn.chenhuanming.octopus.util;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
}
