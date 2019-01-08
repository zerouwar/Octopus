package cn.chenhuanming.octopus.model.formatter;

/**
 * @author chenhuanming
 * Created at 2019-01-08
 */
public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
