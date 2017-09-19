package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.model.ImportModelProperty;

import java.lang.invoke.MethodHandle;

/**
 * Created by chenhuanming on 2017-09-19.
 *
 * @author chenhuanming
 */
public interface ImportModel<T> {

    Class<T> getImportClass();

    ImportModelProperty[] getModelProperties();

    MethodHandle getLineNumberHandler();
}
