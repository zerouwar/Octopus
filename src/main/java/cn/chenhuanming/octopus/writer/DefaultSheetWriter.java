package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.util.CellUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.Collection;

/**
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
    public CellPosition write(Sheet sheet, Collection<T> data) {
        CellPosition end = super.write(sheet, data);
        adjustColumnWidth(sheet, end);

        return end;
    }

    private void adjustColumnWidth(Sheet sheet, CellPosition end) {
        if (sheet instanceof SXSSFSheet) {
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        }
        for (int col = getStartColumn(); col <= end.getCol(); col++) {
            sheet.autoSizeColumn(col, true);
        }
    }
}
