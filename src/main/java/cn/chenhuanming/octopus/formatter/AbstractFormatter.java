package cn.chenhuanming.octopus.formatter;


import cn.chenhuanming.octopus.exception.ParseException;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public abstract class AbstractFormatter<T> implements Formatter<T> {
    @Override
    public T parse(String str) throws ParseException {
        try {
            return parseImpl(str);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), e);
        }
    }

    public abstract T parseImpl(String str) throws Exception;
}
