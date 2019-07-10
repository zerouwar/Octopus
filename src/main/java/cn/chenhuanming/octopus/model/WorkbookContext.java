package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.config.FieldCellStyle;
import cn.chenhuanming.octopus.util.ColorUtils;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

/**
 * context of one workbook
 * manage limited resources of workbook,such as color,fieldCellStyle
 *
 * @author chenhuanming
 * Created at 2019-02-18
 */
@Getter
public class WorkbookContext {
    private static final ThreadLocal<WorkbookContext> holder = new ThreadLocal<>();

    public static WorkbookContext get() {
        return holder.get();
    }

    public static void init(Workbook book, Config config) {
        holder.set(new WorkbookContext(book, config));
    }

    private Workbook book;
    private Config config;
    private Map<Field, CellStyle> cellStyleMap;
    private Map<Field, CellStyle> headerStyleMap;

    private WorkbookContext(Workbook book, Config config) {
        this.book = book;
        this.config = config;
        this.cellStyleMap = new HashMap<>();
        this.headerStyleMap = new HashMap<>();
    }

    public CellStyle getCellStyle(Field field) {
        CellStyle style = cellStyleMap.get(field);
        if (style == null) {
            style = book.createCellStyle();
            Font font = book.createFont();
            FieldCellStyle fieldCellStyle = field.getFieldCellStyle();


            font.setFontHeightInPoints(fieldCellStyle.getFontSize());
            font.setBold(fieldCellStyle.isBold());
            ColorUtils.setColor(book, font, fieldCellStyle.getColor());
            style.setFont(font);
            ColorUtils.setForegroundColor(book, style, fieldCellStyle.getForegroundColor());

            setStyleBorder(style, fieldCellStyle.getBorder());
            ColorUtils.setBorderColor(book, style, fieldCellStyle.getBorderColor());

            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleMap.put(field, style);
            return style;
        }
        return style;
    }

    public CellStyle getHeaderStyle(Field field) {
        CellStyle style = headerStyleMap.get(field);
        if (style == null) {
            style = book.createCellStyle();
            Font font = book.createFont();
            FieldCellStyle fieldCellStyle = field.getHeaderFieldCellStyle();


            font.setFontHeightInPoints(fieldCellStyle.getFontSize());
            font.setBold(fieldCellStyle.isBold());
            ColorUtils.setColor(book, font, fieldCellStyle.getColor());
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            ColorUtils.setForegroundColor(book, style, fieldCellStyle.getForegroundColor());

            setStyleBorder(style, fieldCellStyle.getBorder());
            ColorUtils.setBorderColor(book, style, fieldCellStyle.getBorderColor());
            cellStyleMap.put(field, style);
            return style;
        }
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
