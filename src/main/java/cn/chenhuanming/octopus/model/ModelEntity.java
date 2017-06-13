package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.exception.ExcelImportException;

import java.util.List;

/**
 * Created by Administrator on 2017-06-09.
 */
public interface ModelEntity<T> {
    T getEntity();

    List<ExcelImportException> exceptions();

}
