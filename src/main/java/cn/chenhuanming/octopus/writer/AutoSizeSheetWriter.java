package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.model.CellPosition;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * Auto size all content,but width of config will be ignored
 *
 * @author guangdao
 * Created at 2021-08-15
 * @since 1.1.5
 */
public class AutoSizeSheetWriter implements SheetWriter {
    private final SheetWriter sheetWriter;

    public AutoSizeSheetWriter(SheetWriter sheetWriter) {
        this.sheetWriter = sheetWriter;
    }

    @Override
    public CellPosition write(Sheet sheet, Collection data) {
        CellPosition end = sheetWriter.write(sheet, data);
        for (int i = 0; i < end.getCol(); i++) {
            sheet.autoSizeColumn(i, true);
        }
        return end;
    }
}
