package cn.chenhuanming.octopus.config.annotation;

import cn.chenhuanming.octopus.config.AbstractConfigFactory;
import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.config.FieldCellStyle;
import cn.chenhuanming.octopus.config.ImportValidation;
import cn.chenhuanming.octopus.formatter.DateFormatter;
import cn.chenhuanming.octopus.formatter.DefaultFormatterContainer;
import cn.chenhuanming.octopus.formatter.FormatterContainer;
import cn.chenhuanming.octopus.util.ColorUtils;
import cn.chenhuanming.octopus.util.ReflectionUtils;
import cn.chenhuanming.octopus.util.StringUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author : youthlin.chen @ 2019-04-26 12:00
 */
public class AnnotationConfigFactory extends AbstractConfigFactory {
    private Map<Class, cn.chenhuanming.octopus.formatter.Formatter> instanceMap = new ConcurrentHashMap<>();
    @Getter
    private final Class<?> modelClass;

    public AnnotationConfigFactory(Class modelClass) {
        if (modelClass.getAnnotation(Sheet.class) == null) {
            throw new IllegalArgumentException("the modelClass must have @Sheet annotation:" + modelClass);
        }
        this.modelClass = modelClass;
    }

    @Override
    public Config getConfig() {
        return Config.of(modelClass, readFormatter(), getFields(modelClass));
    }

    private FormatterContainer readFormatter() {
        DefaultFormatterContainer container = new DefaultFormatterContainer();
        Sheet sheet = modelClass.getAnnotation(Sheet.class);
        String dateFormatter = Sheet.DEFAULT_DATE_FORMATTER;
        if (sheet != null) {
            if (StringUtils.isNotEmpty(sheet.dateFormatter())) {
                dateFormatter = sheet.dateFormatter();
            }
        }
        container.addFormat(Date.class, new DateFormatter(dateFormatter));
        if (sheet != null) {
            for (Formatter formatter : sheet.formatters()) {
                Class target = formatter.target();
                Class<? extends cn.chenhuanming.octopus.formatter.Formatter> format = formatter.format();
                container.addFormat(target, getFormatterInstance(format));
            }
        }
        return container;
    }

