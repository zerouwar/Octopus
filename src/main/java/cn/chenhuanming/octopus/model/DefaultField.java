package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.Formatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class DefaultField implements Field {

    protected String name;
    protected String description;
    protected String defaultValue;
    protected Formatter<Date> dateFormat;
    protected Formatter formatter;
    protected short fontSize;
    protected java.awt.Color color;
    protected boolean bold;
    protected java.awt.Color backgroundColor;
    protected Method picker;
    protected Method pusher;
    protected boolean blankable;
    protected List<String> options;
    protected Pattern regex;
    protected List<Field> children;

    public DefaultField() {
        description = "";
        defaultValue = null;
        fontSize = 14;
        color = java.awt.Color.BLACK;
        bold = false;
        backgroundColor = java.awt.Color.WHITE;
        children = new ArrayList<>();
    }

    public DefaultField(List<Field> children) {
        this();
        this.setChildren(children);
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

    public DefaultField addChildren(Field field) {
        getChildren().add(field);
        return this;
    }
}
