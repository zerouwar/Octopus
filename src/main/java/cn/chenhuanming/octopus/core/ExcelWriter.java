package cn.chenhuanming.octopus.core;

import java.io.IOException;
import java.util.Collection;

/**
 * @author chenhuanming
 * Created at 2018/12/19
 */
public interface ExcelWriter {
    <T> ExcelWriter write(String sheetName, SheetWriter<T> sheetWriter, Collection<T> collection);

    void close() throws IOException;
}
