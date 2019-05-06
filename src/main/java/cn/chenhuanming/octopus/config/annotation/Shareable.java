package cn.chenhuanming.octopus.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * thread unsafe, only used when the formatter is stateless
 *
 * @author : youthlin.chen @ 2019-04-26 12:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Shareable {
}
