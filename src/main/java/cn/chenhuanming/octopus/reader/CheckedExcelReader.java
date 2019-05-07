package cn.chenhuanming.octopus.reader;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CheckedData;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author chenhuanming
 * Created at 2019-01-12
 */
public class CheckedExcelReader<T> extends DefaultExcelReader<CheckedData<T>> {
    public CheckedExcelReader(Workbook workbook) {
        super(workbook);
    }

    @Override
    public SheetReader<CheckedData<T>> get(int index, Config config, CellPosition startPoint) {
        return new CheckedSheetReader<>(workbook.getSheetAt(index), config, startPoint);
    }
}
