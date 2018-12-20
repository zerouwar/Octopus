package cn.chenhuanming.octopus.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public class DefaultCellformatter implements CellFormatterMap {

    private Map<Class, CellFormatter> formatMap = new HashMap<>();

    public <T> void addFormat(Class<T> clazz, CellFormatter<T> cellFormatter) {
        formatMap.put(clazz, cellFormatter);
    }

    @Override
    public CellFormatter get(Class c) {
        return formatMap.get(c);
    }
}
