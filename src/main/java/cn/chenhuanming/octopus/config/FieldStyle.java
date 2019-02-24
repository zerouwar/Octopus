package cn.chenhuanming.octopus.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.BorderStyle;

/**
 * @author chenhuanming
 * Created at 2019-01-11
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class FieldStyle implements FieldCellStyle, HeaderCellStyle {
    /**
     * cell style
     */
    protected short fontSize;
    protected java.awt.Color color;
    protected boolean bold;
    protected java.awt.Color foregroundColor;
    protected BorderStyle[] border;
    protected java.awt.Color[] borderColor;

    /**
     * Header style
     */
    protected short headerFontSize;
    protected java.awt.Color headerColor;
    protected boolean headerBold;
    protected java.awt.Color headerForegroundColor;
    protected BorderStyle[] headerBorder;
    protected java.awt.Color[] headerBorderColor;

    public FieldStyle() {
        fontSize = 14;
        color = java.awt.Color.BLACK;
        bold = false;
        border = null;
        borderColor = null;

        headerFontSize = 15;
        headerColor = java.awt.Color.BLACK;
        headerBold = true;
        headerBorder = new BorderStyle[]{BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN};
        headerBorderColor = null;
    }
}
