package cn.chenhuanming.octopus.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public class MethodHandleUtil {
    /**
     * 获取Setter方法句柄
     *
     * @param field
     * @return 方法句柄
     */
    public static MethodHandle setterMethodHandle(Field field) {
        StringBuilder setter = new StringBuilder("set");
        String name = field.getName();
        setter.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));

        MethodType mt = MethodType.methodType(void.class, field.getType());
        try {
            return lookup().findVirtual(field.getDeclaringClass(), setter.toString(), mt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("property " + name + " must has setter method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("property " + name + " must has setter method");
    }

    /**
     * 获取Getter方法句柄
     *
     * @param field
     * @return 方法句柄
     */
    public static MethodHandle getterMethodHandle(Field field) {
        StringBuilder getter = new StringBuilder("get");
        String name = field.getName();
        getter.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));

        MethodType mt = MethodType.methodType(void.class, field.getType());
        try {
            return lookup().findVirtual(field.getDeclaringClass(), getter.toString(), mt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("property " + name + " must has getter method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("property " + name + " must has getter method");
    }
}
