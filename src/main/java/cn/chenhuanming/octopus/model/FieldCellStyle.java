package cn.chenhuanming.octopus.model;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */
public interface FieldCellStyle {
    short getFontSize();

    Color getColor();

    boolean isBold();

    Color getForegroundColor();

    CellStyle getCellStyle(Workbook book);
}
