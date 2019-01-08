package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.FormatterContainer;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface Config {
    Class getClassType();

    FormatterContainer getFormatterContainer();

    List<Field> getFields();
}
