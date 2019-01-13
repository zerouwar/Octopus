package cn.chenhuanming.octopus.model;

import org.apache.poi.ss.usermodel.BorderStyle;
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

    BorderStyle[] getBorder();

    Color[] getBorderColor();

    CellStyle getCellStyle(Workbook book);
}
