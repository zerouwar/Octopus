package cn.chenhuanming.octopus.config;

import org.apache.poi.ss.usermodel.BorderStyle;

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

}
