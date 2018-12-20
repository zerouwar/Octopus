package cn.chenhuanming.octopus.exception;

import java.io.IOException;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public class WriteDataException extends IOException {
    public WriteDataException(String message) {
        super(message);
    }

    public WriteDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
