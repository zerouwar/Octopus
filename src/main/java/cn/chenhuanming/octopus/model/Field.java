package cn.chenhuanming.octopus.model;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/7
 */
public interface Field extends FieldCellStyle {
    String getName();

    String getDescription();

    String getDefaultValue();

    CellFormatter getFormatter();

    CellFormatter<Date> getDateFormat();

    Method getInvoker();

    List<Field> getChildren();

    boolean isLeaf();

}
