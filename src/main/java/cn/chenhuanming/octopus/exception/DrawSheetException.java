package cn.chenhuanming.octopus.exception;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public class DrawSheetException extends WriteDataException {
    public DrawSheetException(String message) {
        super(message);
    }

    public DrawSheetException(String message, Throwable cause) {
        super(message, cause);
    }
}
