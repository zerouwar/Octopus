package cn.chenhuanming.octopus.formatter;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface FormatterContainer {
    <T> Formatter<T> get(Class<T> tClass);
}
