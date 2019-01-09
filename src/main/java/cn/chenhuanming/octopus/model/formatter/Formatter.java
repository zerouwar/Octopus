package cn.chenhuanming.octopus.model.formatter;

import cn.chenhuanming.octopus.exception.ParseException;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface Formatter<T> {
    String format(T t);

    T parse(String str) throws ParseException;
}
