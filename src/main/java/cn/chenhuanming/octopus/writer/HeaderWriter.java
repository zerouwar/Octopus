package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.model.CellPosition;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
public interface HeaderWriter {
    /**
     * Draw header into sheet according to fields config
     *
     * @param sheet      sheet
     * @param startPoint where to start to draw header
     * @param fields     field config
     * @return draw end position
     */
    CellPosition drawHeader(Sheet sheet, CellPosition startPoint, List<Field> fields);
}
