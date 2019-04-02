package cn.chenhuanming.octopus.formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public class DefaultFormatterContainer implements FormatterContainer {

    private Map<Class, Formatter> formatMap;

    public DefaultFormatterContainer() {
        formatMap = new HashMap<>();
        formatMap.put(Integer.class, new IntegerFormatter());
        formatMap.put(Integer.TYPE, new IntegerFormatter());
        formatMap.put(Double.class, new DoubleFormatter());
        formatMap.put(Double.TYPE, new DoubleFormatter());
        formatMap.put(Long.class, new LongFormatter());
        formatMap.put(Long.TYPE, new LongFormatter());
        formatMap.put(Float.class, new FloatFormatter());
        formatMap.put(Float.TYPE, new LongFormatter());
        formatMap.put(Boolean.class, new BooleanFormatter());
        formatMap.put(Boolean.TYPE, new BooleanFormatter());
        formatMap.put(Short.class, new ShortFormatter());
        formatMap.put(Short.TYPE, new ShortFormatter());

    }

    public <T> void addFormat(Class<T> clazz, Formatter<T> formatter) {
        formatMap.put(clazz, formatter);
    }


    @Override
    public <T> Formatter<T> get(Class<T> tClass) {
        return formatMap.get(tClass);
    }
}
