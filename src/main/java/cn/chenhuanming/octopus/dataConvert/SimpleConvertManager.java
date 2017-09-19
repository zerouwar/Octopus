package cn.chenhuanming.octopus.dataConvert;

import cn.chenhuanming.octopus.exception.DataFormatException;
import cn.chenhuanming.octopus.exception.UnSupportedDataTypeException;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import cn.chenhuanming.octopus.util.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.Function;

/**
 * Created by chenhuanming on 2017-06-12.
 *
 * @author chenhuanming
 */
public class SimpleConvertManager extends AbstractConvertManager {
    public SimpleConvertManager(String date2StringFormat, int mantissaNumber) {
        StringBuilder builder = new StringBuilder("#");
        if (mantissaNumber > 0) {
            builder.append("0.");
            for (int i = 0; i < mantissaNumber; i++) {
                builder.append('0');
            }
        }
        setDecimalFormat(new DecimalFormat(builder.toString()));

        //初始化convertMap
        initConverterMap();
    }

    private void initConverterMap() {
        addConverter(Integer.class, (handle, cell) -> getNumericValue(handle, cell, Integer.class,Integer::valueOf));
        addConverter(int.class, (handle, cell) -> getNumericValue(handle, cell, int.class,Integer::valueOf));
        addConverter(Double.class, (handle, cell) -> getNumericValue(handle, cell, Double.class,Double::valueOf));
        addConverter(double.class, (handle, cell) -> getNumericValue(handle, cell, double.class,Integer::valueOf));
        addConverter(Long.class, (handle, cell) -> getNumericValue(handle, cell, Long.class,Long::valueOf));
        addConverter(long.class, (handle, cell) -> getNumericValue(handle, cell, long.class,Long::valueOf));
        addConverter(Short.class, (handle, cell) -> getNumericValue(handle, cell, Short.class,Short::valueOf));
        addConverter(short.class, (handle, cell) -> getNumericValue(handle, cell, short.class,Short::valueOf));
        addConverter(Float.class, (handle, cell) -> getNumericValue(handle, cell, Float.class,Float::valueOf));
        addConverter(float.class, (handle, cell) -> getNumericValue(handle, cell, float.class,Float::valueOf));
        addConverter(String.class, (handle, cell) -> {
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    return cell.getStringCellValue();
                case FORMULA:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return cell.getBooleanCellValue() + "";
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell))
                        return dateFormat.format(cell.getDateCellValue());
                    return decimalFormat.format(cell.getNumericCellValue());
                default:
                    throw new UnSupportedDataTypeException("not support for cell type:" + cell.getCellTypeEnum(),handle,cell);
            }
        });
        addConverter(Date.class, (handle, cell) -> {
            if(cell.getCellTypeEnum()==CellType.NUMERIC){
                if (DateUtil.isCellDateFormatted(cell))
                    return cell.getDateCellValue();
                else
                    throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+"is not a date",handle,cell);
            }
            else if (cell.getCellTypeEnum() == CellType.STRING)
                try {
                    return dateFormat.parse(cell.getStringCellValue());
                } catch (ParseException e) {
                    throw new DataFormatException(handle,Date.class,cell);
                }
            else
                throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+"is not a date",handle,cell);
        });
        addConverter(LocalDate.class, (handle, cell) -> {
            if(cell.getCellTypeEnum()==CellType.NUMERIC){
                if (DateUtil.isCellDateFormatted(cell))
                    return LocalDateTime.ofInstant(cell.getDateCellValue().toInstant(), zone).toLocalDate();
                else
                    throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+"is not a date",handle,cell);
            }
            else if (cell.getCellTypeEnum() == CellType.STRING)
                try {
                    return LocalDate.parse(cell.getStringCellValue());
                } catch (DateTimeParseException e) {
                    throw new DataFormatException(handle,LocalDate.class,cell);
                }
            else
                throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell) + "is not a date", handle,cell);
        });
        addConverter(LocalDateTime.class, (handle, cell) -> {
            if(cell.getCellTypeEnum()==CellType.NUMERIC){
                if (DateUtil.isCellDateFormatted(cell))
                    return LocalDateTime.ofInstant(cell.getDateCellValue().toInstant(), zone);
                else
                    throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+"is not a datetime",handle,cell);
            }
            else if (cell.getCellTypeEnum() ==  CellType.STRING)
                try {
                    return LocalDateTime.parse(cell.getStringCellValue());
                } catch (DateTimeParseException e) {
                    throw new DataFormatException(handle,LocalDateTime.class,cell);
                }
            else
                throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell) + "is not a datetime", handle,cell);
        });
        addConverter(LocalTime.class, (handle, cell) -> {
            if(cell.getCellTypeEnum()==CellType.NUMERIC){
                if (DateUtil.isCellDateFormatted(cell))
                    return LocalDateTime.ofInstant(cell.getDateCellValue().toInstant(), zone).toLocalTime();
                else
                    throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+"is not a time",handle,cell);
            }
            else if (cell.getCellTypeEnum() ==  CellType.STRING)
                try {
                    return LocalTime.parse(cell.getStringCellValue());
                } catch (DateTimeParseException e) {
                    throw new DataFormatException(handle,LocalTime.class,cell);
                }
            else
                throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell) + "is not a time", handle,cell);
        });

    }

    private Object getNumericValue(ImportModelProperty handle, Cell cell, Class clazz, Function<String,Object> stringFormat){
        if(cell.getCellTypeEnum()==CellType.NUMERIC||cell.getCellTypeEnum()==CellType.FORMULA)
            return cell.getNumericCellValue();
        else if(cell.getCellTypeEnum()==CellType.STRING){
            try{
                return stringFormat.apply(cell.getStringCellValue());
            }catch (Exception e){
                throw new DataFormatException(handle,clazz,cell);
            }
        }else
            throw new UnSupportedDataTypeException(CellUtil.positionMsg(cell)+" must be numeric",handle,cell);
    }

}
