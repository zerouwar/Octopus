package cn.chenhuanming.octopus.model;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface Config {
    Class getClazz();

    CellFormatterMap getCellFormatterMap();

    List<Field> getFields();
}
