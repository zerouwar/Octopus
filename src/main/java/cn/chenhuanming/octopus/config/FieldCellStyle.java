package cn.chenhuanming.octopus.config;

import org.apache.poi.ss.usermodel.BorderStyle;

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

}
