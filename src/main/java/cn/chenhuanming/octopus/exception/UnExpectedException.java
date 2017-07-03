package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandleInImport;
import cn.chenhuanming.octopus.util.CellPosition;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class UnExpectedException extends ExcelImportException {

    public UnExpectedException(String message, Throwable cause, ModelEntityWithMethodHandleInImport handle, int rowNum, int colNum) {
        super(message,cause, handle);
        setCellPosition(new CellPosition(rowNum,colNum));
    }
}
