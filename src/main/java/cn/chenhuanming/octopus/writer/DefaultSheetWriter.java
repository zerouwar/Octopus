package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.DefaultCellPosition;
import cn.chenhuanming.octopus.model.WorkbookContext;
import cn.chenhuanming.octopus.util.CellUtils;
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

    public DefaultSheetWriter(Config config) {
        this(config, new DefaultHeaderWriter(), CellUtils.POSITION_ZERO_ZERO);
    }

    @Override
    CellPosition writeContent(Sheet sheet, Collection<T> data, CellPosition headerEndPos, WorkbookContext workbookContext) {
        int row = headerEndPos.getRow() + 1;
        int col = startPoint.getCol();

        for (T t : data) {
            for (Field field : config.getFields()) {
                col = writeField(sheet, row, col, field, t, workbookContext);
            }
            col = startPoint.getCol();
            row++;
        }
        return new DefaultCellPosition(row, headerEndPos.getCol());
    }
}
