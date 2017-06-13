package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ModelEntity;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by Administrator on 2017-06-08.
 */
public interface RowAssembler<T> {
    ModelEntity<T> assemble(Row row, T t);

    boolean isSupport(Row row);
}
