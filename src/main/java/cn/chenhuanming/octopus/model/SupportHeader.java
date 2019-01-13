package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.Formatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SupportHeader extends DefaultField {
    private Field field;
    private int height;
    private int width;
    private List<SupportHeader> headChildren;

    public SupportHeader(Field field) {
        this.field = field;
        this.headChildren = new ArrayList<>(field.getChildren().size());
        if (field.isLeaf()) {
            this.height = 1;
            this.width = 1;
            return;
        }
        int h = 1;
        int w = 0;
        for (Field child : field.getChildren()) {
            SupportHeader header = new SupportHeader(child);
            h = Math.max(h, header.getHeight());
            w += header.width;
            headChildren.add(header);
        }

        //height of all children is the max value of them
        for (SupportHeader child : headChildren) {
            child.setHeight(h);
        }
        this.height = h + 1;
        this.width = w;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public String getDescription() {
        return field.getDescription();
    }

    @Override
    public String getDefaultValue() {
        return field.getDefaultValue();
    }

    @Override
    public List<Field> getChildren() {
        return field.getChildren();
    }

    public List<SupportHeader> getHeaderChildren() {
        return this.headChildren;
    }

    @Override
    public boolean isLeaf() {
        return field.isLeaf();
    }

    @Override
    public short getFontSize() {
        return field.getFontSize();
    }

    @Override
    public Color getColor() {
        return field.getColor();
    }

    @Override
    public boolean isBold() {
        return field.isBold();
    }

    @Override
    public Color getForegroundColor() {
        return field.getForegroundColor();
    }

    @Override
    public Formatter<Date> getDateFormat() {
        return field.getDateFormat();
    }

    @Override
    public Formatter getFormatter() {
        return field.getFormatter();
    }

    @Override
    public Color getHeaderForegroundColor() {
        return field.getHeaderForegroundColor();
    }

    @Override
    public Color getHeaderColor() {
        return field.getHeaderColor();
    }

    @Override
    public short getHeaderFontSize() {
        return field.getHeaderFontSize();
    }

    @Override
    public List<String> getOptions() {
        return field.getOptions();
    }

    @Override
    public Method getPicker() {
        return field.getPicker();
    }

    @Override
    public Method getPusher() {
        return field.getPusher();
    }

    @Override
    public Pattern getRegex() {
        return field.getRegex();
    }

    @Override
    public CellStyle getCellStyle(Workbook book) {
        return field.getCellStyle(book);
    }

    @Override
    public CellStyle getHeaderCellStyle(Workbook book) {
        return field.getHeaderCellStyle(book);
    }
}
