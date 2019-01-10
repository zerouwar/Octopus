package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.Field;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public interface HeaderWriter {
    CellPosition drawHeader(Sheet sheet, CellPosition startPoint, List<Field> fields);
}
