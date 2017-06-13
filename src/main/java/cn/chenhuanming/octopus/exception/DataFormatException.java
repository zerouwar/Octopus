package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import cn.chenhuanming.octopus.util.CellUtil;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class DataFormatException extends ExcelImportException {

    public DataFormatException(ModelEntityWithMethodHandle handle, Class clazz, Cell cell) {
        super("in "+CellUtil.positionMsg(cell)+","+CellUtil.getStringValue(cell)+" can not be formatted to "+clazz,handle);
    }


}
