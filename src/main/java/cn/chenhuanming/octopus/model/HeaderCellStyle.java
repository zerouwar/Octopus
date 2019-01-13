package cn.chenhuanming.octopus.model;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;

/**
 * @author chenhuanming
 * Created at 2019-01-11
 */
public interface HeaderCellStyle {
    short getHeaderFontSize();

    Color getHeaderColor();

    boolean isHeaderBold();

    Color getHeaderForegroundColor();

    BorderStyle[] getHeaderBorder();

    Color[] getHeaderBorderColor();

    CellStyle getHeaderCellStyle(Workbook book);
}
