package cn.chenhuanming.octopus.model.formatter;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface FormatterContainer {
    <T> Formatter<T> get(Class<T> tClass);
}
