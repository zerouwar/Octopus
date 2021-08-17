package cn.chenhuanming.octopus.writer;

import java.io.Closeable;
import java.util.Collection;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public interface ExcelWriter extends Closeable {
    <T> ExcelWriter write(String sheetName, SheetWriter<T> sheetWriter, Collection<T> collection);
}
