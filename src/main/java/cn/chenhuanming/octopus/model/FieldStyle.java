package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.util.ColorUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;

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

    @Override
    public CellStyle getCellStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setFontHeightInPoints(this.getFontSize());
        font.setBold(this.isBold());
        ColorUtils.setColor(book, font, getColor());
        style.setFont(font);
        ColorUtils.setForegroundColor(book, style, this.getForegroundColor());

        setStyleBorder(style, border);
        ColorUtils.setBorderColor(book, style, borderColor);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    @Override
    public CellStyle getHeaderCellStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setFontHeightInPoints(this.getHeaderFontSize());
        font.setBold(this.isHeaderBold());
        ColorUtils.setColor(book, font, this.getHeaderColor());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        ColorUtils.setForegroundColor(book, style, this.getHeaderForegroundColor());

        setStyleBorder(style, headerBorder);
        ColorUtils.setBorderColor(book, style, headerBorderColor);

        return style;
    }

    private void setStyleBorder(CellStyle style, BorderStyle[] border) {
        if (border != null) {
            if (border[0] != null) {
                style.setBorderTop(border[0]);
            }
            if (border[1] != null) {
                style.setBorderRight(border[1]);
            }
            if (border[2] != null) {
                style.setBorderBottom(border[2]);
            }
            if (border[3] != null) {
                style.setBorderLeft(border[3]);
            }
        }
    }
}
