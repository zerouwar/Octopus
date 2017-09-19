package cn.chenhuanming.octopus.core;

import cn.chenhuanming.octopus.annotation.ModelIgnore;
import cn.chenhuanming.octopus.annotation.ModelLineNumber;
import cn.chenhuanming.octopus.annotation.ModelProperty;
import cn.chenhuanming.octopus.model.ImportModelProperty;
import cn.chenhuanming.octopus.util.DataUtil;
import cn.chenhuanming.octopus.util.MethodHandleUtil;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by chenhuanming on 2017-09-19.
 *
 * @author chenhuanming
 */
public class JavaConfigImportModel<T> implements ImportModel<T> {

    private Class<T> clazz;

    //保存每个带有@ModelProperty属性的注解信息及其MethodHandle
    private ImportModelProperty[] modelProperties;
    //行号MethodHandle
    private MethodHandle lineNumberHandler;

    public JavaConfigImportModel(Class clazz) {
        this.clazz = clazz;
        Field[] fields = clazz.getDeclaredFields();
        List<ImportModelProperty> handles = new ArrayList<>();
        for (Field field : fields) {
            //遇到ModelIgnore忽略掉
            if (field.getAnnotation(ModelIgnore.class) != null)
                continue;
            //处理ModelLineNumber
            if (lineNumberHandler==null&&field.getAnnotation(ModelLineNumber.class) != null) {
                if (field.getType() != Integer.class && field.getType() != int.class)
                    throw new IllegalArgumentException("type of property annotated by @ModelLineNumber must be Integer or int");
                lineNumberHandler = MethodHandleUtil.setterMethodHandle(field);
                continue;
            }
            //获取ModelProperty
            handles.add(getModelProperty(field));
        }
        modelProperties = new ImportModelProperty[handles.size()];
        IntStream.range(0, handles.size()).forEach(i -> modelProperties[i] = handles.get(i));
    }

    /**
     * 从类的Filed获取ModelProperty
     * @param field
     * @return
     */
    private ImportModelProperty getModelProperty(Field field) {
        ModelProperty property = field.getAnnotation(ModelProperty.class);
        ImportModelProperty handle = new ImportModelProperty();
        handle.setName(field.getName());

        MethodHandle mh = MethodHandleUtil.setterMethodHandle(field);
        handle.setHandle(mh);
        if (property != null) {
            handle.setDescription(property.value());
            handle.setWrongMsg(property.wrongMsg());
            handle.setPattern(DataUtil.notNullAndNotEmpty(property.pattern(), s -> Optional.of(Pattern.compile(property.pattern())), s -> Optional.empty()));
            handle.setDefaultValue(property.defaultValue());
            handle.setBlankable(property.blankable());
        } else {
            handle.setWrongMsg("");
            handle.setDescription("");
            handle.setPattern(Optional.empty());
            handle.setDefaultValue("");
            handle.setBlankable(true);
        }
        return handle;
    }

    @Override
    public Class<T> getImportClass() {
        return clazz;
    }

    @Override
    public ImportModelProperty[] getModelProperties() {
        return modelProperties;
    }

    @Override
    public MethodHandle getLineNumberHandler() {
        return lineNumberHandler;
    }
}
