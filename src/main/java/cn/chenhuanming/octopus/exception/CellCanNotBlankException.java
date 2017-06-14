package cn.chenhuanming.octopus.exception;

import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import cn.chenhuanming.octopus.util.CellUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by chenhuanming on 2017-06-14.
 *
 * @author chenhuanming
 */
@Getter
public class CellCanNotBlankException extends ExcelImportException {

    public CellCanNotBlankException(String message, ModelEntityWithMethodHandle handle, Cell cell) {
        super(CellUtil.positionMsg(cell)+"requires not blank!", handle,cell);
    }
}
