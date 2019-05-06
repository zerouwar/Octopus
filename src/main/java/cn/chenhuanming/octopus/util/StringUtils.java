package cn.chenhuanming.octopus.util;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class StringUtils {
    public static final String OPTION_SPLITTER_VERTICAL = "\\|";
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String defaultIfEmpty(String s, String defaultValue) {
        return isEmpty(s) ? defaultValue : s;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
}
