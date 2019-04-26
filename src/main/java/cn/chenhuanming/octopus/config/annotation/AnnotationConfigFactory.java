package cn.chenhuanming.octopus.config.annotation;

import cn.chenhuanming.octopus.config.CachedConfigFactory;
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
import java.util.regex.Pattern;

/**
 * @author : youthlin.chen @ 2019-04-26 12:00
 */
public class AnnotationConfigFactory extends CachedConfigFactory {
    @Getter
    private final Class<?> modelClass;

    public AnnotationConfigFactory(Class modelClass) {
        if (modelClass.getAnnotation(Sheet.class) == null) {
            throw new IllegalArgumentException("the modelClass must have @Sheet annotation:" + modelClass);
        }
        this.modelClass = modelClass;
    }

    @Override
    protected Config readConfig() {
        Config config = new Config();
        config.setClassType(modelClass);
        config.setFormatterContainer(readFormatter());
        config.setFields(getFields(modelClass));
        return config;
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
                Field field = new Field();
                field.setName(declaredField.getName());
                field.setDescription(fieldAnnotation.description());
                setFormatter(field, fieldAnnotation);
                setCellStyleConfig(field, fieldAnnotation);
                setHeaderCellStyleConfig(field, fieldAnnotation);
                setInvoker(field, declaredField.getDeclaringClass());
                setImportValidation(field, fieldAnnotation);
                fields.add(field);
            }
            if (header != null) {
                Field field = new Field();
                field.setName(declaredField.getName());
                field.setDescription(header.description());
                setHeaderCellStyleConfig(field, header);
                setInvoker(field, declaredField.getDeclaringClass());
                field.setChildren(getFields(declaredField.getType()));
                fields.add(field);
            }
        }
        return fields;
    }

    private void setFormatter(Field field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
        String dateFormat = fieldAnnotation.dateFormat();
        if (StringUtils.isNotEmpty(dateFormat)) {
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            field.setDateFormat(dateFormatter);
            field.setFormatter(dateFormatter);
        }
        Class<? extends cn.chenhuanming.octopus.formatter.Formatter> formatter = fieldAnnotation.formatter();
        if (!formatter.equals(cn.chenhuanming.octopus.formatter.Formatter.class)) {
            field.setFormatter(getFormatterInstance(formatter));
        }
    }

    private void setCellStyleConfig(Field field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
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
        builder.border(convertBorder(StringUtils.defaultIfEmpty(border, "0,0,0,0")));

        String borderColor = fieldAnnotation.borderColor();
        borderColor = StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000");
        builder.borderColor(convertBorderColor(borderColor));

        field.setFieldCellStyle(builder.build());
    }

    private void setHeaderCellStyleConfig(Field field, Annotation headerOrField) {
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
            builder.border(convertBorder(StringUtils.defaultIfEmpty(border, "1,1,1,1")));
            borderColor = StringUtils.defaultIfEmpty(borderColor, "#000000,#000000,#000000,#000000");
            builder.borderColor(convertBorderColor(borderColor));

            field.setHeaderFieldCellStyle(builder.build());
        } else {
            throw new IllegalArgumentException("unknown annotation, @Header or @Field required:" + headerOrField);
        }
    }

    private void setInvoker(Field field, Class classType) {
        //getter
        Method picker = ReflectionUtils.readMethod(classType, field.getName());
        field.setPicker(picker);
        //setter
        Method pusher = ReflectionUtils.writeMethod(classType, field.getName());
        field.setPusher(pusher);
    }

    private void setImportValidation(Field field, cn.chenhuanming.octopus.config.annotation.Field fieldAnnotation) {
        ImportValidation validation = new ImportValidation();
        boolean isBlankable = fieldAnnotation.isBlankable();
        validation.setBlankable(isBlankable);

        String regex = fieldAnnotation.regex();
        if (!StringUtils.isEmpty(regex)) {
            validation.setRegex(Pattern.compile(regex));
        }

        String options = fieldAnnotation.options();
        if (!StringUtils.isEmpty(options) && options.length() >= 2) {
            String[] split = options.split(SPLITTER);
            validation.setOptions(Arrays.asList(split));
        }

        field.setImportValidation(validation);
    }

}
