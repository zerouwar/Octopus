package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ImportModelProperty;
import cn.chenhuanming.octopus.util.CellPosition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Excel导入异常类的父类
 * @author chenhuanming
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class ExcelImportException extends RuntimeException{

    private String property;

    private String propertyDescription;

    private String wrongMsg;

    private CellPosition cellPosition;

    ExcelImportException(String message, ImportModelProperty handle) {
        super(message);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
    }

    ExcelImportException(String message, Throwable cause, ImportModelProperty handle) {
        super(message, cause);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
    }

    public ExcelImportException(String message, ImportModelProperty handle, Cell cell) {
        super(message);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
        this.cellPosition = new CellPosition(cell);
    }

    public ExcelImportException(String message, Throwable cause, ImportModelProperty handle, Cell cell) {
        super(message, cause);
        this.property = handle.getName();
        this.propertyDescription = handle.getDescription();
        this.wrongMsg = handle.getWrongMsg();
        this.cellPosition = new CellPosition(cell);
    }

}
