package cn.chenhuanming.octopus.annotation;

import java.lang.annotation.*;

/**
 * Field will be the line number of excel in entity of modelEntity,type must be int or Integer
 * @author chenhuanming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelLineNumber {
}
