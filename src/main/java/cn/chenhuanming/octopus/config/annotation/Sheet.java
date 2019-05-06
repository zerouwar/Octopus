package cn.chenhuanming.octopus.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : youthlin.chen @ 2019-04-26 11:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sheet {
    String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    Formatter[] formatters() default {};

    String dateFormatter() default DEFAULT_DATE_FORMATTER;

}
