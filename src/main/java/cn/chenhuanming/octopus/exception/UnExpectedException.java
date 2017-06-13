package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class UnExpectedException extends ExcelImportException {

    public UnExpectedException(String message, Throwable cause,ModelEntityWithMethodHandle handle) {
        super(message,cause, handle);
    }
}
