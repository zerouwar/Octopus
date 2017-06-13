package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.exception.ExcelImportException;

/**
 * Created by Administrator on 2017-06-10.
 */
public interface ConfigurableModelEntity<T> extends ModelEntity<T>{
    void addException(ExcelImportException exception);

    void setEntity(T t);

}
