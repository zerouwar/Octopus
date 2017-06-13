package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;

/**
 * Created by chenhuanming on 2017-06-12.
 * 暂不支持该数据类型或者操作
 * @author chenhuanming
 */
public class UnSupportedDataTypeException extends ExcelImportException {
    public UnSupportedDataTypeException(String message, ModelEntityWithMethodHandle handle) {
        super(message, handle);
    }
}
