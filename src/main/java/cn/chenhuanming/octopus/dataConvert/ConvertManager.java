package cn.chenhuanming.octopus.dataConvert;

import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public interface ConvertManager {
    Object convert(Cell cell,Class clazz,ImportModelProperty handle) throws ExcelImportException;
}
