package cn.chenhuanming.octopus.util;

import cn.chenhuanming.octopus.exception.DataFormatException;
import cn.chenhuanming.octopus.exception.UnSupportedDataTypeException;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.function.Function;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public final class CellUtil {
    public static String positionMsg(Cell cell){
        return String.format("cell (%d,%d) ",cell.getRowIndex()+1,cell.getColumnIndex()+1);
    }

    public static Object formatDefaultValue(ImportModelProperty handle, Cell cell, Class clazz, Function<String,Object> convertDefaultValue, Object valueIfStringEmpty){
        return formatString(handle,cell,clazz,convertDefaultValue,handle.getDefaultValue(),valueIfStringEmpty);
    }

    public static Object formatString(ImportModelProperty handle, Cell cell, Class clazz, Function<String,Object> convert, String value, Object valueIfEmptyString){
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
                throw new UnSupportedDataTypeException("cell type is error!",null,cell);
            default:
                throw new UnSupportedDataTypeException("unknow cell type!",null,cell);
        }
    }

    public static void createCells(int startRow, int lastRow, int startCol, int lastCol, Sheet sheet, CellStyle cellStyle){
        //create all cells we need
        for (int i = startRow; i <=lastRow; i++) {
            Row row = sheet.createRow(i);
            for (int j = startCol; j <=lastCol; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
