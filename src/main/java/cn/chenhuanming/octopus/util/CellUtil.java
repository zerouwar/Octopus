package cn.chenhuanming.octopus.util;

import cn.chenhuanming.octopus.exception.DataFormatException;
import cn.chenhuanming.octopus.exception.UnSupportedDataTypeException;
import cn.chenhuanming.octopus.model.ModelEntityWithMethodHandle;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Function;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class CellUtil {
    public static String positionMsg(Cell cell){
        return String.format("cell (%d,%d) ",cell.getRowIndex()+1,cell.getColumnIndex()+1);
    }

    public static Object formatDefaultValue(ModelEntityWithMethodHandle handle, Cell cell, Class clazz, Function<String,Object> convertDefaultValue,Object valueIfStringEmpty){
        return formatString(handle,cell,clazz,convertDefaultValue,handle.getDefaultValue(),valueIfStringEmpty);
    }

    public static Object formatString(ModelEntityWithMethodHandle handle, Cell cell, Class clazz, Function<String,Object> convert,String value,Object valueIfEmptyString){
        try{
            if(value.equals(""))
                return valueIfEmptyString;
            return convert.apply(value);
        }catch (Exception e){
            throw new DataFormatException(handle,clazz,cell);
        }
    }

    public static String getStringValue(Cell cell){
        switch (cell.getCellTypeEnum()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            case _NONE:
                return "";
            case FORMULA:
                return cell.getCellFormula();
            case ERROR:
                throw new UnSupportedDataTypeException("cell type is error!",null);
            default:
                throw new UnSupportedDataTypeException("unknow cell type!",null);
        }
    }
}
