package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CheckedData;
import cn.chenhuanming.octopus.model.ConfigReader;
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
    public SheetReader<CheckedData<T>> get(int index, ConfigReader configReader, CellPosition startPoint) {
        return new CheckedSheetReader<>(workbook.getSheetAt(index), configReader, startPoint);
    }
}
