package cn.chenhuanming.octopus.util;

/**
 * @author guangdao
 * Created at 2019-05-06
 */
public class ValidationUtils {
    public static void notEmpty(String s, String name) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException(String.format("%s can not be empty", name));
        }
    }

}
