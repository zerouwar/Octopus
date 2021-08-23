package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CellPositions;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.WorkbookContext;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * Default sheet writer,writes sheet header and content starting on start point
 *
 * @author chenhuanming
 * Created at 2018/12/16
 */
public class DefaultSheetWriter<T> extends AbstractSheetWriter<T> {

    public DefaultSheetWriter(Config config, HeaderWriter headerWriter, CellPosition startPoint) {
        super(config, headerWriter, startPoint);
    }

    public DefaultSheetWriter(Config config, CellPosition startPoint) {
        super(config, new DefaultHeaderWriter(), startPoint);
    }

    public DefaultSheetWriter(Config config) {
        this(config, new DefaultHeaderWriter(), CellPositions.POSITION_ZERO_ZERO);
    }

    @Override
    CellPosition writeContent(Sheet sheet, Collection<T> data, CellPosition startPosition, WorkbookContext workbookContext) {
        int row = startPosition.getRow();
        int col = startPosition.getCol();

        for (T t : data) {
            col = startPosition.getCol();
            for (Field field : config.getFields()) {
                col = writeField(sheet, row, col, field, t, workbookContext);
            }
            row++;
        }
        return new DefaultCellPosition(row, col);
    }
}
