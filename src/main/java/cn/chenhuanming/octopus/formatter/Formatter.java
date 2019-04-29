package cn.chenhuanming.octopus.formatter;

import cn.chenhuanming.octopus.exception.ParseException;
import cn.chenhuanming.octopus.reader.CheckedSheetReader;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface Formatter<T> {

    /**
     * Format data from T to String when exporting
     * @param t your data object
     * @return string for writing into excel
     */
    String format(T t);

    /**
     * Read String type data from excel and get T when importing
     * If data is not valid,then throw ParseException.It will catch in return value of @{# {@link CheckedSheetReader}}
     * @param str string value from excel
     * @return T your data object
     * @throws ParseException when failed or is invalid data
     * @see CheckedSheetReader
     * @see cn.chenhuanming.octopus.model.CheckedData
     */
    T parse(String str) throws ParseException;
}
