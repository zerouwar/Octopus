package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ImportModelProperty;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-12.
 * 暂不支持该数据类型或者操作
 * @author chenhuanming
 */
public class UnSupportedDataTypeException extends ExcelImportException {
    public UnSupportedDataTypeException(String message, ImportModelProperty handle, Cell cell) {
        super(message, handle,cell);
    }
}
