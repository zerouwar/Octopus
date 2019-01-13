package cn.chenhuanming.octopus.core;


import cn.chenhuanming.octopus.exception.SheetNotFoundException;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.ConfigReader;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public interface ExcelReader<T> {
    SheetReader<T> get(int index, ConfigReader configReader, CellPosition startPoint) throws ArrayIndexOutOfBoundsException;

    SheetReader<T> get(String sheetName, ConfigReader configReader, CellPosition startPoint) throws SheetNotFoundException;
}
