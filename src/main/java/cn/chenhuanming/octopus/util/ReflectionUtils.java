package cn.chenhuanming.octopus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
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
    public static String invokeReadMethod(Method method, Object o, String defaultValue) {
        if (method == null || o == null) {
            return defaultValue;
        }
        String value = defaultValue;
        Object o1 = invokeReadMethod(method, o);
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
     * @return return value fo read method
     * return null if failed or method is null
     */
    public static Object invokeReadMethod(Method method, Object o) {
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

    public static Method readMethod(Class clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return new PropertyDescriptor(fieldName, clazz).getReadMethod();
        } catch (Exception e) {
            LOGGER.debug("failed to fetch getter method for field " + fieldName, e);
            return null;
        }
    }

    public static Method writeMethod(Class clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return new PropertyDescriptor(fieldName, clazz).getWriteMethod();
        } catch (Exception e) {
            LOGGER.debug("failed to fetch setter method for field " + fieldName, e);
            return null;
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("can not new instance through default constructor:" + clazz.getName());
            return null;
        }
    }
}
