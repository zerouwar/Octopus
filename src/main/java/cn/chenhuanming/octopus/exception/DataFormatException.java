package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ImportModelProperty;
import cn.chenhuanming.octopus.util.CellUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
@Getter
public class DataFormatException extends ExcelImportException {

    public DataFormatException(ImportModelProperty handle, Class clazz, Cell cell) {
        super("in "+CellUtil.positionMsg(cell)+","+CellUtil.getStringValue(cell)+" can not be formatted to "+clazz,handle,cell);
    }


}
