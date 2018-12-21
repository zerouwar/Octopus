package cn.chenhuanming.octopus.model;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
public interface Field extends FieldCellStyle {
    /**
     * attribute name,will be used to access from data
     *
     * @return
     */
    String getName();

    /**
     * attribute description used to write into sheet's header
     * @return
     */
    String getDescription();

    /**
     * default value
     * @return
     */
    String getDefaultValue();

    /**
     * format content which is wrote into excel or read from excel
     * @return
     */
    CellFormatter getFormatter();

    /**
     * format value if is date type
     * @return
     */
    CellFormatter<Date> getDateFormat();

    /**
     * method that access value from data
     *
     * @return
     */
    Method getPicker();

    /**
     * method that set value into data
     *
     * @return
     */
    Method getPusher();

    /**
     * children of field,normally represent tree construct of data.
     * It will be used to write and read excel
     * @return
     */
    List<Field> getChildren();

    /**
     * whether is a leaf attribute,depending on return value of {@link #getChildren()} in common
     * @return
     */
    boolean isLeaf();

}
