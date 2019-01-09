package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.ConfigReader;
import cn.chenhuanming.octopus.util.CellUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * @author chenhuanming
 * Created at 2018/12/16
 */
public class DefaultSheetWriter<T> extends AbstractSheetWriter<T> {
    public DefaultSheetWriter(ConfigReader configReader, HeaderWriter headerWriter, CellPosition startPoint) {
        super(configReader, headerWriter, startPoint);
    }

    public DefaultSheetWriter(ConfigReader configReader) {
        this(configReader, new DefaultHeaderWriter(), CellUtils.POSITION_ZERO_ZERO);
    }

    @Override
    public CellPosition write(Sheet sheet, Collection<T> data) {
        CellPosition end = super.write(sheet, data);
        adjustColumnWidth(sheet, end);

        return end;
    }

    private void adjustColumnWidth(Sheet sheet, CellPosition end) {
        for (int col = getStartColnum(); col <= end.getCol(); col++) {
            sheet.autoSizeColumn(col, true);
        }
    }
}
