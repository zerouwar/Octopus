package cn.chenhuanming.octopus.dataConvert;

import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandleInImport;
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
    public Object convert(Cell cell, Class clazz, ModelEntityWithMethodHandleInImport handle)throws ExcelImportException {
        return convertMap.get(clazz).convert(handle,cell);
    }

    public AbstractConvertManager addConverter(Class clazz,DataConverter converter){
        convertMap.put(clazz,converter);
        return this;
    }

//    protected Object getCellValueIfCellTypeEquals(ModelEntityWithMethodHandle handle,Cell cell,CellType cellType,Function<Cell,Object> equals) {
//        return getCellValueIfCellTypeEquals(handle,cell,cellType,equals,(handle1, cell1) ->{
//            throw new UnSupportedDataTypeException("cellType of cell ("+cell.getRowIndex()+","+cell.getColumnIndex()+") is not "+cellType,handle.getName(),handle.getDescription());
//        });
//    }
//
//    protected Object getCellValueIfCellTypeEquals(ModelEntityWithMethodHandle handle, Cell cell, CellType cellType, Function<Cell,Object> equals, BiFunction<ModelEntityWithMethodHandle,Cell,Object> notEquals)  {
//        if(cell.getCellTypeEnum()==cellType){
//            return equals.apply(cell);
//        } else
//            return notEquals.apply(handle,cell);
//    }
//
//    protected Object getCellValueIfIsString(ModelEntityWithMethodHandle handle, Cell cell, Function<String,Object> isString){
//        return getCellValueIfIsString(handle,cell,isString,(handle1, cell1) -> {
//            throw new UnSupportedDataTypeException("cell ("+cell.getRowIndex()+","+cell.getColumnIndex()+") is not string",handle.getName(),handle.getDescription());
//        });
//    }
//
//    protected Object getCellValueIfIsString(ModelEntityWithMethodHandle handle, Cell cell, Function<String,Object> isString,BiFunction<ModelEntityWithMethodHandle,Cell,Object> notString) {
//        if(cell.getCellTypeEnum()==CellType.STRING){
//            return isString.apply(cell.getStringCellValue());
//        }else
//            return notString.apply(handle,cell);
//    }
}
