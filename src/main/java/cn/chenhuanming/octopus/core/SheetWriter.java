package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.CellPosition;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * writes data into a sheet
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public interface SheetWriter<T> {

    CellPosition write(Sheet sheet, Collection<T> data);

}
