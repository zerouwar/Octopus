package cn.chenhuanming.octopus.model;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/14
 */
@Data
public class SupportHeader extends AbstractField {
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

        //all height of chilren is max height value of them
        for (SupportHeader child : headChildren) {
            child.setHeight(h);
        }
        this.height = h + 1;
        this.width = w;
    }

    @Override
    public CellStyle getCellStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setFontHeightInPoints((short) (this.getFontSize() + 2));
        font.setBold(true);
        font.setColor((short) this.getColor().getRGB());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillBackgroundColor((short) this.getBackgroundColor().getRGB());
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
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
    public Method getPicker() {
        return null;
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
    public Color getBackgroundColor() {
        return field.getBackgroundColor();
    }
}
