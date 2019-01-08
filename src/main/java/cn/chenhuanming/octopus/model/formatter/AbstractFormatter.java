package cn.chenhuanming.octopus.model.formatter;


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
            throw new ParseException(e);
        }
    }

    abstract T parseImpl(String str) throws Exception;
}
