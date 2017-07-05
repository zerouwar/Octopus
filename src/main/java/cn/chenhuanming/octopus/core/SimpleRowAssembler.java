package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.dataConvert.DefaultValueConvertManager;
import cn.chenhuanming.octopus.dataConvert.SimpleConvertManager;
import cn.chenhuanming.octopus.model.ConfigurableModelEntity;
import cn.chenhuanming.octopus.model.SimpleModelEntity;

/**
 * Created by Administrator on 2017-06-11.
 *
 * @author Administrator
 */
public class SimpleRowAssembler<T> extends AbstractRowAssembler<T> {
    public SimpleRowAssembler(String date2StringFormat, int mantissaNumber, Class<T> clazz) {
        super(new SimpleConvertManager(date2StringFormat, mantissaNumber),new DefaultValueConvertManager(), clazz);
    }

    @Override
    protected ConfigurableModelEntity<T> modelEntity() {
        return new SimpleModelEntity<>();
    }
}
