package cn.chenhuanming.octopus.dictionary;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhuanming on 2017-06-13.
 *
 * @author chenhuanming
 */
public final class DefaultValue {
    public static final Integer INTEGER = Integer.valueOf(0);
    public static final int BASIC_INT = 0;
    public static final Double DOUBLE = Double.valueOf(0);
    public static final double BASIC_DOUBLE = 0d;
    public static final Float FLOAT = Float.valueOf(0);
    public static final float BASIC_FLOAT = 0f;
    public static final Short SHORT = Short.valueOf((short) 0);
    public static final short BASIC_SHORT = 0;
    public static final Long LONG = Long.valueOf(0);
    public static final long BASIC_LONG = 0L;

    private static Map<Class,Object> map = new HashMap<>();

    static {
        Field[] fields = DefaultValue.class.getDeclaredFields();
        for (Field field:fields) {
            if(field.getName().equals("map"))
                continue;
            try {
                map.put(field.getType(),field.get(DefaultValue.class));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object getDefaultValue(Class clazz){
        return map.get(clazz);
    }
}
