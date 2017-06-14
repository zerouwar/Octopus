package cn.chenhuanming.octopus.annotation;

import cn.chenhuanming.octopus.exception.ExcelImportException;
import cn.chenhuanming.octopus.model.ModelEntity;

import java.lang.annotation.*;

/**
 * this field will be a property in entity of {@link ModelEntity},actually the field without this annotation
 * also will be a property in entity of {@link ModelEntity}.if you don't want to transform the field
 * @author chenhuanming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelProperty {

    /**
     * description of field
     */
    String value() default "";

    /**
     * default value when cell is empty or empty string
     */
    String defaultValue() default "";

    /**
     * hint when it occur in the process of transform.It can find in exceptions of {@link ModelEntity},see {@link ExcelImportException}
     */
    String wrongMsg() default "";

    /**
     * pattern of regex which will be use to check the value of cell
     */
    String pattern() default "";

    /**
     * whether cell can be blank(In POI's words,cell type is blank,cell type is string but empty or cell is null)
     */
    boolean blankable() default true;
}
