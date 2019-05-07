package cn.chenhuanming.octopus.reader;


import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.exception.SheetNotFoundException;
import cn.chenhuanming.octopus.model.CellPosition;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public interface ExcelReader<T> {
    SheetReader<T> get(int index, Config config, CellPosition startPoint) throws ArrayIndexOutOfBoundsException;

    SheetReader<T> get(String sheetName, Config config, CellPosition startPoint) throws SheetNotFoundException;
}
