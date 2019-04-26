package cn.chenhuanming.octopus.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : youthlin.chen @ 2019-04-26 11:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Header {

    String description();

    short headerFontSize() default 15;

    String headerColor() default "#000000";

    boolean headerIsBold() default true;

    String headerForegroundColor() default "#FFFFFF";

    String headerBorder() default "1,1,1,1";

    String headerBorderColor() default "#000000,#000000,#000000,#000000";

}
