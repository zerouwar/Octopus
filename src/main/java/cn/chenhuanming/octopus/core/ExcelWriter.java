package cn.chenhuanming.octopus.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Created by chenhuanming on 2017-07-01.
 *
 * @author chenhuanming
 */
public interface ExcelWriter<T> {
    void write(Collection<T> collection);

    void writeToStream(OutputStream os) throws IOException;

}
