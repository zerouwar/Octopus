package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.dataConvert.DefaultValueConvertManager;
import cn.chenhuanming.octopus.dataConvert.SimpleConvertManager;
import cn.chenhuanming.octopus.model.ConfigurableModelEntity;
import cn.chenhuanming.octopus.model.SimpleModelEntity;

/**
 *
 * 该RowAssembler会重用modelEntity，适用于循环遍历的情况（即每次只需要一个modelEntity）。调用该类的
 * ModelEntity<T> assemble(Row row, T t)方法会得到一个重用的modelEntity，因此该modelEntity应该是stateless（无状态）的
 * @param <T>
 *
 */
public class ReusableRowAssembler<T> extends AbstractRowAssembler<T>{
    private final ConfigurableModelEntity<T> modelEntity;

    public ReusableRowAssembler(String date2StringFormat, int mantissaNumber, Class<T> clazz) {
        super(new SimpleConvertManager(date2StringFormat, mantissaNumber),new DefaultValueConvertManager(),clazz);
        modelEntity = new SimpleModelEntity<>();
    }

    @Override
    protected ConfigurableModelEntity<T> modelEntity() {
        modelEntity.exceptions().clear();
        return modelEntity;
    }
}
