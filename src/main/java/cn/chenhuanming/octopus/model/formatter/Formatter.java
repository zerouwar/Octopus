package cn.chenhuanming.octopus.model.formatter;

import cn.chenhuanming.octopus.exception.ParseException;

/**
 * @author chenhuanming
 * Created at 2018/12/17
 */
public interface Formatter<T> {

    /**
     * Format data from T to String when exporting
     *
     * @param t
     * @return
     */
    String format(T t);

    /**
     * Read String type data from excel and get T when importing
     * If data is not valid,then throw ParseException.It will catch in return value of @{# {@link cn.chenhuanming.octopus.core.CheckedSheetReader}}
     * @param str string value from excel
     * @return T
     * @throws ParseException when failed or is invalid data
     * @see cn.chenhuanming.octopus.core.CheckedSheetReader
     * @see cn.chenhuanming.octopus.model.CheckedData
     */
    T parse(String str) throws ParseException;
}
