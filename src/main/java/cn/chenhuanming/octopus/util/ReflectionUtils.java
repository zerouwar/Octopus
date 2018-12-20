package cn.chenhuanming.octopus.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author chenhuanming
 * Created at 2018/12/13
 */
public class ReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * call method and get the return string value,or return defaultValue
     *
     * @param method
     * @param o
     * @param defaultValue
     * @return
     */
    public static String invokeGetter(Method method, Object o, String defaultValue) {
        if (method == null || o == null) {
            return defaultValue;
        }
        String value = defaultValue;
        Object o1 = invokeGetter(method, o);
        if (o1 != null) {
            value = String.valueOf(o1);
        }

        return value;
    }

    /**
     * call getter method and return value,or return null
     *
     * @param method
     * @param o
     * @return
     */
    public static Object invokeGetter(Method method, Object o) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(o);
        } catch (Exception e) {
            LOGGER.debug("failed to invoke " + method.getClass() + "#" + method.getName(), e);
            return null;
        }
    }

    public static Method getterMethod(Class clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getMethod("get" + upperFirstLetter(fieldName));
        } catch (Exception e) {
            LOGGER.debug("failed to fetch getter method for field " + fieldName, e);
            return null;
        }
    }

    private static String upperFirstLetter(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name can not be empty");
        }
        char[] chars = name.toCharArray();
        if (chars[0] >= 'a' && chars[0] < 'z') {
            chars[0] -= 32;
        }
        return new String(chars);
    }
}