    private cn.chenhuanming.octopus.formatter.Formatter getFormatterInstance(
            Class<? extends cn.chenhuanming.octopus.formatter.Formatter> formatClass) {
        try {
            cn.chenhuanming.octopus.formatter.Formatter instance;
            if (formatClass.getAnnotation(Shareable.class) != null) {
                instance = instanceMap.get(formatClass);
                if (instance == null) {
                    instance = formatClass.newInstance();
                    instanceMap.put(formatClass, instance);
                }
            } else {
                instance = formatClass.newInstance();
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        for (java.lang.reflect.Field declaredField : clazz.getDeclaredFields()) {
            cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation =
                    declaredField.getAnnotation(cn.chenhuanming.octopus.config.annotation.Field.class);
            Header header = declaredField.getAnnotation(Header.class);
            if (fieldAnnotation != null && header != null) {
                throw new IllegalArgumentException("a field can not have @Field and @Header both:" + declaredField);
            }
            if (fieldAnnotation != null) {
                Field.FieldBuilder field = Field.builder();
                field.name(declaredField.getName());
                field.description(fieldAnnotation.description());
                setFormatter(field, fieldAnnotation);
                setCellStyleConfig(field, fieldAnnotation);
                setHeaderCellStyleConfig(field, fieldAnnotation);
                setInvoker(field, declaredField.getName(), declaredField.getDeclaringClass());
                setImportValidation(field, fieldAnnotation);
                fields.add(field.build());
            }
            if (header != null) {
                Field.FieldBuilder field = Field.builder();
                field.name(declaredField.getName());
                field.description(header.description());
                setHeaderCellStyleConfig(field, header);
                setInvoker(field, declaredField.getName(), declaredField.getDeclaringClass());
                field.children(getFields(declaredField.getType()));
                fields.add(field.build());
            }
        }
        return fields;
    }

    private void setFormatter(Field.FieldBuilder field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
        Class<? extends cn.chenhuanming.octopus.formatter.Formatter> formatter = fieldAnnotation.formatter();
        if (!formatter.equals(cn.chenhuanming.octopus.formatter.Formatter.class)) {
            field.formatter(getFormatterInstance(formatter));
        } else {
            if (StringUtils.isNotEmpty(fieldAnnotation.dateFormat())) {
                field.formatter(new DateFormatter(fieldAnnotation.dateFormat()));
            }
        }
    }

    private void setCellStyleConfig(Field.FieldBuilder field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();

        short fontSize = fieldAnnotation.fontSize();
        builder.fontSize(fontSize);

        String color = fieldAnnotation.color();
        builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));

        boolean isBold = fieldAnnotation.isBold();
        builder.bold(isBold);

        String foregroundColor = fieldAnnotation.foregroundColor();
        builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, null)));

        String border = fieldAnnotation.border();
        builder.border(super.convertBorder(StringUtils.defaultIfEmpty(border, "0,0,0,0")));

        String borderColor = fieldAnnotation.borderColor();
        borderColor = StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000");
        builder.borderColor(super.convertBorderColor(borderColor));

        builder.width(fieldAnnotation.width());

        field.fieldCellStyle(builder.build());
    }

    private void setHeaderCellStyleConfig(Field.FieldBuilder field, Annotation headerOrField) {
        FieldCellStyle.FieldCellStyleBuilder builder = FieldCellStyle.builder();
        if (headerOrField instanceof Header
                || headerOrField instanceof cn.chenhuanming.octopus.config.annotation.Field) {
            short fontSize = 0;
            String color = null;
            boolean isBold = false;
            String foregroundColor = null;
            String border = null;
            String borderColor = null;

            if (headerOrField instanceof Header) {
                fontSize = ((Header) headerOrField).headerFontSize();
                color = ((Header) headerOrField).headerColor();
                isBold = ((Header) headerOrField).headerIsBold();
                foregroundColor = ((Header) headerOrField).headerForegroundColor();
                border = ((Header) headerOrField).headerBorder();
                borderColor = ((Header) headerOrField).headerBorderColor();
            }
            if (headerOrField instanceof cn.chenhuanming.octopus.config.annotation.Field) {
                fontSize = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField).headerFontSize();
                color = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField).headerColor();
                isBold = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField).headerIsBold();
                foregroundColor = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField)
                        .headerForegroundColor();
                border = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField).headerBorder();
                borderColor = ((cn.chenhuanming.octopus.config.annotation.Field) headerOrField).headerBorderColor();
            }
            builder.fontSize(fontSize);
            builder.color(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(color, "#000000")));
            builder.bold(isBold);
            builder.foregroundColor(ColorUtils.hex2Rgb(StringUtils.defaultIfEmpty(foregroundColor, "#FFFFFF")));
            builder.border(super.convertBorder(StringUtils.defaultIfEmpty(border, "1,1,1,1")));
            borderColor = StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000");
            builder.borderColor(super.convertBorderColor(borderColor));
            builder.width(0);

            field.headerFieldCellStyle(builder.build());
        } else {
            throw new IllegalArgumentException("unknown annotation, @Header or @Field required:" + headerOrField);
        }
    }

    private void setInvoker(Field.FieldBuilder field, String name, Class classType) {
        //getter
        Method picker = ReflectionUtils.readMethod(classType, name);
        field.picker(picker);
        //setter
        Method pusher = ReflectionUtils.writeMethod(classType, name);
        field.pusher(pusher);
    }

    private void setImportValidation(Field.FieldBuilder field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
        boolean blankable = fieldAnnotation.isBlankable();
        List<String> options = null;
        Pattern regex = null;

        String regexStr = fieldAnnotation.regex();
        if (StringUtils.isNotEmpty(regexStr)) {
            regex = Pattern.compile(regexStr);
        }

        String optionsStr = fieldAnnotation.options();
        if (StringUtils.isNotEmpty(optionsStr) && optionsStr.length() >= 2) {
            String[] split = optionsStr.split(StringUtils.OPTION_SPLITTER_VERTICAL);
            options = Arrays.asList(split);
        }

        field.importValidation(ImportValidation.of(blankable, options, regex));
    }

}
