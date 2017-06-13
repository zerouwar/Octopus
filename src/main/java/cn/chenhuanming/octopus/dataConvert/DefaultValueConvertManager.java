package cn.chenhuanming.octopus.dataConvert;

import cn.chenhuanming.octopus.dictionary.DefaultValue;
import cn.chenhuanming.octopus.util.CellUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class DefaultValueConvertManager extends AbstractConvertManager{

    public DefaultValueConvertManager() {
        addConverter(String.class,(handle, cell) -> handle.getDefaultValue());
        addConverter(Integer.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Integer.class,s->Integer.valueOf(handle.getDefaultValue()), DefaultValue.INTEGER));
        addConverter(int.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,int.class, s->Integer.valueOf(handle.getDefaultValue()),DefaultValue.BASIC_INT));
        addConverter(Double.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Double.class,s->Double.valueOf(handle.getDefaultValue()),DefaultValue.DOUBLE));
        addConverter(double.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,double.class,s->Double.valueOf(handle.getDefaultValue()),DefaultValue.BASIC_DOUBLE));
        addConverter(Float.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Float.class,s->Float.valueOf(handle.getDefaultValue()),DefaultValue.FLOAT));
        addConverter(float.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,float.class,s->Float.valueOf(handle.getDefaultValue()),DefaultValue.BASIC_FLOAT));
        addConverter(Short.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Short.class,s->Short.valueOf(handle.getDefaultValue()),DefaultValue.SHORT));
        addConverter(short.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,short.class,s->Short.valueOf(handle.getDefaultValue()),DefaultValue.BASIC_SHORT));
        addConverter(Long.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Long.class,s->Long.valueOf(handle.getDefaultValue()),DefaultValue.LONG));
        addConverter(long.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,long.class,s->Long.valueOf(handle.getDefaultValue()),DefaultValue.BASIC_LONG));
        addConverter(Date.class, (handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Date.class,s->dateFormat.format(s),null));
        addConverter(LocalDate.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Date.class,s->LocalDate.parse(handle.getDefaultValue()),null));
        addConverter(LocalDateTime.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Date.class,s->LocalDateTime.parse(handle.getDefaultValue()),null));
        addConverter(LocalTime.class,(handle, cell) -> CellUtil.formatDefaultValue(handle,cell,Date.class,s->LocalTime.parse(handle.getDefaultValue()),null));
    }

}
