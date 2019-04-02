package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.Formatter;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Data
public class Field {

    /**
     * Used to access from data.
     */
    private String name;

    /**
     * attribute description used to write into sheet's header
     */
    private String description;

    /**
     * default value
     */
    private String defaultValue;

    /**
     * format value if it is date type
     */
    private Formatter<Date> dateFormat;

    /**
     * format content which is wrote into excel or read from excel
     */
    private Formatter formatter;

    /**
     * method that access value from data
     */
    private Method picker;

    /**
     * method that set value into data
     */
    private Method pusher;

    /**
     * children of field,normally represent tree construct of data.
     * It will be used to write and read excel
     */
    private List<Field> children;

    /**
     * cell style of table body when exporting excel
     */
    private FieldCellStyle fieldCellStyle;

    /**
     * cell style of table header when exporting excel
     */
    private FieldCellStyle headerFieldCellStyle;

    /**
     * validation config when importing excel
     */
    private ImportValidation importValidation;

    public Field() {
        description = "";
        defaultValue = null;
        children = new ArrayList<>();

        fieldCellStyle = FieldCellStyle.defaultCellStyle();
        headerFieldCellStyle = FieldCellStyle.defaultHeaderCellStyle();
    }

    public Field(List<Field> children) {
        this();
        this.setChildren(children);
    }

    public boolean isLeaf() {
        return getChildren() == null || getChildren().size() == 0;
    }

    public Field addChildren(Field field) {
        getChildren().add(field);
        return this;
    }
}
