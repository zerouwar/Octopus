package cn.chenhuanming.octopus.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
@Data
public class DefaultConfig implements Config {
    private Class clazz;
    private CellFormatterMap cellFormatterMap;
    private List<Field> fields;
}
