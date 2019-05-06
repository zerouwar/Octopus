package cn.chenhuanming.octopus.config.annotation;

import cn.chenhuanming.octopus.formatter.Formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : youthlin.chen @ 2019-04-26 11:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    String description();

    short headerFontSize() default 15;

    String headerColor() default "#000000";

    boolean headerIsBold() default true;

    String headerForegroundColor() default "#FFFFFF";

    String headerBorder() default "1,1,1,1";

    String headerBorderColor() default "#000000,#000000,#000000,#000000";

    short fontSize() default 14;

    String color() default "#000000";

    boolean isBold() default false;

    String foregroundColor() default "";

    String border() default "0,0,0,0";

    String borderColor() default "#000000,#000000,#000000,#000000";

    String dateFormat() default "";

    Class<? extends Formatter> formatter() default Formatter.class;

    boolean isBlankable() default false;

    String regex() default "";

    String options() default "";

}
