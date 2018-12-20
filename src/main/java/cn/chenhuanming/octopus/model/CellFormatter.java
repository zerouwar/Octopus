package cn.chenhuanming.octopus.model;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface CellFormatter<T> {
    String format(T t);

    T parse(String str);
}
