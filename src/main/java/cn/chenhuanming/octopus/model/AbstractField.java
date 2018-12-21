package cn.chenhuanming.octopus.model;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Data
public abstract class AbstractField implements Field {

    protected String name;
    protected String description;
    protected String defaultValue;
    protected CellFormatter<Date> dateFormat;
    protected CellFormatter formatter;
    protected short fontSize;
    protected java.awt.Color color;
    protected boolean bold;
    protected java.awt.Color backgroundColor;
    protected Method picker;
    protected Method pusher;
    protected List<Field> children;

    public AbstractField() {
        description = "";
        defaultValue = "";
        fontSize = 14;
        color = java.awt.Color.BLACK;
        bold = false;
        backgroundColor = java.awt.Color.WHITE;
        children = new ArrayList<>();
    }

    @Override
    public boolean isLeaf() {
        return getChildren() == null || getChildren().size() == 0;
    }

    @Override
    public CellStyle getCellStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setFontHeightInPoints(this.getFontSize());
        font.setBold(this.isBold());
        font.setColor((short) this.getColor().getRGB());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillBackgroundColor((short) this.getBackgroundColor().getRGB());
        return style;
    }

    public AbstractField addChildren(Field field) {
        getChildren().add(field);
        return this;
    }
}
