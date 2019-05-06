package cn.chenhuanming.octopus.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : youthlin.chen @ 2019-04-26 12:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Formatter {
    Class target();

    Class<? extends cn.chenhuanming.octopus.formatter.Formatter> format();
}
