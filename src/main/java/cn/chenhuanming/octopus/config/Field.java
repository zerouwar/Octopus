package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.Formatter;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/15
 */
@Value
@Builder
public class Field {

    /**
     * Used to access from data.
     */
    @NonNull
    private String name;

    /**
     * attribute description used to write into sheet's header
     */
    @NonNull
    private String description;

    /**
     * default value
     */
    private String defaultValue;

    /**
     * format content which is wrote into excel or read from excel
     */
    private Formatter formatter;

    /**
     * method that access value from data
     */
    @NonNull
    private Method picker;

    /**
     * method that set value into data
     */
    @NonNull
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
    @NonNull
    private FieldCellStyle headerFieldCellStyle;

    /**
     * validation config when importing excel
     */
    private ImportValidation importValidation;

//    public Field() {
//        description = "";
//        defaultValue = null;
//        children = new ArrayList<>();
//
//        fieldCellStyle = FieldCellStyle.defaultCellStyle();
//        headerFieldCellStyle = FieldCellStyle.defaultHeaderCellStyle();
//    }
//
//    public Field(List<Field> children) {
//        this();
//        this.setChildren(children);
//    }

    public boolean isLeaf() {
        return getChildren() == null || getChildren().size() == 0;
    }
}
