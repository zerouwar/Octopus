package cn.chenhuanming.octopus.writer;

import java.io.Closeable;
import java.util.Collection;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public interface ExcelWriter extends Closeable {
    /**
     * Create new sheet and write data into it
     *
     * @param sheetName   new sheet name
     * @param sheetWriter sheet writer
     * @param data        data
     * @param <T>         data type
     * @return this
     */
    <T> ExcelWriter write(String sheetName, SheetWriter<T> sheetWriter, Collection<T> data);
}
