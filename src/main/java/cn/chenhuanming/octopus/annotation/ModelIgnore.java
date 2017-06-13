package cn.chenhuanming.octopus.annotation;

import cn.chenhuanming.octopus.core.RowAssembler;

import java.lang.annotation.*;

/**
 * Field marked by this annotation will to be ignored by {@link RowAssembler}
 * @author chenhuanming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelIgnore {
}
