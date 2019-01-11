package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.model.formatter.Formatter;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
public interface Field extends FieldCellStyle, ImportValidation, HeaderCellStyle {
    /**
     * attribute name,will be used to access from data
     */
    String getName();

    /**
     * attribute description used to write into sheet's header
     */
    String getDescription();

    /**
     * default value
     */
    String getDefaultValue();

    /**
     * format content which is wrote into excel or read from excel
     */
    Formatter getFormatter();

    /**
     * format value if it is date type
     */
    Formatter<Date> getDateFormat();

    /**
     * method that access value from data
     */
    Method getPicker();

    /**
     * method that set value into data
     */
    Method getPusher();

    /**
     * children of field,normally represent tree construct of data.
     * It will be used to write and read excel
     */
    List<Field> getChildren();

    /**
     * whether is a leaf attribute,depending on return value of {@link #getChildren()} in common
     */
    boolean isLeaf();

}
