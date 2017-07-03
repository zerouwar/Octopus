package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public interface SheetWriter<T> {
    void write(Sheet sheet, CellStyle headStyle, CellStyle contentStyle, Collection<T> collection);
}
