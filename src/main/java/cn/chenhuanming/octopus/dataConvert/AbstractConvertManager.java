package cn.chenhuanming.octopus.dataConvert;

import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
@Setter(value = AccessLevel.PROTECTED)
public abstract class AbstractConvertManager implements ConvertManager{
    private Map<Class,DataConverter> convertMap;
    protected ZoneId zone;//时区，默认取当前时区
    protected SimpleDateFormat dateFormat;//时间格式化
    protected DecimalFormat decimalFormat;//double类型的数据小数点尾数位数

    public AbstractConvertManager() {
        convertMap = new HashMap<>();
        this.zone = ZoneId.systemDefault();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.decimalFormat = new DecimalFormat("#");
    }

    @Override
    public Object convert(Cell cell, Class clazz, ImportModelProperty handle)throws ExcelImportException {
        return convertMap.get(clazz).convert(handle,cell);
    }

    public AbstractConvertManager addConverter(Class clazz,DataConverter converter){
        convertMap.put(clazz,converter);
        return this;
    }

}
