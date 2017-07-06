package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.exception.ExcelImportException;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-09.
 */
@ToString
public class SimpleModelEntity<T> implements ConfigurableModelEntity<T>{
    private T entity;
    private List<ExcelImportException> exceptions;

    public SimpleModelEntity() {
        exceptions = new ArrayList<>();
    }

    @Override
    public void addException(ExcelImportException exception){
        exceptions.add(exception);
    }

    @Override
    public void setEntity(T t) {
        this.entity = t;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public List<ExcelImportException> exceptions() {
        return exceptions;
    }
}
