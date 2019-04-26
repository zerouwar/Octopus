package cn.chenhuanming.octopus.config.annotation;

import cn.chenhuanming.octopus.config.CachedConfigFactory;

/**
 * @author : youthlin.chen @ 2019-04-26 11:56
 */
public abstract class AbstractAnnotationConfigFactory extends CachedConfigFactory {
    protected Class<?> modelClass;

    public AbstractAnnotationConfigFactory(Class modelClass) {
        /**
         *  todo 1.是否有必要强校验 // 2.考虑修改 {@link cn.chenhuanming.octopus.Octopus#writeOneSheet} 不传 sheetName 而在注解中定义
         */
        if (modelClass.getAnnotation(Sheet.class) == null) {
            throw new IllegalArgumentException("the modelClass must have @Sheet annotation:" + modelClass);
        }
        this.modelClass = modelClass;
    }

}
