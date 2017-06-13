package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import lombok.Getter;

/**
 * Excel导入异常类的父类
 * @author chenhuanming
 */
@Getter
public class ExcelImportException extends RuntimeException{

    private String property;

    private String propertyDescription;

    private String wrongMsg;

    public ExcelImportException(String message, ModelEntityWithMethodHandle handle) {
        super(message);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
    }

    public ExcelImportException(String message, Throwable cause, ModelEntityWithMethodHandle handle) {
        super(message, cause);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
    }

}
