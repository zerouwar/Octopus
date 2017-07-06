package cn.chenhuanming.octopus.core;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

/**
 * It should be stateless, thread-safe and single.
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public interface ExcelWriter<T> {
    void write(Workbook workbook,Collection<T> collection);

}
